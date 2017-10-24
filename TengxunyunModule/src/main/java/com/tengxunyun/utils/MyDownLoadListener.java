package com.tengxunyun.utils;

/**
 * Created by yangc on 2017/8/9.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:  下载回调
 */

public interface MyDownLoadListener     {
    /***
     * 成功
     *
     */
    void onSuccess(int code, String msg);

    /**
     * 失败
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
