package com.androidjp.traffichelper.data.model.location;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.androidjp.traffichelper.data.pojo.Location;
import com.orhanobut.logger.Logger;

/**
 * 高德定位管理类（对应jar包版本：AMap_Location_V3.2.1_20161228.jar）
 * Created by androidjp on 2017/1/8.
 */
public class AMapLocationManager implements IAMapLocationManager{

    public static final int HIGH_ACCURACY = 0x11;///高精度模式
    public static final int ONLY_WIFI = 0x12;///低功耗模式（只wifi）
    public static final int ONLY_GPS = 0x13;///只GPS（不支持室内环境定位）


    private static AMapLocationManager sIntanse;
    private Context mContext;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationListener mLocationListener = null;
    private AMapLocationClientOption mOption = null;
    private AMapLocationCallback mCallback =null;

    private Location mLocation = null;

    /**
     * 定位结果列表
     */
    private AMapLocationManager(Context context){
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                ///操作结果
                if (aMapLocation!=null){
                    StringBuilder sb =new StringBuilder();
                    sb
                            .append(aMapLocation.getCity()).append(aMapLocation.getDistrict())
                            .append(aMapLocation.getStreet()).append(aMapLocation.getStreetNum());
                    if (TextUtils.isEmpty(sb.toString())){
                        Logger.i("定位得到结果：aMapLocation不为null，但内容为null！");
                        AMapLocation location =  mLocationClient.getLastKnownLocation();
                        if (mLocation==null)
                            mLocation = new Location(location.getCity(),location.getProvince(),location.getStreet()+location.getStreetNum(),location.getLatitude(),location.getLongitude());
                        else{
                            mLocation.city = location.getCity();
                            mLocation.province = location.getProvince();
                            mLocation.street = location.getStreet()+location.getStreetNum();
                            mLocation.latitude = location.getLatitude();
                            mLocation.longitude = location.getLongitude();
                        }
                        if (mCallback !=null){
                            mCallback.finishLocation(mLocation.toString());
//                    mCallback.finishLocation(location.getAddress());
                        }
                    }else{
                        Logger.i("定位得到结果：aMapLocation不为null，内容为"+sb.toString());

                        if (mLocation==null)
                            mLocation = new Location(aMapLocation.getCity(),aMapLocation.getProvince(),aMapLocation.getStreet()+aMapLocation.getStreetNum(),aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        else{
                            mLocation.city = aMapLocation.getCity();
                            mLocation.province = aMapLocation.getProvince();
                            mLocation.street = aMapLocation.getStreet()+aMapLocation.getStreetNum();
                            mLocation.latitude = aMapLocation.getLatitude();
                            mLocation.longitude = aMapLocation.getLongitude();
                        }
                        if (mCallback !=null){
                            mCallback.finishLocation(sb.toString());
//                    mCallback.finishLocation(aMapLocation.getAddress());
                        }
                    }

                }else{
                    AMapLocation location =  mLocationClient.getLastKnownLocation();
                    if (mLocation==null)
                        mLocation = new Location(location.getCity(),location.getProvince(),location.getStreet(),location.getLatitude(),location.getLongitude());
                    else{
                        mLocation.city = location.getCity();
                        mLocation.province = location.getProvince();
                        mLocation.street = location.getStreet();
                        mLocation.latitude = location.getLatitude();
                        mLocation.longitude = location.getLongitude();
                    }
                    if (mCallback !=null){
                        Logger.i("定位得到结果：aMapLocation为null,显示最近一次的定位结果："+mLocation.toString());
                        mCallback.finishLocation(mLocation.toString());
//                    mCallback.finishLocation(location.getAddress());
                    }
                }

            }
        };
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(this.mLocationListener);
        mOption = new AMapLocationClientOption();
        //基本配置
        this.mOption.setNeedAddress(true);//默认true
//        this.mOption.setWifiScan(false);///默认为true，强制刷新wifi
        this.mOption.setMockEnable(false);///默认为false，不允许模拟GPS定位结果
        this.mOption.setHttpTimeOut(20000);///默认请求超时未30s
        this.mOption.setLocationCacheEnable(true);///默认为false，不缓存定位结果（高精度和wifi时会缓存）
    }

    public static AMapLocationManager getInstance(Context context){
        if (sIntanse == null){
            synchronized (AMapLocationManager.class){
                if (sIntanse == null)
                    sIntanse =  new AMapLocationManager(context);
            }
        }
        return sIntanse;
    }



    @Override
    public void startLocation() {
        this.mLocationClient.setLocationOption(this.mOption);
        this.mLocationClient.startLocation();
    }

    @Override
    public void stopLocation() {
        this.mLocationClient.stopLocation();
    }

    @Override
    public void release() {
        this.mCallback = null;
        this.mLocationListener = null;
        this.mOption = null;
        this.mLocationClient.onDestroy();
        sIntanse = null;
    }

    @Override
    public Location getLocation() {
        return this.mLocation;
    }

    @Override
    public IAMapLocationManager setModel(int model) {
        ///配置参数
        ///高精度模式
        switch (model){
            case HIGH_ACCURACY:
                this.mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                break;
            case ONLY_WIFI:
                this.mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
                break;
            case ONLY_GPS:
                this.mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
                break;
        }

        return this;
    }

    @Override
    public IAMapLocationManager setOnceLocation(boolean onceLocation) {
        if (onceLocation)///单次定位
            this.mOption.setOnceLocationLatest(true);///获取最近3s最高精度的一次定位数据
        else
            this.mOption.setOnceLocation(false);
        return this;
    }

    @Override
    public IAMapLocationManager setLocationPeriodTime(long time) {
        this.mOption.setOnceLocation(false).setInterval(time);///默认是2s
        return null;
    }

    public IAMapLocationManager setLocationCallback(AMapLocationCallback callback){
        this.mCallback = callback;
        return this;
    }


    public interface AMapLocationCallback{
         void finishLocation(String locationMsg);
         void error(String msg);
    }
}
