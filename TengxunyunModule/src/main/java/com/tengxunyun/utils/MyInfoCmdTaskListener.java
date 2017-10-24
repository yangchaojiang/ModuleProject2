package com.tengxunyun.utils;

/**
 * Created by yangc on 2017/8/9.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public interface MyInfoCmdTaskListener     {


    /***
     * 成功
     *
     * @param biz_attr 属性
     * @param ctime    创建时间
     * @param mtime    long(Unix时间戳)最后一次修改时间
     **/
    void onSuccess(String biz_attr, long ctime, long mtime);

    /**
     * 失败
     *
     * @param code 错误码
     * @param msg  错误内容
     **/
    void onFailed(int code, String msg);
}
