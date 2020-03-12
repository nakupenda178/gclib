package com.moerlong.baselibrary.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Hzf  on  2019/1/10
 */
public class FileUtils {
  public static boolean fileCopy(String oldFilePath, String newFilePath) throws IOException {
    //如果原文件不存在
    if (fileExists(oldFilePath) == false) {
      return false;
    }
    //获得原文件流
    FileInputStream inputStream = new FileInputStream(new File(oldFilePath));
    byte[] data = new byte[1024];
    //输出流
    FileOutputStream outputStream = new FileOutputStream(new File(newFilePath));
    //开始处理流
    while (inputStream.read(data) != -1) {
      outputStream.write(data);
    }
    inputStream.close();
    outputStream.close();
    return true;
  }

  public static boolean fileExists(String filePath) {
    File file = new File(filePath);
    return file.exists();
  }

  /**
   *
   * @param context
   * @param uri
   * @return
   */
  public static String getFilePathFromUri(Context context, Uri uri) {
    if (uri == null) {
      return null;
    }
    String scheme = uri.getScheme();
    String filePath = null;
    if (TextUtils.isEmpty(scheme) || TextUtils.equals(ContentResolver.SCHEME_FILE, scheme)) {
      filePath = uri.getPath();
    } else if (TextUtils.equals(ContentResolver.SCHEME_CONTENT, scheme)) {
      String[] filePathColumn = { MediaStore.MediaColumns.DATA };
      Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
      if (cursor != null) {
        if (cursor.moveToFirst()) {
          int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
          if (columnIndex > -1) {
            filePath = cursor.getString(columnIndex);
          }
        }
        cursor.close();
      }
    }
    return filePath;
  }

  /**
   * 主要是兼容7.0以后的fileProvider 把URI 以content provider 方式 对外提供的解析方法
   */
  public static File getFileFromUri(Uri uri, Context context) {
    if (uri == null) {
      return null;
    }
    switch (uri.getScheme()) {
      case "content":
        return getFileFromContentUri(uri, context);
      case "file":
        return new File(uri.getPath());
      default:
        return null;
    }
  }

  /**
   * Gets the corresponding path to a file from the given content:// URI
   *
   * @param contentUri The content:// URI to find the file path from
   * @param context Context
   * @return the file path as a string
   */

  private static File getFileFromContentUri(Uri contentUri, Context context) {
    if (contentUri == null) {
      return null;
    }
    File file = null;
    String filePath;
    String fileName;
    String[] filePathColumn =
        { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
    ContentResolver contentResolver = context.getContentResolver();
    Cursor cursor = contentResolver.query(contentUri, filePathColumn, null, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
      filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
      fileName = cursor.getString(cursor.getColumnIndex(filePathColumn[1]));
      cursor.close();
      if (!TextUtils.isEmpty(filePath)) {
        file = new File(filePath);
      }
      if (!file.exists() || file.length() <= 0 || TextUtils.isEmpty(filePath)) {
        filePath = getPathFromInputStreamUri(context, contentUri, fileName);
      }
      if (!TextUtils.isEmpty(filePath)) {
        file = new File(filePath);
      }
    }
    return file;
  }

  /**
   * 用流拷贝文件一份到自己APP目录下
   */
  public static String getPathFromInputStreamUri(Context context, Uri uri, String fileName) {
    InputStream inputStream = null;
    String filePath = null;

    if (uri.getAuthority() != null) {
      try {
        inputStream = context.getContentResolver().openInputStream(uri);
        File file = createTemporalFileFrom(context, inputStream, fileName);
        filePath = file.getPath();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (inputStream != null) {
            inputStream.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return filePath;
  }

  private static File createTemporalFileFrom(Context context, InputStream inputStream,
      String fileName) throws IOException {
    File targetFile = null;

    if (inputStream != null) {
      int read;
      byte[] buffer = new byte[8 * 1024];
      //自己定义拷贝文件路径
      targetFile = new File(context.getCacheDir(), fileName);
      if (targetFile.exists()) {
        targetFile.delete();
      }
      OutputStream outputStream = new FileOutputStream(targetFile);

      while ((read = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, read);
      }
      outputStream.flush();

      try {
        outputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return targetFile;
  }

    /**
     * 通过文件路径获取文件名
     * @param filePath
     * @return
     */
  public static String getFileNameByPath(String filePath){
      File target = new File(filePath);
      if(target.isFile()&&target.exists()){
          int index = filePath.lastIndexOf(File.separator);
          if(index!=-1){
              String fileName = filePath.substring(index+1);
              return fileName;
          } else {
              return filePath;
          }
      }
      return "";
  }

    /**
     * 根据文件路径生成一个新的路径
     * @param filePath
     * @return
     */
  public static String createFileWithNewName(String filePath){
      String newFileName = "compress_"+getFileNameByPath(filePath);
      File newFile = new File(new File(filePath).getParent(), newFileName);
      if(!newFile.exists()) {
          try {
              newFile.createNewFile();
              return newFile.getAbsolutePath();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      return "";
  }

  public static String getPath(final Context context, final Uri uri) {

    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }

        // TODO handle non-primary volumes
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        return getDataColumn(context, contentUri, null, null);
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[] {
                split[1]
        };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {
      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }

  public static String getDataColumn(Context context, Uri uri, String selection,
                                     String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {
            column
    };

    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
              null);
      if (cursor != null && cursor.moveToFirst()) {
        final int column_index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(column_index);
      }
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return null;
  }

  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }
}
