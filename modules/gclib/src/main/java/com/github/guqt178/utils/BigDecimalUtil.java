package com.github.guqt178.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {

    // 除法运算默认精度
    private static final int DEF_DIV_SCALE = 5;

    private BigDecimalUtil() {
    }

    /**
     * 精确加法
     */
    public static String add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2).stripTrailingZeros().toPlainString();
    }

    /**
     * 精确减法
     */
    public static String sub(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2).stripTrailingZeros().toPlainString();
    }

    /**
     * 精确乘法
     */
    public static String mul(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).stripTrailingZeros().toPlainString();
    }

    /**
     * 精确除法 使用默认精度
     */
    public static String div(double value1, double value2) {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法
     *
     * @param scale 精度
     */
    public static String div(double value1, double value2, int scale) {
        if (scale < 0) {
            scale = DEF_DIV_SCALE;
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
    }

    /**
     * 精确加法
     */
    public static String add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).stripTrailingZeros().toPlainString();
    }

    /**
     * 精确减法
     */
    public static String sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2).stripTrailingZeros().toPlainString();
    }

    /**
     * 精确乘法
     */
    public static String mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).stripTrailingZeros().toPlainString();
    }

    /**
     * 精确除法 使用默认精度
     */
    public static String div(String value1, String value2) {
        return div(value1, value2, DEF_DIV_SCALE);
    }


    /**
     * 精确除法
     *
     * @param scale 精度
     */
    public static String div(String value1, String value2, int scale) {
        if (scale < 0) {
            scale = DEF_DIV_SCALE;
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
    }

    /**
     * 四舍五入
     *
     * @param scale 小数点后保留几位
     */

    public static String round(double v, int scale) {
        return div(v, 1, scale);
    }

    /**
     * 取精度,四舍五入
     *
     * @param v
     * @param scale
     * @return
     */
    public static String round(String v, int scale) {
        return div(v, "1", scale);
    }

}
