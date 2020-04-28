package com.github.guqt178.ezhttp.callback;

/**
 * <p>描述：下载进度回调（主线程，可以直接操作UI）</p>
 * 作者： zhouyou<br>
 * 日期： 2017/4/28 16:28 <br>
 * 版本： v1.0<br>
 */
public abstract class DownloadProgressCallBack<T> extends CallBack<T> {
    public DownloadProgressCallBack() {
    }

    @Override
    public void onSuccess(T response) {
        
    }

    public abstract void update(long bytesRead, long contentLength, boolean done);

    public abstract void onComplete(String path);

    @Override
    public void onCompleted() {
        
    }
}
