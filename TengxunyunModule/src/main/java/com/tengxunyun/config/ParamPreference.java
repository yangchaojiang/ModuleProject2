package com.tengxunyun.config;

/**
 * Created by yangc on 2017/5/6.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */
public interface ParamPreference {
    String TENCENT_COS_APPID = "1251471829"; //为常量字符串，即你的appid/腾讯云注册的appidd
    String TENCENT_COS_BUCKET = "yangjiang";//为常量字符串，即你的bucket 名称
    String TENCENT_COS_SECRET_ID = "AKIDumXZVFgLkAQWQ7O8f084oQq46UilgHlF";// 为常量字符串，即你的 secretID
    String  TENCENT_COS_SECRET_KEY="TlqjQuCarAKOENPFtLadTCl8NQHNJWdZ";//SecretKey 为你的secretKey

    String persistenceId="persistenceId";//，以便应用退出重进后能够继续进行上传；传入为 Null，则不会进行持久化保存
}
