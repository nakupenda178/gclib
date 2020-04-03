package com.github.guqt178.utils.intent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;

import java.io.File;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/23
 *     desc  : 意图相关工具类
 * </pre>
 */
public final class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 获取App具体设置的意图
     *
     * @param packageName 包名
     * @return intent
     */
    public static Intent getAppDetailsSettingsIntent(final String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取分享文本的意图
     *
     * @param content 分享文本
     * @return intent
     */
    public static Intent getShareTextIntent(final String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    /**
     * 获取word intent
     *
     * @return
     */
    public static Intent getWordFileIntent(final String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Uri uri = Uri.fromFile(new File(param));
        //intent.setType("application/msword");
        //intent.setTypeAndNormalize("application/pdf");
        intent.setDataAndTypeAndNormalize(file2Uri(getFileByPath(path)), "application/pdf");
        return intent;
    }


    /**
     *
     * @param context you know
     * @param path 文件路径
     */
    public static void startWordFileIntent(Context context, final String path) {
        Intent intent = getWordFileIntent(path);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "没有支持的应用", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 选择图片intent
     */
    public static Intent getPickIntentWithGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        return intent.setTypeAndNormalize("image/*");
    }


    ///////////////////////////////////////////////////////
    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取文件的uri
     *
     * @param file
     * @return
     */
    private static Uri file2Uri(final File file) {
        if (file == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //String authority = Utils.getApp().getPackageName() + ".utilcode.provider";
            //return FileProvider.getUriForFile(Utils.getApp(), authority, file);
            return Uri.parse("content://" + file.getAbsolutePath());
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 分享文本
     * @param uri     图片uri
     * @return intent
     */
    public static Intent getShareImageIntent(final String content, final Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取其他应用组件的意图
     *
     * @param packageName 包名
     * @param className   全类名
     * @return intent
     */
    public static Intent getComponentIntent(final String packageName, final String className) {
        return getComponentIntent(packageName, className, null);
    }

    /**
     * 获取其他应用组件的意图
     *
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     * @return intent
     */
    public static Intent getComponentIntent(final String packageName, final String className, final Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取关机的意图
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.SHUTDOWN"/>}</p>
     *
     * @return intent
     */
    public static Intent getShutdownIntent() {
        Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳至拨号界面意图
     *
     * @param phoneNumber 电话号码
     */
    public static Intent getDialIntent(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取拨打电话意图
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
     *
     * @param phoneNumber 电话号码
     */
    public static Intent getCallIntent(final String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳至发送短信界面的意图
     *
     * @param phoneNumber 接收号码
     * @param content     短信内容
     */
    public static Intent getSendSmsIntent(final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    /**
     * 获取拍照的意图
     *
     * @param outUri 输出的uri
     * @return 拍照的意图
     */
    public static Intent getCaptureIntent(final Uri outUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        return intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 选择视频
     *
     * @param activity
     */
    public static void selectVideo(Activity activity) {
        if (android.os.Build.BRAND.equals("Huawei")) {
            Intent intentPic = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intentPic, 2);
        }
        if (android.os.Build.BRAND.equals("Xiaomi")) {//是否是小米设备,是的话用到弹窗选取入口的方法去选取视频
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
            activity.startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), 2);
        } else {//直接跳到系统相册去选取视频
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
            } else {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("video/*");
            }
            activity.startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), 2);
        }

    }

    //    /**
    //     * 获取选择照片的Intent
    //     *
    //     * @return
    //     */
    //    public static Intent getPickIntentWithGallery() {
    //        Intent intent = new Intent(Intent.ACTION_PICK);
    //        return intent.setType("image*//*");
    //    }
    //
    //    /**
    //     * 获取从文件中选择照片的Intent
    //     *
    //     * @return
    //     */
    //    public static Intent getPickIntentWithDocuments() {
    //        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    //        return intent.setType("image*//*");
    //    }
    //
    //
    //    public static Intent buildImageGetIntent(final Uri saveTo, final int outputX, final int outputY, final boolean returnData) {
    //        return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData);
    //    }
    //
    //    public static Intent buildImageGetIntent(Uri saveTo, int aspectX, int aspectY,
    //                                             int outputX, int outputY, boolean returnData) {
    //        Intent intent = new Intent();
    //        if (Build.VERSION.SDK_INT < 19) {
    //            intent.setAction(Intent.ACTION_GET_CONTENT);
    //        } else {
    //            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
    //            intent.addCategory(Intent.CATEGORY_OPENABLE);
    //        }
    //        intent.setType("image*//*");
    //        intent.putExtra("output", saveTo);
    //        intent.putExtra("aspectX", aspectX);
    //        intent.putExtra("aspectY", aspectY);
    //        intent.putExtra("outputX", outputX);
    //        intent.putExtra("outputY", outputY);
    //        intent.putExtra("scale", true);
    //        intent.putExtra("return-data", returnData);
    //        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
    //        return intent;
    //    }
    //
    //    public static Intent buildImageCropIntent(final Uri uriFrom, final Uri uriTo, final int outputX, final int outputY, final boolean returnData) {
    //        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData);
    //    }
    //
    //    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY,
    //                                              int outputX, int outputY, boolean returnData) {
    //        Intent intent = new Intent("com.android.camera.action.CROP");
    //        intent.setDataAndType(uriFrom, "image*//*");
    //        intent.putExtra("crop", "true");
    //        intent.putExtra("output", uriTo);
    //        intent.putExtra("aspectX", aspectX);
    //        intent.putExtra("aspectY", aspectY);
    //        intent.putExtra("outputX", outputX);
    //        intent.putExtra("outputY", outputY);
    //        intent.putExtra("scale", true);
    //        intent.putExtra("return-data", returnData);
    //        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
    //        return intent;
    //    }
    //
    //    public static Intent buildImageCaptureIntent(final Uri uri) {
    //        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    //        return intent;
    //    }
}
