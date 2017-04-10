package com.androidjp.traffichelper;

import android.content.Context;

import org.litepal.LitePalApplication;


/**
 * 自定义Application基类
 * Created by androidjp on 2016/12/1.
 */

public class THApplication extends LitePalApplication {

    private static THApplication sInstance;

    public static Context getContext(){
        return getInstance().getApplicationContext();
    }

    public static THApplication getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        ///创建一下数据库表
//        SQLiteDatabase db = LitePal.getDatabase();
//        db.close();
    }

}
