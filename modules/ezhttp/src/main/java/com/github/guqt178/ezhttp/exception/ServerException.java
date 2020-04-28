package com.github.guqt178.ezhttp.exception;

/**
 * <p>描述：处理服务器异常</p>
 * 作者： zhouyou<br>
 * 日期： 2016/9/15 16:51 <br>
 * 版本： v1.0<br>
 */
public class ServerException extends RuntimeException {
    private int errCode;
    private String message;

    public ServerException(int errCode, String msg) {
        super(msg);
        this.errCode = errCode;
        this.message = msg;
    }

    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}