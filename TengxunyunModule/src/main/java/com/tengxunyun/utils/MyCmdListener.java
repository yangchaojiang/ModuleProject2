package com.tengxunyun.utils;

import java.util.Map;

/**
 * Created by yangc on 2017/8/9.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */


public interface MyCmdListener {


    /****
     * 成功
     *
     * @param biz_attr 目录绑定的属性信息
     * @param ctime 创建时间 (Unix时间戳)
     * @param mtime  最后一次修改时间 (Unix时间戳)
     * @param sha  文件的sha值
     * @param customs_headers 文件的头部属性
     * @param filelen  	文件的长度大小
     */

    void onSuccess(String  biz_attr, long ctime, long mtime, String sha, Map customs_headers, int filelen);


    /**
     * 失败
     *
     * @param code 错误码
     * @param msg  错误内容
     **/
    void onFailed(int code, String msg);

}
