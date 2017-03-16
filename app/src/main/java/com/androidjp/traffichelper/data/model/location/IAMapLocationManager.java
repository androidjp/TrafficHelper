package com.androidjp.traffichelper.data.model.location;

/**
 * Created by androidjp on 2017/1/8.
 */

public interface IAMapLocationManager {

    ///开始定位
    void startLocation();
    //停止定位
    void stopLocation();
    //释放资源
    void release();

    /**
     * @param model wifi 或 gps
     * @return this
     */
    IAMapLocationManager setModel(int model);

    /**
     * @param onceLocation 是否连续定位
     * @return this
     */
    IAMapLocationManager setOnceLocation(boolean onceLocation);

    /**
     * @param time 连续定位周期间隔（ms）
     * @return this
     */
    IAMapLocationManager setLocationPeriodTime(long time);
}
