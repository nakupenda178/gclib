package com.github.guqt178.utils.intent

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 权限请求页适配，不同手机系统跳转到不同的权限请求页
 *
 * @author Donkor
 */
class SettingOperator(private val mContext: Context) {
    private val TAG = "SettingOperator"
    //自己的项目包名
    private val packageName = mContext.packageName

    fun jumpPermissionPage() {
        val name = Build.MANUFACTURER
        Log.e(TAG, "jumpPermissionPage --- name : $name")
        when (name) {
            "HUAWEI" -> goHuaWeiMainager()
            "vivo" -> goVivoMainager()
            "OPPO" -> goOppoMainager()
            "Coolpad" -> goCoolpadMainager()
            "Meizu" -> goMeizuMainager()
            "Xiaomi" -> goXiaoMiMainager()
            "samsung" -> goSangXinMainager()
            "Sony" -> goSonyMainager()
            "LG" -> goLGMainager()
            else -> goIntentSetting()
        }
    }

    private fun goLGMainager() {
        try {
            val intent = Intent(packageName)
            val comp = ComponentName("com.android.settings", "com.android.settings.Settings\$AccessLockSummaryActivity")
            intent.component = comp
            mContext.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            goIntentSetting()
        }
    }

    private fun goSonyMainager() {
        try {
            val intent = Intent(packageName)
            val comp = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            intent.component = comp
            mContext.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            goIntentSetting()
        }
    }

    private fun goHuaWeiMainager() {
        try {
            val intent = Intent(packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
            intent.component = comp
            mContext.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            goIntentSetting()
        }
    }

    private fun goXiaoMiMainager() {
        val rom = miuiVersion
        Log.e(TAG, "goMiaoMiMainager --- rom : $rom")
        val intent = Intent()
        if ("V6" == rom || "V7" == rom) {
            intent.action = "miui.intent.action.APP_PERM_EDITOR"
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
            intent.putExtra("extra_pkgname", packageName)
        } else if ("V8" == rom || "V9" == rom) {
            intent.action = "miui.intent.action.APP_PERM_EDITOR"
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
            intent.putExtra("extra_pkgname", packageName)
        } else {
            goIntentSetting()
        }
        mContext.startActivity(intent)
    }

    private fun goMeizuMainager() {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", packageName)
            mContext.startActivity(intent)
        } catch (localActivityNotFoundException: ActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace()
            goIntentSetting()
        }
    }

    private fun goSangXinMainager() { //三星4.3可以直接跳转
        goIntentSetting()
    }

    private fun goIntentSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", mContext.packageName, null)
        intent.data = uri
        try {
            mContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goOppoMainager() {
        doStartApplicationWithPackageName("com.coloros.safecenter")
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     * 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    private fun goCoolpadMainager() {
        doStartApplicationWithPackageName("com.yulong.android.security:remote")
        /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    private fun goVivoMainager() {
        doStartApplicationWithPackageName("com.bairenkeji.icaller")
        /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    /**
     * 此方法在手机各个机型设置中已经失效
     *
     * @return
     */
    private val appDetailSettingIntent: Intent
        private get() {
            val localIntent = Intent()
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", mContext.packageName, null)
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.action = Intent.ACTION_VIEW
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
                localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.packageName)
            }
            return localIntent
        }

    private fun doStartApplicationWithPackageName(packagename: String) { // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = mContext.packageManager.getPackageInfo(packagename, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageinfo == null) {
            return
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)
        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveinfoList = mContext.packageManager
                .queryIntentActivities(resolveIntent, 0)
        Log.e("PermissionPageManager", "resolveinfoList" + resolveinfoList.size)
        for (i in resolveinfoList.indices) {
            Log.e("PermissionPageManager", resolveinfoList[i].activityInfo.packageName + resolveinfoList[i].activityInfo.name)
        }
        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) { // packageName参数2 = 参数 packname
            val packageName = resolveinfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            val className = resolveinfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            val cn = ComponentName(packageName, className)
            intent.component = cn
            try {
                mContext.startActivity(intent)
            } catch (e: Exception) {
                goIntentSetting()
                e.printStackTrace()
            }
        }
    }

    companion object {
        private val miuiVersion: String?
            private get() {
                val propName = "ro.miui.ui.version.name"
                val line: String
                var input: BufferedReader? = null
                try {
                    val p = Runtime.getRuntime().exec("getprop $propName")
                    input = BufferedReader(
                            InputStreamReader(p.inputStream), 1024)
                    line = input.readLine()
                    input.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    return null
                } finally {
                    try {
                        input!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                return line
            }
    }

}