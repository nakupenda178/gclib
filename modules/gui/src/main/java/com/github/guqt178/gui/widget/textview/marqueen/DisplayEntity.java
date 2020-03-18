package com.github.guqt178.gui.widget.textview.marqueen;

import android.text.TextUtils;

import java.util.Date;

/**
 * 需要滚动展示消息的实体
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:04
 */
public class DisplayEntity {
    /**
     * ID
     */
    private String mID;
    /**
     * 展示的消息内容
     */
    private final String mMessage;

    /**
     * 消息时间
     */
    private long mTime;

    /**
     * 有效时间
     */
    private int mEffectiveInternal;

    public DisplayEntity(String message) {
        mMessage = message;
    }

    public DisplayEntity(String ID, String message, long time, int effectiveInternal) {
        mID = ID;
        mMessage = message;
        mTime = time;
        mEffectiveInternal = effectiveInternal;
    }

    public String getID() {
        return mID;
    }

    public DisplayEntity setID(String ID) {
        mID = ID;
        return this;
    }

    public String getMessage() {
        return mMessage;
    }

    public long getTime() {
        return mTime;
    }

    public DisplayEntity setTime(long time) {
        mTime = time;
        return this;
    }

    public int getEffectiveInternal() {
        return mEffectiveInternal;
    }

    public DisplayEntity setEffectiveInternal(int effectiveInternal) {
        mEffectiveInternal = effectiveInternal;
        return this;
    }

    @Override
    public String toString() {
        return updateMessage();
    }

    /**
     * 更新消息
     */
    private String updateMessage() {
        if (isValid() && mTime != 0 && mMessage.contains("$") ) {
            //$是时间的占位符，需要动态替换掉
            String internal = String.valueOf(calculateNumberofMinutes(mTime));
            return mMessage.replace("$", internal);
        } else {
            return mMessage;
        }
    }

    /**
     * 计算时间距离
     * @param date 时间
     * @return
     */
    public int calculateNumberofMinutes(long date) {
        int result = (int) ((getCurrentDate().getTime() - date) / (1000 * 60));
        return result;
    }

    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 是否有效
     * @return
     */
    public boolean isValid() {
        return !TextUtils.isEmpty(mMessage);
    }
}
