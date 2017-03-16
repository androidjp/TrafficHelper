package com.androidjp.lib_custom_view.titlebar;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Activity 沉浸式 TitleBar适配器
 * Created by androidjp on 16-7-2.
 */
public class ImmerseAdapter {

    public static void setImmerseStyle(Activity activity){
        /*4.4版本：全透明 5.X以上：半透明 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            // Translucent status bar
            window.setFlags(///设置status bar为透明
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
