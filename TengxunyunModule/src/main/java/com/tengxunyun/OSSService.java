package com.tengxunyun;

import android.content.Context;
import android.util.Log;

import com.tencent.cos.COSClient;
import com.tencent.cos.COSConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.CreateDirRequest;
import com.tencent.cos.model.CreateDirResult;
import com.tencent.cos.model.DeleteObjectRequest;
import com.tencent.cos.model.DeleteObjectResult;
import com.tencent.cos.model.GetObjectMetadataRequest;
import com.tencent.cos.model.GetObjectMetadataResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.model.UpdateObjectRequest;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.tencent.cos.task.listener.IDownloadTaskListener;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.tengxunyun.config.ParamPreference;
import com.tengxunyun.utils.MyICmdTaskListener;
import com.tengxunyun.utils.MyInfoCmdTaskListener;
import com.tengxunyun.utils.MyCmdListener;
import com.tengxunyun.utils.MyDownLoadListener;
import com.tengxunyun.utils.MyLoadTaskListener;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created:yangjiang on 2016/11/10 17:48
 * E-Mail:1007181167@qq.com
 * Description: 腾讯文件工具类
 */
public final class OSSService {
    private COSClient client;
    private static final String TAG = OSSService.class.getName();
    private boolean isDebug = false;
    private String cosPath;//远程目录
    private String sign;//签名
    private String yuMing;

    /**
     * cos sdk 配置设置; 根据需要设置
     */

    // 创建单例getInstance
    public static OSSService getInstance() {

        return Holder.holder;
    }

    private static class Holder {
        static OSSService holder = new OSSService();
    }

    /***
     * 初始化方式
     *
     * @param context 上下
     * @param yuMing  访问域名
     ***/
    public void init(Context context, String yuMing) {
        synchronized (this) {
            getOSSClient(context);
        }
        this.yuMing = yuMing;
    }

    public String getSign() {
        return sign;
    }

    /***
     * @param sign 签名 d 服务器签名设置
     **/
    public void setSign(String sign) {
        this.sign = sign;
    }

    /****
     * d获取
     ***/
    private COSClient getOSSClient(Context context) {
        if (client == null) {
            //持久化 ID，每个 COSClient 需设置一个唯一的 ID 用于持久化保存未完成任务 列表
            // ，以便应用退出重进后能够继续进行上传；传入为 Null，则不会进行持久化保存
            //创建COSClientConfig对象，根据需要修改默认的配置参数
            COSConfig config = new COSConfig();
            config.setEndPoint(COSEndPoint.COS_TJ);
            client = new COSClient(context, ParamPreference.TENCENT_COS_APPID, config, ParamPreference.persistenceId);
        }
        return client;
    }

    public COSClient getOSSClient() {
        return client;
    }


    /**
     * 获取Bucket名 称
     ***/
    public String getBucket() {
        return ParamPreference.TENCENT_COS_BUCKET;
    }

    /***
     * 创建目录
     ***/

    public void creareDir() {
        CreateDirRequest createDirRequest = new CreateDirRequest();
        createDirRequest.setBucket(getBucket());
        createDirRequest.setCosPath(cosPath);
        Log.d(TAG, getSign());
        createDirRequest.setSign(getSign());
        createDirRequest.setListener(new ICmdTaskListener() {
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                final CreateDirResult createDirResult = (CreateDirResult) cosResult;
                Log.w(TAG, "目录创建成功： ret=" + createDirResult.code + "; msg=" + createDirResult.msg
                        + "ctime = " + createDirResult.ctime);
            }

            @Override
            public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                Log.w(TAG, "目录创建失败： ret=" + cosResult.code + "; msg=" + cosResult.msg);
            }
        });
        getOSSClient().createDirAsyn(createDirRequest);
    }


    /***
     * 修改目录
     *
     * @param biz_attr     目录绑定的属性信息
     * @param infoListener 修改回调
     ***/
    public void updateDir(String biz_attr, final MyICmdTaskListener infoListener) {
        UpdateObjectRequest updateObjectRequest = new UpdateObjectRequest();
        updateObjectRequest.setBucket(getBucket());
        updateObjectRequest.setCosPath(cosPath);
        updateObjectRequest.setBizAttr(biz_attr);
        updateObjectRequest.setSign(getSign());
        updateObjectRequest.setListener(new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                if (infoListener != null) {
                    infoListener.onSuccess(cosResult.code, cosResult.msg);
                }
                //更新成功
                if (isDebug) {
                    Log.w(TAG, cosResult.code + " : " + cosResult.msg);
                }
            }

            @Override
            public void onFailed(COSRequest COSRequest, COSResult cosResult) {
                //更新失败
                if (infoListener != null) {
                    infoListener.onFailed(cosResult.code, cosResult.msg);
                }
                if (isDebug) {
                    Log.w(TAG, cosResult.code + " : " + cosResult.msg);
                }
            }
        });
        getOSSClient().updateObjectAsyn(updateObjectRequest);
    }

    /***
     * 查询目录的信息
     *
     * @param listener 回调
     **/
    public void GetDri(final MyInfoCmdTaskListener listener) {
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest();
        getObjectMetadataRequest.setBucket(getBucket());
        getObjectMetadataRequest.setCosPath(cosPath);
        getObjectMetadataRequest.setSign(getSign());
        getObjectMetadataRequest.setListener(new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                GetObjectMetadataResult result = (GetObjectMetadataResult) cosResult;
                if (listener != null) {
                    listener.onSuccess(result.biz_attr == null ? "" : result.biz_attr, result.ctime, result.mtime);
                }
                if (isDebug) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("code=" + result.code + "; msg=" + result.msg + "\n");
                    stringBuilder.append("ctime =" + result.ctime + "; mtime=" + result.mtime + "\n");
                    stringBuilder.append("biz_attr=" + result.biz_attr == null ? "" : result.biz_attr);
                    Log.w(TAG, stringBuilder.toString());
                }
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                if (listener != null) {
                    listener.onFailed(cosResult.code, cosResult.msg);
                }
                if (isDebug) {
                    Log.w(TAG, cosResult.code + " : " + cosResult.msg);
                }

            }

        });
        getOSSClient().getObjectMetadataAsyn(getObjectMetadataRequest);

    }

    /***
     * 上传文件 同步
     *
     * @param path    文件本地路径
     * @param fileKey 自己定义在oss的文件fileKey
     * @return boolean true 上传成功
     **/
    public PutObjectResult uploadFile(String path, String fileKey) {
        // 构造上传请求
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(getBucket());
        putObjectRequest.setCosPath(cosPath + "/" + fileKey);
        putObjectRequest.setSrcPath(path);
        putObjectRequest.setSign(getSign());
        return getOSSClient().putObject(putObjectRequest);
    }

    /***
     * 上传文件  异步
     *
     * @param path                文件本地路径
     * @param fileKey             自己定义在oss的文件fileKey
     * @param sliceFlag           设置是否分片上传：true，分片上传;false,简单文件上传
     * @param iUploadTaskListener 上传进度回调
     * @return OSSAsyncTask  异步任务
     **/
    public void uploadFileAsyn(String path, String fileKey, boolean sliceFlag, final MyLoadTaskListener iUploadTaskListener) {
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(getBucket());
        putObjectRequest.setCosPath(cosPath + '/' + fileKey);
        putObjectRequest.setSrcPath(path);
        putObjectRequest.setSign(getSign());
        //设置是否允许覆盖同名文件： "0"，允许覆盖；"1",不允许覆盖；
        putObjectRequest.setInsertOnly("1");
        //设置是否开启分片上传
        putObjectRequest.setSliceFlag(sliceFlag);//设置是否分片上传：true，分片上传;false,简单文件上传
        putObjectRequest.setListener(new IUploadTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                PutObjectResult result = (PutObjectResult) cosResult;
                if (iUploadTaskListener != null) {
                    iUploadTaskListener.onSuccess(result.access_url, result.url);
                }
                if (isDebug) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(" 上传结果： ret=" + result.code + "; msg =" + result.msg + "\n");
                    stringBuilder.append(" access_url= " + result.access_url + "\n");
                    stringBuilder.append(" resource_path= " + result.resource_path + "\n");
                    stringBuilder.append(" url= " + result.url);
                    Log.w(TAG, "uploadFileAsyn:" + stringBuilder.toString());
                }
            }

            @Override
            public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                if (isDebug) {
                    Log.w(TAG, "uploadFileAsyn:上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg);
                }
                if (iUploadTaskListener != null) {
                    iUploadTaskListener.onFailed(cosResult.code, cosResult.msg);
                }

            }

            @Override
            public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
                float progress = (float) currentSize / totalSize;
                progress = progress * 100;
                if (isDebug) {
                    Log.w(TAG, "uploadFileAsyn:进度：  " + (int) progress + "%");
                }
                if (iUploadTaskListener != null) {
                    iUploadTaskListener.onProgress(progress);
                }
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                if (isDebug) {
                    Log.w(TAG, ":uploadFileAsyn:取消： ret =" + cosResult.code + "; msg =" + cosResult.msg);
                }
                if (iUploadTaskListener != null) {
                    iUploadTaskListener.onCancel(cosResult.code, cosResult.msg);
                }

            }
        });
        getOSSClient().putObjectAsyn(putObjectRequest);
    }

    /**
     * 获取文件信息
     ***/
    public GetObjectMetadataResult getObjectInfo(String fileKey) {
        // 创建同步获取文件元信息请求
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest();
        getObjectMetadataRequest.setBucket(getBucket());
        getObjectMetadataRequest.setCosPath(cosPath + "/" + fileKey);
        getObjectMetadataRequest.setSign(getSign());
        return getOSSClient().getObjectMetadata(getObjectMetadataRequest);
    }

    /***
     * 获取文件信息
     *
     * @param fileKey          文件key
     * @param iCmdTaskListener 回调
     **/
    public void getObjectInfoAsyn(String fileKey, final MyCmdListener iCmdTaskListener) {
        // 创建同步获取文件元信息请求
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest();
        getObjectMetadataRequest.setBucket(getBucket());
        getObjectMetadataRequest.setCosPath(cosPath + "/" + fileKey);
        getObjectMetadataRequest.setSign(getSign());
        getObjectMetadataRequest.setListener(new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                GetObjectMetadataResult result = (GetObjectMetadataResult) cosResult;
                if (iCmdTaskListener != null) {
                    iCmdTaskListener.onSuccess((result.biz_attr == null ? "" : result.biz_attr), result.ctime, result.mtime, result.sha, result.custom_headers, result.filelen);
                }
                if (isDebug) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("code=" + result.code + "; msg=" + result.msg + "\n");
                    stringBuilder.append("ctime =" + result.ctime + "; mtime=" + result.mtime + "\n");
                    stringBuilder.append("biz_attr=" + result.biz_attr == null ? "" : result.biz_attr);
                    stringBuilder.append("sha=" + result.sha);
                    Log.w(TAG, stringBuilder.toString());
                }

            }

            @Override
            public void onFailed(COSRequest cosRequest, final COSResult cosResult) {
                if (iCmdTaskListener != null) {
                    iCmdTaskListener.onFailed(cosResult.code, cosResult.msg);
                }
                if (isDebug) {
                    Log.w(TAG, cosResult.code + " : " + cosResult.msg);
                }
            }

        });
        getOSSClient().getObjectMetadataAsyn(getObjectMetadataRequest);
    }

    /****
     * 刪除文件
     *
     * @param key 刪除文件key
     * @return boolean
     ***/
    public boolean deleteFile(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest();
            deleteObjectRequest.setBucket(getBucket());
            deleteObjectRequest.setCosPath(key);
            deleteObjectRequest.setSign(getSign());
            DeleteObjectResult result = getOSSClient().deleteObject(deleteObjectRequest);
            return result.code == 200;
        } catch (Exception e) {
            return false;
        }

    }

    /****
     * 刪除文件  异步
     *
     * @param key              刪除文件key
     * @param iCmdTaskListener 删除文件回调
     ***/
    public void deleteFileAsync(String key, final MyICmdTaskListener iCmdTaskListener) {
        // 创建删除请求
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest();
        deleteObjectRequest.setBucket(getBucket());
        deleteObjectRequest.setCosPath(key);
        deleteObjectRequest.setSign(getSign());
        deleteObjectRequest.setListener(new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                if (isDebug) {
                    Log.w(TAG, cosResult.code + " : " + cosResult.msg);
                }
                if (iCmdTaskListener != null) {
                    iCmdTaskListener.onSuccess(cosResult.code, cosResult.msg);
                }
            }

            @Override
            public void onFailed(COSRequest COSRequest, COSResult cosResult) {
                if (isDebug) {
                    Log.w(TAG, cosResult.code + " : " + cosResult.msg);
                }
                if (iCmdTaskListener != null) {
                    iCmdTaskListener.onFailed(cosResult.code, cosResult.msg);
                }
            }
        });
        getOSSClient().deleteObjectAsyn(deleteObjectRequest);

    }

    /*****
     * 下载文件  异步
     *
     * @param downloadURl 下载路径
     * @param savePath    本地绝对保存文件路径
     *****/
    public void fileDownAsync(String downloadURl, String savePath, final MyDownLoadListener iDownloadTaskListener) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(downloadURl, savePath);
        getObjectRequest.setSign(getSign());
        getObjectRequest.setListener(new IDownloadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
                float progress = currentSize / (float) totalSize;
                progress = progress * 100;
                if (isDebug) {
                    Log.w(TAG, "progress =" + (int) (progress) + "%");
                }
                if (iDownloadTaskListener != null) {
                    iDownloadTaskListener.onProgress(progress);
                }
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                if (iDownloadTaskListener != null) {
                    iDownloadTaskListener.onCancel(cosResult.code, cosResult.msg);
                }
            }

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                if (iDownloadTaskListener != null) {
                    iDownloadTaskListener.onSuccess(cosResult.code, cosResult.msg);
                }
                if (isDebug) {
                    Log.w(TAG, "code =" + cosResult.code + "; msg =" + cosResult.msg);
                }
            }

            @Override
            public void onFailed(COSRequest COSRequest, COSResult cosResult) {
                if (iDownloadTaskListener != null) {
                    iDownloadTaskListener.onFailed(cosResult.code, cosResult.msg);
                }
                if (isDebug) {
                    Log.w(TAG, "code =" + cosResult.code + "; msg =" + cosResult.msg);
                }
            }
        });
        getOSSClient().getObjectAsyn(getObjectRequest);

    }


    /****
     * @param fileKey 文件key
     * @return String  文件路徑
     ***/
    public String getOSSFile(String fileKey) {
        if (fileKey != null && (isHttp(fileKey)
                || fileKey.contains("file://")
                || fileKey.contains("content://")
                || fileKey.contains("drawable://"))
                || new File(fileKey).exists()
                ) {
            return fileKey;
        } else {
            return getOssObjecttUrl() + fileKey;
        }
    }


    /***
     * 域名管理
     **/
    private String getOssObjecttUrl() {
        return "yangjiang-1251471829.costj.myqcloud.com";
    }

    /**
     * 判断url是否为网址
     *
     * @param url
     * @return URL 链接
     */
    private static boolean isHttp(String url) {
        if (null == url) return false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}
