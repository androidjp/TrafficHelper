package com.androidjp.traffichelper.home;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.amap.api.location.AMapLocation;
import com.androidjp.traffichelper.data.model.location.AMapLocationManager;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.SoftReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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
        ///TODO: 根据初步的理赔计算公式 ，开始计算

        ///TODO： 使用RxJava
        Flowable.create(new FlowableOnSubscribe<Record>() {
            @Override
            public void subscribe(FlowableEmitter<Record> e) throws Exception {
                e.onNext(record);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .map(new Function<Record, RecordRes>() {
                    @Override
                    public RecordRes apply(Record record) throws Exception {
                        Thread.sleep(2000);
                        RecordRes recordRes = new RecordRes();
                        int dead = 11;
                        int fen_mu = 10;
                        recordRes.money_hurt = (float) (34757 * ((record.hurt_level * 1.0) / fen_mu % 1) * 20);
                        recordRes.money_bury = (record.hurt_level == 11) ? (float) (2472.71 / 12 * 6) : 0;
                        recordRes.money_heart = (float) (5000 * 6.00);
                        recordRes.money_nursing = record.nursing_days * 60;
                        recordRes.money_tardy = record.tardy_days * record.salary;
                        recordRes.money_medical = record.medical_free;
                        recordRes.money_nutrition = record.nutrition_days * 50;
                        recordRes.money_hospital_allowance = record.hospital_days * 100;
                        recordRes.calculateAllPay();
                        return recordRes;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecordRes>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Logger.i(MainPresenter.class.getSimpleName(), "onSubscribe()");
                        s.request(1);
                    }

                    @Override
                    public void onNext(RecordRes recordRes) {
                        Logger.i(MainPresenter.class.getSimpleName(), "onNext()");
                        if (record != null && mView != null)
                            mView.get().showRecordResult(recordRes);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Logger.i(MainPresenter.class.getSimpleName(), "onComplete()");
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
