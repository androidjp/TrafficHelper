package com.androidjp.traffichelper.home;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.amap.api.location.AMapLocation;
import com.androidjp.lib_common_util.pojo.network.Result;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.location.AMapLocationManager;
import com.androidjp.traffichelper.data.model.retrofit.ServiceGenerator;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.orhanobut.logger.Logger;

import java.lang.ref.SoftReference;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * MainContract.Presenter实现类
 * Created by androidjp on 2016/12/20.
 */

public class MainPresenter implements MainContract.Presenter, AMapLocationManager.AMapLocationCallback {

    private SoftReference<MainContract.View> mView;
    private Context mContext;
    private AMapLocation curLocation;///当前定位

    public MainPresenter(Context context, MainContract.View view) {
        this.mContext = context;
        this.mView = new SoftReference<>(view);
    }


    @Override
    public void prepareCalculate() {
        //TODO: 计算理赔结果
        if (this.mView.get() != null) {
            this.mView.get().prepareCalculateMsg();
        }
    }

    @Override
    public void startCalculate(final Record record) {
        if (record.getLocation() == null)
            record.setLocation(AMapLocationManager.getInstance(mContext).getLocation());

        record.record_time = System.currentTimeMillis();

        ///TODO: 根据初步的理赔计算公式 ，开始计算
        Logger.i("发送给后端计算前的record：" + record.toJsonString());
        //TODO:使用Retrofit2 上传Record并后端计算
        ServiceAPI.RecordAPI recordAPI = ServiceGenerator.createService(ServiceAPI.RecordAPI.class);
        Flowable<Result<RecordRes>> call = recordAPI.addRecord(record);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordResResult -> {
                    if (recordResResult != null && recordResResult.data != null) {
                        RecordRes recordRes = recordResResult.data;
                        Logger.i("上传理赔记录并计算，成功，返回RecordRes , code=" + recordResResult.code + ",message=" + recordResResult.msg + ", data=" + recordRes.toJsonString());

                        if (mView != null)
                            mView.get().showRecordResult(recordRes);
                    }
                }, throwable -> {
                    Logger.e("上传理赔记录并计算 onFailure!! ," + throwable.getMessage());
                    if (mView != null) {
                        mView.get().showRecordResult(null);
                    }
                });
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
                if (mView.get() != null)
                    mView.get().showLocationMsg(locationMsg);
            }
        });
    }

    @Override
    public void error(String msg) {

    }
}
