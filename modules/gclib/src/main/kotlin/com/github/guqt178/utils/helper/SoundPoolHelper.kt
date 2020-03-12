package com.github.guqt178.utils.helper

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.IntDef
import android.support.annotation.NonNull
import android.support.annotation.RawRes
import android.support.v4.util.SimpleArrayMap
import android.util.Log.i
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * 封装了SoundPool
 */
class SoundPoolHelper @JvmOverloads constructor(
    @NonNull private val context: Context,
    private var maxStream: Int,
    @TYPE streamType: Int
) : LifecycleObserver {

    private val TAG = "SoundPoolHelper"

    private var NOW_RINGTONE_TYPE = RingtoneManager.TYPE_NOTIFICATION

    @IntDef(
        TYPE_MUSIC,
        TYPE_ALARM,
        TYPE_RING
    )
    @Retention(RetentionPolicy.SOURCE)
    annotation class TYPE

    @IntDef(
        RING_TYPE_MUSIC,
        RING_TYPE_ALARM,
        RING_TYPE_RING
    )
    @Retention(RetentionPolicy.SOURCE)
    annotation class RING_TYPE

    init {

        if (null == soundPool) {
            val at = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes.Builder()
                    .setFlags(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
            soundPool = SoundPool
                .Builder()
                .setMaxStreams(maxStream)
                .setAudioAttributes(at)
                .build()
            loadDefault()
            observerLifeEvent()
        } else {
            playDefault()
        }
    }

    /**
     * soundPool自己会释放资源
     */
    private fun observerLifeEvent() {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    /**
     * 设置RingtoneType，这只是关系到加载哪一个默认音频
     * 需要在load之前调用
     * @param ringtoneType  ringtoneType
     * @return  this
     */
    fun setRingtoneType(@RING_TYPE ringtoneType: Int): SoundPoolHelper {
        NOW_RINGTONE_TYPE = ringtoneType
        return this
    }

    /**
     * 加载音频资源
     * @param context   上下文
     * @param resId     资源ID
     * @return  this
     */
    fun load(ringtoneName: String, @RawRes resId: Int): SoundPoolHelper {
        if (maxStream == 0) return this
        maxStream--
        soundPool?.load(context, resId, 1)
        soundPool?.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                ringtoneIds.put(ringtoneName, sampleId)
                play(ringtoneName, false)
            } else {
                i(TAG, "load error")
            }
        }
        return this
    }

    /**
     * 加载默认的铃声
     */
    private fun loadDefault() {
        val uri = getSystemDefaultRingtoneUri(context)
        if (uri == null)
            i(TAG, "路径为空")
        else
            load(
                DEFAULT_NAME, uri2Path(
                    context,
                    uri
                ) ?: "")
    }

    /**
     * 加载铃声
     * @param ringtoneName 自定义铃声名称
     * @param ringtonePath 铃声路径
     */
    fun load(ringtoneName: String, ringtonePath: String): SoundPoolHelper {
        if (maxStream == 0)
            return this
        maxStream--
        soundPool?.load(ringtonePath, 1)
        soundPool?.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                ringtoneIds.put(ringtoneName, sampleId)
                play(ringtoneName, false)
            } else {
                i(TAG, "load error")
            }
        }
        return this
    }

    /**
     * int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) ：
     * 1)该方法的第一个参数指定播放哪个声音；
     * 2) leftVolume 、
     * 3) rightVolume 指定左、右的音量：
     * 4) priority 指定播放声音的优先级，数值越大，优先级越高；
     * 5) loop 指定是否循环， 0 为不循环， -1 为循环；
     * 6) rate 指定播放的比率，数值可从 0.5 到 2 ， 1 为正常比率。
     */
    private fun play(ringtoneName: String, isLoop: Boolean) {
        if (ringtoneIds.containsKey(ringtoneName)) {
            soundPool?.play(
                ringtoneIds[ringtoneName] ?: -1, 1f, 1f, 1, if (isLoop) -1 else 0, 1f)
        }
    }

    private fun playDefault() {
        play(DEFAULT_NAME, false)
    }

    /**
     * 释放资源
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() {
        soundPool?.release()
        soundPool = null
    }

    /**
     * 获取系统默认铃声的Uri
     * @param context  上下文
     * @return  uri
     */
    private fun getSystemDefaultRingtoneUri(context: Context): Uri? {
        return try {
            RingtoneManager.getActualDefaultRingtoneUri(context, NOW_RINGTONE_TYPE)
        } catch (e: Exception) {
            null
        }

    }

    companion object {

        /*常量*/
        const val TYPE_MUSIC = AudioManager.STREAM_MUSIC
        const val TYPE_ALARM = AudioManager.STREAM_ALARM
        const val TYPE_RING = AudioManager.STREAM_RING
        const val RING_TYPE_MUSIC = RingtoneManager.TYPE_ALARM
        const val RING_TYPE_ALARM = RingtoneManager.TYPE_NOTIFICATION
        const val RING_TYPE_RING = RingtoneManager.TYPE_RINGTONE

        const val DEFAULT_NAME = "default_ring"

        /*变量*/
        private var soundPool: SoundPool? = null
        private val ringtoneIds: SimpleArrayMap<String, Int> = SimpleArrayMap(3)

        @JvmOverloads
        private fun get(
            context: Context,
            maxStream: Int = 3,
            @TYPE streamType: Int = TYPE_ALARM
        ) = SoundPoolHelper(
            context,
            maxStream,
            streamType
        )

        @JvmOverloads
        fun play(context: Context) =
            get(context)

        /**
         * 把 Uri 转变 为 真实的 String 路径
         * @param context 上下文
         * @param uri  URI
         * @return 转换结果
         */
        private fun uri2Path(context: Context, uri: Uri?): String? {
            if (null == uri) return null
            val scheme = uri.scheme
            var data: String? = null
            if (scheme == null)
                data = uri.path
            else if (ContentResolver.SCHEME_FILE == scheme)
                data = uri.path
            else if (ContentResolver.SCHEME_CONTENT == scheme) {
                val cursor = context.contentResolver.query(
                    uri,
                    arrayOf(MediaStore.Images.ImageColumns.DATA),
                    null,
                    null,
                    null
                )
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                        if (index > -1) {
                            data = cursor.getString(index)
                        }
                    }
                    cursor.close()
                }
            }
            return data
        }
    }
}
