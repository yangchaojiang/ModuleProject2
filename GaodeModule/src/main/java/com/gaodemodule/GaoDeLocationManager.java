package com.gaodemodule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangc on 2017/9/17.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 高德定位管理类
 */

public class GaoDeLocationManager {


    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private MyLocationsListener myLocationsListener = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    public static GaoDeLocationManager getSingleton() {
        return Holder.holder;
    }
    private static class Holder {
        static GaoDeLocationManager holder = new GaoDeLocationManager();
    }
    private GaoDeLocationManager(){

    }

    /***
     * 初始化
     *
     * @param context 上下文
     ***/
    public void init(@NonNull Context context) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(getDefaultLocationClientOption());
    }

    /***
     * 默认配置
     ***/
    public AMapLocationClientOption getDefaultLocationClientOption() {
        //初始化AMapLocationClientOption对象
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setOnceLocation(false);  //获取一次定位结果：该方法默认为false。
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            mLocationOption.setInterval(2000);//  //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setNeedAddress(true); //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setMockEnable(true); //设置是否允许模拟位置,默认为true，允许模拟位置
            mLocationOption.setHttpTimeOut(20000);  //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setLocationCacheEnable(false);//关闭缓存机制
        }
        return mLocationOption;
    }

    /***
     * 启动定位
     *
     * @param locationsListener 回调结果
     **/
    public void start(@NonNull MyLocationsListener locationsListener) {
        if (mLocationClient != null) {
            this.myLocationsListener = locationsListener;
            mLocationListener = new MyAMapLocationListener();
            mLocationClient.setLocationListener(mLocationListener);
            mLocationClient.startLocation(); //启动定位
        }
    }

    /***
     * 停止定位
     */
    public boolean stop() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationListener = null;
            if (mLocationClient.isStarted()) {
                mLocationClient.stopLocation();//停止定位
            }
            myLocationsListener = null;
        }
        return true;
    }

    /***
     * 如果
     * 销毁服务 调用此方法后，一切示例都被被设置null
     * 再次使用必须调用 init();反发
     **/
    public void onDestroy() {
        if (mLocationClient != null) {
            stop();
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            mLocationClient = null;
            mLocationOption = null;
            Holder.holder = null;
        }
    }

    /**
     * 返回最后一位置信息
     */
    public String getLastKnownLocation() {
        if (mLocationClient != null)
            return Utils.getLocationStr(mLocationClient.getLastKnownLocation());
        return null;
    }

    /***
     * 处理回调
     **/
    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null && myLocationsListener != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    myLocationsListener.onSucceed(amapLocation.getAddress(), df.format(date));
                    //可在其中解析amapLocation获取相应内容。
                 /* amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();//国家信息
                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getStreet();//街道信息
                    amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    amapLocation.getFloor();//获取当前室内定位的楼层
                    amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                   */
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    myLocationsListener.onFailed(amapLocation.getErrorCode(), amapLocation.getErrorInfo());
                }
            }
        }
    }
}
