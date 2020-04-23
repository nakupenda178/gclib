package com.github.guqt178.utils.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import android.widget.TextView
import com.github.guqt178.log.Alog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 会记住冷却时间的帮助类(界面推出后冷却时间继续,直到走完)
 */
class PersistentCooldown private constructor(private val lifecycleOwner: LifecycleOwner,
                                             private val who: TextView) : LifecycleObserver {


    private val context: Context = getContext()

    private val target: TextView = who

    private val sp_file_name: String="${this.context.packageName}_cool_down_config" //sp存储文件名

    private val sp: SharedPreferences=this.context.getSharedPreferences(sp_file_name, Context.MODE_PRIVATE)

    private val key_timestamp: String = "save_timestamp_${target.id}" //退出界面的时间戳key

    private val key_remaintime: String = "remain_time_${target.id}" //退出界面时还剩下多少没走完key


    private var displayCooldownCount = -1L //界面显示的倒计时实际值

    private var cooldownTask: Disposable? = null

    private val lifecycle: Lifecycle = lifecycleOwner.lifecycle

    // <editor-fold defaultstate="collapsed" desc="生命周期方法">


    init {
        lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifeDestroy() {
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onLifeResume() {
        cooldown(false)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onLifeStop() {

        if (displayCooldownCount >= 1L) {
            //倒计时未走完,保存信息
            sp.edit().putLong(key_timestamp, System.currentTimeMillis())
                    .putLong(key_remaintime, displayCooldownCount).apply()
        } else {
            //倒计时完成后清理现场
            sp.edit().remove(key_timestamp).remove(key_remaintime).apply()
        }

        if (cooldownTask?.isDisposed == false) {
            cooldownTask?.dispose()
            cooldownTask = null
        }
    }

    // </editor-fold>

    fun onClick(action: (() -> Unit)? = null) {
        this.target.setOnClickListener { v ->
            action?.invoke()
            cooldown()
        }
    }


    /**
     * 关闭界面后依然记住冷却时间
     * @param initiative true主动触发(点击动作触发)
     */
    private fun cooldown(initiative: Boolean = true, totalTime: Number = 60) {

        val defaultTotalTime = totalTime.toLong()

        if (initiative) {
            //第一次触发初始值动作,用默认时间60s作为开始
            doStartCooldown(defaultTotalTime)
        } else {
            //1.这一步获取上一次界面退出时保存的值
            val savedTimeStamp = sp.getLong(key_timestamp, System.currentTimeMillis())
            val remainTime = sp.getLong(key_remaintime, -1)
            //重新进入界面的间隔时间,比如上一次界面退出后,过10s在进来,则该值为10s
            val second = (System.currentTimeMillis() - savedTimeStamp) / 1000
            //保存的值减去时间间隔得到实际剩余未走走完的时间
            val delta = remainTime - second
            //若果倒计时没有走完,继续走
            if (delta > 0) doStartCooldown(delta)
        }
    }

    private fun getContext() = when (lifecycleOwner) {
        is Activity -> lifecycleOwner
        is Fragment -> lifecycleOwner.context!!
        else -> throw IllegalStateException("lifecycleOwner must be AppcompatActivity or AppcompatFragment")
    }

    @SuppressLint("SetTextI18n")
    private fun doStartCooldown(totalTime: Long) {
        //已经在倒计时的话或有(
        val labelIndex = target.text.indexOf("(")
        //原始文本值,倒计时完后要恢复
        val originalText = target.text.substring(0, if (labelIndex != -1) labelIndex else target.text.length)
        //Rx方法倒计时实现
        cooldownTask = Flowable.intervalRange(0, (totalTime + 2), 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    target.isEnabled = false
                }
                .doOnNext { time ->
                    displayCooldownCount = totalTime - time
                    target.text = "$originalText(${displayCooldownCount}s)"
                }
                .doOnComplete {
                    target.isEnabled = true
                    target.text = originalText
                }
                .subscribe()
    }

    companion object {
        @JvmStatic
        fun newInstance(lifecycleOwner: LifecycleOwner, who: TextView) = PersistentCooldown(lifecycleOwner,who)
    }
}