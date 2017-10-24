package com.moduleproject2;

import android.app.Application;

import com.baidulocationmodule.BaiDuLocationManager;
import com.gaodemodule.GaoDeLocationManager;

/**
 * Created by yangc on 2017/9/16.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class App extends Application {
    public static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        BaiDuLocationManager.getSingleton().init(this);
        GaoDeLocationManager.getSingleton().init(this);
    }
}
