package com.androidjp.lib_common_util.system;

import android.util.Log;

/**
 * Created by JP on 2016/3/20.
 */
public class LogUtil {

    /*开发*/
    private final static int DEVELOP = 0;
    /*测试*/
    private final static int TEST = 1;
    /*上限*/
    private final static int ONLINE = 2;


    private final static int CurrentState = DEVELOP;


    public static void info(Class clazz,String msg){
        String TAG = clazz.getSimpleName();
        switch (CurrentState){
            case DEVELOP:
                Log.i(TAG,"develop <---" + msg);
                break;
            case TEST:
                Log.i(TAG,"test <---" + msg);
                break;
            case ONLINE:
                Log.i(TAG,"online <---" + msg);
                break;
        }
    }

    public static void debug(Class clazz, String msg) {
        String TAG = clazz.getSimpleName();
        switch (CurrentState){
            case DEVELOP:
                Log.d(TAG,"develop <---" + msg);
                break;
            case TEST:
                Log.d(TAG,"test <---" + msg);
                break;
            case ONLINE:
                Log.d(TAG,"online <---" + msg);
                break;
        }
    }

    public static void error(Class clazz, Exception e) {
        String TAG = clazz.getSimpleName();
        switch (CurrentState){
            case DEVELOP:
                Log.e(TAG,"develop <---exception <---" + e.getMessage());
                break;
            case TEST:
                Log.e(TAG,"test <---exception <---" + e.getMessage());
                break;
            case ONLINE:
                Log.e(TAG,"online <---exception <---" + e.getMessage());
                break;
        }
    }

    public static void error(Class clazz, String e) {
        String TAG = clazz.getSimpleName();
        switch (CurrentState){
            case DEVELOP:
                Log.e(TAG,"develop <---" + e);
                break;
            case TEST:
                Log.e(TAG,"test <---" + e);
                break;
            case ONLINE:
                Log.e(TAG,"online <---" + e);
                break;
        }
    }

}
