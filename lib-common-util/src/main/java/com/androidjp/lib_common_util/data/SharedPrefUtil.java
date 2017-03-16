package com.androidjp.lib_common_util.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;


/**
 * Created by JP on 2016/3/24.
 */
public class SharedPrefUtil {
    private static final String APP_PREFERENCES = "APP_PREFERENCES";

    private static SharedPreferences sharedPrefs;


    /**
     * 初始化并返回一个SPF文件对象
     * @param context
     * @return
     */
    public static SharedPreferences getInstance(Context context) {
        if (sharedPrefs == null) {
            sharedPrefs = context.getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
        return sharedPrefs;
    }

}
