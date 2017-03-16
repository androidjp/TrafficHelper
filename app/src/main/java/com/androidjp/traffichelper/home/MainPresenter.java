package com.androidjp.traffichelper.home;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.amap.api.location.AMapLocation;
import com.androidjp.traffichelper.data.model.location.AMapLocationManager;
import com.androidjp.traffichelper.data.pojo.Record;

import java.lang.ref.SoftReference;

/**
 * Created by androidjp on 2016/12/20.
 */

public class MainPresenter implements MainContract.Presenter,AMapLocationManager.AMapLocationCallback{

    private SoftReference<MainContract.View> mView;
    private Context mContext;
    private AMapLocation curLocation;///当前定位

    public MainPresenter(Context context,MainContract.View view) {
        this.mContext = context;
        this.mView = new SoftReference<>(view);
    }


    @Override
    public void prepareCalculate() {
        //TODO: 计算理赔结果
        if (this.mView.get()!=null){
            this.mView.get().prepareCalculateMsg();
        }
    }

    @Override
    public void startCalculate(Record record) {

    }

    @Override
    public void getLocation() {
        AMapLocationManager.getInstance(mContext).setLocationCallback(this).setOnceLocation(true).setModel(AMapLocationManager.HIGH_ACCURACY).startLocation();
    }

    @Override
    public void consult() {
    }

    @Override
    public void stopLocation() {
        AMapLocationManager.getInstance(mContext).stopLocation();
    }

    @Override
    public void destroyLocation() {
        AMapLocationManager.getInstance(mContext).release();
    }

    @Override
    public void start() {
        //TODO：获取一次定位信息
        AMapLocationManager.getInstance(mContext).setLocationCallback(this).setOnceLocation(true).setModel(AMapLocationManager.HIGH_ACCURACY).startLocation();
    }

    @Override
    public void finishLocation(final String locationMsg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mView.get()!=null)
                    mView.get().showLocationMsg(locationMsg);
            }
        });
    }

    @Override
    public void error(String msg) {

    }
}
