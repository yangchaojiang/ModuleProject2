package com.baidulocationmodule;


/**
 * Created by yangc on 2017/9/16.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public interface MyLocationsListener {

   // void onSucceed(BDLocation location);

    void onSucceed(String ss);
    /***
     * 错误 成功
     **/
    void onFailed(int type, String msg);
}
