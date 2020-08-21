package com.github.guqt178.utils.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.github.guqt178.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    private static final String TAG = "SDK_GU_QT.Util";

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // 创建合适文件大小的数组
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        //Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }

            Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Log.e(TAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }

    public static int parseInt(final String string, final int def) {
        try {
            return (string == null || string.length() <= 0) ? def : Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }


    public boolean isNeedCheckPermission() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    /**
     * 测量 view 的大小，返回宽高
     */
    public int[] measureView(View view) {
        int[] arr = new int[2];
        int spec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        arr[0] = view.getMeasuredWidth();
        arr[1] = view.getMeasuredHeight();
        return arr;
    }


    /**
     * 连续点击达到点击次数后回调监听
     */
    public void multiClickListener(View view, int frequency, View.OnClickListener listener) {
        view.setTag(R.id.multiClickFrequency, 0);
        view.setTag(R.id.multiClickLastTime, System.currentTimeMillis());
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int f = (int) (view.getTag(R.id.multiClickFrequency));
                if (f == frequency) {
                    view.setTag(R.id.multiClickFrequency, 0);
                    view.setTag(R.id.multiClickLastTime, System.currentTimeMillis());
                    if (listener != null) {
                        listener.onClick(v);
                    }
                } else {
                    long lastTime = (Long) (view.getTag(R.id.multiClickLastTime));
                    if (System.currentTimeMillis() - lastTime < 400) {
                        view.setTag(R.id.multiClickFrequency, f + 1);
                    }
                    view.setTag(R.id.multiClickLastTime, System.currentTimeMillis());
                }
            }
        });
    }

    /**
     * 检查目标缓存目录是否存在，如果存在则返回这个目录，如果不存在则新建这个目录
     *
     * @return
     */
    public static File checkTargetCacheDir(String storageDir) {

        File file = null;
        file = new File(storageDir);

        if (!file.exists()) {
            file.mkdirs();//创建目录
        }

        if (file != null && file.exists())
            return file;//文件已经被成功创建
        else {
            return null;//即时经过以上检查，文件还是没有被准确的创建
        }
    }

    /**
     * 将bitmap写入一个file中
     *
     * @return 保存bitmap的file对象
     */
    public static File convertToFile(Bitmap bitmap, String storageDir, String prefix) throws IOException {
        File cacheDir = checkTargetCacheDir(storageDir);
        //以时间戳生成一个临时文件名称
        cacheDir = createFile(cacheDir, prefix, ".jpg");
        boolean created = false;//是否创建成功,默认没有创建
        if (!cacheDir.exists()) created = cacheDir.createNewFile();
        if (created)//将图片写入目标file,100表示不压缩,Note:png是默认忽略这个参数的
            bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(cacheDir));
        return cacheDir;
    }


    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    /**
     * 压缩图片质量
     *
     * @param image
     * @param desireSize 默认可以穿200代表将图片压缩到200k一下
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int desireSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int offset = 100;
        while (baos.toByteArray().length / 1024 > desireSize) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩

            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, offset, baos);//这里压缩options%，把压缩后的数据存放到baos中
            offset -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //压缩图片大小
    public static Bitmap resizeImageSize(File file) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 1;
        Bitmap bitmap = null;
        while (true) {
            if (((options.outWidth / i) <= 600)
                    && ((options.outHeight / i) <= 600)) {
                in = new BufferedInputStream(
                        new FileInputStream(file));
                options.inSampleSize = i;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 使用矩阵缩放图片至期待的宽高
     *
     * @param source       被缩放的图片
     * @param expectWidth  期待的宽
     * @param expectHeight 期待的高
     * @return 返回压缩后的图片
     */
    public static Bitmap zoomBitmap(Bitmap source, float expectWidth, float expectHeight) {
        // 获取这个图片的宽和高
        float width = source.getWidth();
        float height = source.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        //默认不缩放
        float scaleWidth = 1;
        float scaleHeight = 1;
        // 计算宽高缩放率
        if (expectWidth < width) {
            scaleWidth = ((float) expectWidth) / width;
        }
        if (expectHeight < height) {
            scaleHeight = ((float) expectHeight) / height;
        }
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


    /**
     * 压缩Bitmap,同时使用两种策略压缩,先压缩宽高，再压缩质量
     *
     * @return 存储Bitmap的文件
     * @throws IOException
     */
    public static File compressBitmap(String url, String storageDir, String prefix) throws IOException {
        if (!TextUtils.isEmpty(url)) {
            File img = new File(url);
            Bitmap bitmap = resizeImageSize(img);
            bitmap = compressImage(bitmap, 200);
            return convertToFile(bitmap, storageDir, prefix);
        }
        return null;
    }

}
