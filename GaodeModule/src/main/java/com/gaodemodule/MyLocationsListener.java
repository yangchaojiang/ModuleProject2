package com.gaodemodule;


/**
 * Created by yangc on 2017/9/16.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public interface MyLocationsListener {
    /***
     * 成功
     *
     * @param msg        内容
     * @param dateString 定位时间
     ***/
    void onSucceed(String msg, String dateString);

    /***
     * @param code 错误码
     * @param msg  内容
     **/
    void onFailed(int code, String msg);
}
