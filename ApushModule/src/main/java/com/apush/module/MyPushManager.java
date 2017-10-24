package com.apush.module;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.igexin.sdk.PushReceiver;
import com.igexin.sdk.PushService;

/**
 * Created by yangc on 2017/9/17.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class MyPushManager {

    public static MyPushManager getSingleton() {
        return Holder.holder;
    }

    private static class Holder {
        static MyPushManager holder = new MyPushManager();
    }
    private MyPushManager() {
    }
    /***
     * 初始化
     *
     * @param context 上下文
     **/
    public void init(@NonNull Context context) {
        PushManager.getInstance().initialize(context.getApplicationContext(), DemoIntentService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(context.getApplicationContext(),DemoIntentService.class);
        Log.d("MyPushManager",    PushManager.getInstance().getClientid(context)+null);
    }
}
