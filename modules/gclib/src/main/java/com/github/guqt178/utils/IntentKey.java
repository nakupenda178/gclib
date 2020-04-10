package com.github.guqt178.utils;

/**
 *    Intent Key 管理
 */
public final class IntentKey {

    public static final String PREFIX = "intent_key_";

    //通用
    public static final String INTENT_KEY_01 = PREFIX + "intent_key_one";
    public static final String INTENT_KEY_02 = PREFIX + "intent_key_two";
    public static final String INTENT_KEY_03 = PREFIX + "intent_key_three";
    public static final String INTENT_KEY_04 = PREFIX + "intent_key_four";



    // 常用相关

    /** id */
    public static final String ID = PREFIX + "id";
    /** token */
    public static final String TOKEN = PREFIX + "token";
    /** 索引 */
    public static final String INDEX = PREFIX + "index";
    /** 位置 */
    public static final String POSITION = PREFIX + "position";
    /** 状态 */
    public static final String STATUS = PREFIX + "status";
    /** 类型 */
    public static final String TYPE = PREFIX + "type";
    /** 订单 */
    public static final String ORDER = PREFIX + "order";
    /** 余额 */
    public static final String BALANCE = PREFIX + "balance";
    /** 时间 */
    public static final String TIME = PREFIX + "time";
    /** 代码 */
    public static final String CODE = PREFIX + "code";
    /** URL */
    public static final String URL = PREFIX + "url";
    /** 路径 */
    public static final String PATH = PREFIX + "path";
    /** 数量 */
    public static final String AMOUNT = PREFIX + "amount";
    /** 总数 */
    public static final String COUNT = PREFIX + "count";
    /** 其他 */
    public static final String OTHER = PREFIX + "other";

    // 个人信息

    /** 姓名 */
    public static final String NAME = PREFIX + "name";
    /** 年龄 */
    public static final String AGE = PREFIX + "age";
    /** 性别 */
    public static final String SEX = PREFIX + "sex";
    /** 手机 */
    public static final String PHONE = PREFIX + "phone";
    /** 密码 */
    public static final String PASSWORD = PREFIX + "password";
    /** 会员 */
    public static final String VIP = PREFIX + "vip";
    /** 描述 */
    public static final String DESCRIBE = PREFIX + "describe";
    /** 备注 */
    public static final String REMARK = PREFIX + "remark";
    /** 星座 */
    public static final String CONSTELLATION = PREFIX + "constellation";

    // 地方

    /** 地址 */
    public static final String ADDRESS = PREFIX + "address";
    /** 省 */
    public static final String PROVINCE = PREFIX + "province";
    /** 市 */
    public static final String CITY = PREFIX + "city";
    /** 区 */
    public static final String AREA = PREFIX + "area";

    // 文件类型相关

    /** 文本 */
    public static final String FILE = PREFIX + "file";
    /** 文本 */
    public static final String TXT = PREFIX + "txt";
    /** 图片 */
    public static final String PICTURE = PREFIX + "picture";
    /** 音频 */
    public static final String VOICE = PREFIX + "voice";
    /** 视频 */
    public static final String VIDEO = PREFIX + "video";

    // 支付相关

    /** 余额支付 */
    public static final String BALANCE_PAY = PREFIX + "balance_pay";
    /** 微信支付 */
    public static final String WECHAT_PAY =PREFIX +  "wechat_pay";
    /** 支付宝支付 */
    public static final String ALI_PAY = PREFIX + "ali_pay";
    /** 银联支付 */
    public static final String UNION_PAY = PREFIX + "union_pay";
}
