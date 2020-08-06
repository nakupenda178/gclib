package com.github.guqt178.utils.keyboard;


import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述   ：金额输入过滤器，限制小数点后输入位数
 * <p>
 * 默认限制小数点2位
 * 默认第一位输入小数点时，转换为0.
 * 如果起始位置为0,且第二位跟的不是".",则无法后续输入
 * 用法:
 * mEditText.setFilters(new InputFilter[]{new MoneyValueFilter()});
 * //手动设置其他位数，例如3
 * mEditText.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(3)});
 * <p>
 */

public class MoneyValueFilter extends DigitsKeyListener {
    /**
     * 最大数字
     */
    private long MAX_VALUE = 500;

    /**
     * 小数点后的数字的位数,如果为0说明不允许小数点输入
     */
    private int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    Pattern p;

    public MoneyValueFilter() {
        //用于匹配输入的是0-9  .  这几个数字和字符
        p = Pattern.compile("([0-9]|\\.)*");
    }

    /**
     * @param precision 小数点后精度
     */
    public void setPrecision(int precision) {
        this.POINTER_LENGTH = precision;
    }

    /**
     * @param maxValue 整数的最大值
     */
    public void setMaxValue(long maxValue) {
        this.MAX_VALUE = maxValue;
    }

    /**
     * source    新输入的字符串
     * start    新输入的字符串起始下标，一般为0
     * end    新输入的字符串终点下标，一般为source长度-1
     * dest    输入之前文本框内容
     * dstart    原内容起始坐标，一般为0
     * dend    原内容终点坐标，一般为dest长度-1
     */

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String sourceText = source.toString();
        String destText = dest.toString();

        if (POINTER.equals(sourceText) && POINTER_LENGTH == 0) {//只允许整数时,不能输入.
            return "";
        }
        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            if (dstart == 0 && destText.indexOf(POINTER) == 1) {//保证小数点不在第一个位置
                return "0";
            }
            return "";
        }
        Matcher matcher = p.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(source)) { //只能输入一个小数点
                    return "";
                }
            }
            //验证小数点精度，保证小数点后只能输入两位
            int index = destText.indexOf(POINTER);
            int length = destText.trim().length() - index;
            if (length > POINTER_LENGTH && dstart > index) {
                return "";
            }
        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(source)) && dstart == 0) {//第一个位置输入小数点的情况
                    return "0.";
                } /*else if ("0".equals(source) && dstart == 0) {
                    return "";
                }*/
            }
        }
        //dstart
        //修复当光标定位到第一位的时候 还能输入其他的    这个是为了修复以下的情况
        /**
         * <>
         *     当如下情况的时候  也就是 已经输入了23.45   这个时候限制是500元
         *     那么这个时候如果把光标移动2前面  也就是第0位  在输入一个5  那么这个实际的参与下面的
         *     判断的sumText > MAX_VALUE  是23.455  这个是不大于 500的   但是实际情况是523  这个时候
         *     已经大于500了  所以之前的是存在bug的   这个要进行修正 也就是拿到的比较数应该是523.45  而不是23.455
         *     所以有了下面的分隔  也就是  把23.45  (因为这个时候dstart=0)  分隔成 ""  和23.45  然后把  5放到中间
         *     进行拼接 也就是  "" + 5 + 23.45  也就是523.45  然后在进行和500比较
         *     还有一个比较明显的就是   23.45   这个时候光标在2和3 之间  那么如果修正之前  是23.455   修正之后  dstart = 1
         *     这个时候分隔是 "2"  "3.45"   这个时候拼接是253.45  然后和500比较  以此类推
         * </>
         */
        String first = destText.substring(0, dstart);

        String second = destText.substring(dstart, destText.length());
        //dend
        String sum = first + sourceText + second;
        //验证输入金额的大小
        double sumText = Double.parseDouble(sum);
        //这里得到输入完之后需要计算的金额  如果这个金额大于了事先设定的金额,那么久直接返回  不需要加入输入的字符
        if (sumText > MAX_VALUE) {
            //Toast.makeText(MyApp.getContext(), MyApp.getContext().getResources().getString(R.string.appreciate_input), Toast.LENGTH_SHORT).show();
            return dest.subSequence(dstart, dend);
        }
        //如果输入的金额小于事先规定的金额
        return dest.subSequence(dstart, dend) + sourceText;
    }

}