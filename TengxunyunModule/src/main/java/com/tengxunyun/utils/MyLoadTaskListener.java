package com.tengxunyun.utils;

/**
 * Created by yangc on 2017/8/9.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 上专回调
 */

public interface MyLoadTaskListener {


    /***
     * 成功
     *
     * @param key 文件key
     */
    void onSuccess(String key, String url);

    /**
     *失败
     *
     * @param code 错误码
     * @param msg  错误内容
     **/
    void onFailed(int code, String msg);

    /**
     * 上传进度
     *
     * @param onProgress 进度  0-100
     **/
    void onProgress(float onProgress);

    /**
     * 取消
     *
     * @param code 错误码
     * @param msg  错误内容
     **/
    void onCancel(int code, String msg);
}
