package com.seven.cattle;

import android.content.Context;
import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.seven.cattle.in.UploadListener;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by yangc on 2017/8/8.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class OssServer {


    public static OssServer getInstance() {
        return Holder.holder;
    }
    private OssServer() {

    }
    private static class Holder {
        static OssServer holder = new OssServer();
    }
    private UploadManager uploadManager;
    private static volatile String key;
    private static volatile String token;
    private static volatile String bucket;
    private static volatile String domainName;
    // 初始化、执行上传
    private volatile boolean isCancelled = false;
    /**
     * 初始化key
     *
     * @param keys        保存在服务器上的资源唯一标识
     * @param tokens      服务器分配的 token
     * @param buckets     空间名称
     * @param domainNames
     ***/
    public static void intKey(String keys, String tokens, String buckets, String domainNames) {
        key = keys;
        token = tokens;
        bucket = buckets;
        domainName = domainNames;

    }

    /**
     * 初始化操作
     ***/
    public void init(Context application) {
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                // .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
                // .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        //一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
    }

    /***
     * 上传文件
     *
     * @param path 文件路径
     **/
    public void upLoadFile(String path, final UploadListener listener) {
        upLoadFile(new File(path), listener);
    }

    /***
     * 上传文件
     *
     * @param path 文件
     **/
    public void upLoadFile(File path, final UploadListener listener) {
        uploadManager.put(path, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {

                if (listener != null) {
                    if (info.isOK()) {
                        listener.onComplete(key);
                        Log.i("qiniu", "Upload Success");
                    } else {
                        listener.onUploadFail(info.error);
                        Log.i("qiniu", "Upload Fail");
                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    }
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                if (listener != null) {
                    listener.onProgress(key, percent);
                }
            }
        }, new UpCancellationSignal() {
            public boolean isCancelled() {
                return isCancelled;
            }
        }));

    }

    // 点击取消按钮，让UpCancellationSignal##isCancelled()方法返回true，以停止上传
    public void cancell() {
        isCancelled = true;
    }


    /**
     * 得到文件uri
     **/
    private String getFileUri(String key) {
        return "http://of830rd74.bkt.clouddn.com/" + key;
    }


}
