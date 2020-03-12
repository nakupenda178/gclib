package com.github.guqt178.http;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.github.guqt178.utils.AppInfoUtils;

import java.io.File;

/**
 * @describe: 基于系统自带带DownloadManager封装带下载文件工具类，主要用于app更新(兼容7.0)
 *<p>
 *     1.在Manifest中添加使用权限
 *     <uses-permission android:name="android.permission.INTERNET"/>
 *     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *
 *     2.在Manifest中添加7.0私有文件权限
 *     <provider
 *            android:name="android.support.v4.content.FileProvider"
 *            android:authorities="${applicationId}.provider"
 *            android:exported="false"
 *            android:grantUriPermissions="true">
 *
 *      <meta-data
 *           android:name="android.support.FILE_PROVIDER_PATHS"
 *           android:resource="@xml/provider_paths"/>
 *       </provider>
 *
 *     3.建立xml私有文件权限配置
 *     在res中建立xml文件夹，建立provider_paths.xml文件，文件中：
 *
 *     <?xml version="1.0" encoding="utf-8"?>
 *     <resources xmlns:android="http://schemas.android.com/apk/res/android">
 *           <paths>
 *              <external-path
 *                  name="download"
 *                  path=""/>
 *           </paths>
 *     </resources>
 *
 *</p>
 */

public class DownloadUtils {
    private DownloadManager mDownloadManager;
    private Context mContext;
    private long downloadId;
    private String apkName;

    public DownloadUtils(Context context) {
        mContext = context;
    }

    public void download(String url, String name) {
        final String packageName = "com.android.providers.downloads";
        int state = mContext.getPackageManager().getApplicationEnabledSetting(packageName);
        //检测下载管理器是否被禁用
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("温馨提示").setMessage
                    ("系统下载管理器被禁止，需手动打开").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + packageName));
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        mContext.startActivity(intent);
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            //正常下载流程
            apkName = name;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedOverRoaming(false);
            //通知栏显示
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle(AppInfoUtils.getAppName(mContext));
            request.setDescription("正在下载中...");
            request.setVisibleInDownloadsUi(true);
            //设置下载的路径
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
            //获取DownloadManager
            mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadId = mDownloadManager.enqueue(request);
            mContext.registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        cursor.close();
    }

    /**
     * 7.0兼容
     */
    private void installAPK() {
        File apkFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}
