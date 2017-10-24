package com.tengxunyun.utils;

/**
 * Created by yangc on 2017/8/9.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:文件删除回调
 */

public interface MyICmdTaskListener {

    void onSuccess(int code,String msg);

    /**
     *  失败
     *
     * @param code 错误码
     * @param msg  错误内容
     **/
    void onFailed(int code, String msg);
}
