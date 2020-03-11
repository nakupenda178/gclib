package com.github.guqt178.utils

import android.app.Notification
import android.app.Notification.VISIBILITY_SECRET
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.FileProvider
import com.github.guqt178.R
import java.io.File
import java.util.*

/**
 * 描述:     通知工具类
 *
 * @author 阿钟
 */
object NotificationHelper {

    /**
     * 获取设置的通知渠道id
     */
    @get:RequiresApi(api = Build.VERSION_CODES.O)
    private val notificationChannelId = System.currentTimeMillis().toString()
    private val CHANNEL_BOUND = 1024
    private val CHANNEL_NAME = "moerlong_im"

    /**
     * 构建一个消息
     *
     * @param context 上下文
     * @param icon    图标id
     * @param title   标题
     * @param content 内容
     */
    private fun builderNotification(context: Context, icon: Int, title: String, content: String): NotificationCompat.Builder {
        var channelId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = notificationChannelId
        }
        return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentText(content)
                //不能删除
                .setAutoCancel(true)
                //正在交互（如播放音乐）
                .setOngoing(false)
    }

    /**
     * 显示刚开始下载的通知
     *
     * @param context 上下文
     * @param icon    图标
     * @param title   标题
     * @param content 内容
     */
    fun showNotification(context: Context, icon: Int, title: String, content: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager)
        }
        val builder = builderNotification(context, icon, title, content)
                .setDefaults(Notification.DEFAULT_SOUND)
        manager.notify(generateChannelId(), builder.build())
    }


    /**
     * 显示下载完成的通知,点击进行安装
     *
     * @param context     上下文
     * @param icon        图标
     * @param title       标题
     * @param content     内容
     * @param authorities Android N 授权
     * @param apk         安装包
     */
    fun showDoneNotification(context: Context, icon: Int, title: String, content: String,
                             authorities: String, apk: File) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //不知道为什么需要先取消之前的进度通知，才能显示完成的通知。
        //manager.cancel(generateChannelId());
        val intent = Intent(Intent.ACTION_VIEW)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, authorities, apk)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(apk)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val builder = builderNotification(context, icon, title, content)
                .setContentIntent(pi)
        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        manager.notify(generateChannelId(), notification)
    }

    /**
     * 显示新消息提醒
     *
     * @param context 上下文
     * @param icon    图标
     * @param title   标题
     * @param content 内容
     */
    fun showNewMessageNotification(context: Context, icon: Int, title: String, content: String, senderId: String, senderName: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager)
        }
       /* val intent = Intent(context, ChatAty::class.java)
        intent.putExtra(ChatAty.KEY_WHO, senderId)
        intent.putExtra(ChatAty.KEY_NAME, senderName)*/
        //val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val pi = PendingIntent.getActivity(context, 0, null, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = builderNotification(context, icon, title, content)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_LIGHTS)
        manager.notify(generateChannelId(), builder.build())
    }

    /**
     * @param context
     * @param content
     */
    fun showNewMessageNotification(context: Context, content: String, senderId: String, senderName: String) {
        showNewMessageNotification(context, R.drawable.sample_footer_loading, "收到一条新消息!!", content, senderId, senderName)
    }


    /**
     * 获取通知栏开关状态
     *
     * @return true |false
     */
    fun notificationEnable(context: Context): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        return notificationManagerCompat.areNotificationsEnabled()
    }

    /**
     * 适配 Android O 通知渠道
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun afterO(manager: NotificationManager) {
        //IMPORTANCE_LOW：默认关闭声音与震动、IMPORTANCE_DEFAULT：开启声音与震动
        val channel = NotificationChannel(
            notificationChannelId, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
        //是否在桌面icon右上角展示小圆点
        channel.enableLights(true)
        //是否绕过请勿打扰模式
        channel.canBypassDnd()
        //闪光灯
        channel.enableLights(true)
        //锁屏显示通知
        channel.lockscreenVisibility = VISIBILITY_SECRET
        //闪关灯的灯光颜色
        channel.lightColor = Color.RED
        //桌面launcher的消息角标
        channel.canShowBadge()
        //是否允许震动
        channel.enableVibration(true)
        //获取系统通知响铃声音的配置
        channel.audioAttributes
        //获取通知取到组
        channel.group
        //设置可绕过  请勿打扰模式
        channel.setBypassDnd(true)
        //设置震动模式
        channel.vibrationPattern = longArrayOf(100, 100, 200)
        //是否会有灯光
        channel.shouldShowLights()
        manager.createNotificationChannel(channel)
    }

    private fun generateChannelId(): Int {
        return Random().nextInt(CHANNEL_BOUND)
    }

}
