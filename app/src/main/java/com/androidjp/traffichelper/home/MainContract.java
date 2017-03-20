package com.androidjp.traffichelper.home;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;

/**
 * MVP
 * 首页 契约
 * Created by androidjp on 2016/11/20.
 */

public class MainContract {

    public interface View extends BaseView<Presenter>{
        void prepareCalculateMsg();
        void showLocationMsg(String location);
        void showRecordResult(RecordRes recordRes);
    }

    public interface Presenter extends BasePresenter{
        ///准备理赔计算
        void prepareCalculate();
        //进行理赔计算
        void startCalculate(Record record);

        ///高德定位与显示
        void getLocation();

        ///咨询
        void consult();

        //停止定位
        void stopLocation();
        ///销毁定位管理
        void destroyLocation();
    }
}
