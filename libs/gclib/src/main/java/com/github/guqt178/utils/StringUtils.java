package com.moerlong.baselibrary.utils.download;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jxnk25 on 2016/12/30.
 *
 * @link https://xiaohaibin.github.io/
 * @email： xhb_199409@163.com
 * @github: https://github.com/xiaohaibin
 * @description： 字符串处理工具类
 */

public class StringUtils {

    /**
     * 描述：将null转化为“”.
     *
     * @param str 指定的字符串
     * @return 字符串的String类型
     */
    public static String parseEmpty(String str) {
        if (str == null || "null".equalsIgnoreCase(str.trim())) {
            str = "";
        }
        return str.trim();
    }


    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    /**
     * 将list数组tostring转换成逗号分隔的字符串数据
     *
     * @param list 要转化的数组
     * @return
     */
    public static String ArrayToString(List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : list) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 将list数组tostring转换成空格分隔的字符串数据
     *
     * @param list 要转化的数组
     * @return
     */
    public static String ArrayToStringSpace(List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : list) {
            if (flag) {
                result.append(" ");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 去掉字符串中的空格
     *
     * @param str
     * @return
     */
    public static String removeSpace(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.replaceAll(" ", "");
    }

    /**
     * 判断网址是否有效
     *
     * @param link
     * @return
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 格式化价格
     *
     * @param price
     * @return
     */
    public static String forMatPrice(@NonNull Double price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(price);
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    public static String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }


    /**
     * 隐藏手机号码中间四位
     *
     * @param phone
     * @return
     */
    public static String hidePhoneNumber(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return "";
    }

    /**
     * 隐藏手机号码中间四位
     *
     * @param phone
     * @return
     */
    public static String hideIdCard(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            return phone.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1********$2");
        }
        return "";
    }

    /**
     * 截取图片地址中的id
     * http://trade-10002987.image.myqcloud.com/31761-58df14e8cff82
     *
     * @param imageUrl
     * @return
     */
    public static String getImagePicId(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        }
        return "";
    }

    /**
     * 将系统表情转化为字符串
     *
     * @param s
     * @return
     */
    public static String getEmojiString(String s) {
        int length = s.length();
        String context = "";
        //循环遍历字符串，将字符串拆分为一个一个字符
        for (int i = 0; i < length; i++) {
            char codePoint = s.charAt(i);
            //判断字符是否是emoji表情的字符
            if (isEmojiCharacter(codePoint)) {
                //如果是将以中括号括起来
                String emoji = "[" + Integer.toHexString(codePoint) + "]";
                context = context + emoji;
                continue;
            }
            context = context + codePoint;
        }
        return context;
    }

    /**
     * 是否包含表情
     *
     * @param codePoint
     * @return 如果不包含 返回false,包含 则返回true
     */

    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
}
