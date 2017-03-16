package com.androidjp.lib_common_util.ui;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * 代替Toast的SnakeBar的显示
 * Created by androidjp on 16/9/26.
 */
public class SnackUtil {

    ///短时间提示
    public static void show(View view , String msg){
        Snackbar.make(view, msg,2000).show();
    }

    public static void show(View view , int msgId){
        Snackbar.make(view, msgId,2000).show();
    }


    ///长时间提示
    public static void showLong(View view ,String msg){
        Snackbar.make(view,msg,5000).show();
    }

    public static void showLong(View view ,int msgId){
        Snackbar.make(view,msgId,5000).show();
    }

    public static void showAndOpt(View view , String msg, String tip, final SnackListener snackListener){
        Snackbar.make(view, msg,2000).setAction(tip, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackListener!=null)
                    snackListener.onClick(v);
            }
        }).setCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar snackbar) {
                if (snackListener!=null)
                    snackListener.onShown(snackbar);
            }

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (snackListener!=null)
                    snackListener.onDismissed(snackbar,event);
            }
        }).show();
    }

    public static void showAndOpt(View view , int msgId, int tipId, final SnackListener snackListener){
        Snackbar.make(view, msgId,2000).setAction(tipId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackListener!=null)
                    snackListener.onClick(v);
            }
        }).setCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar snackbar) {
                if (snackListener!=null)
                    snackListener.onShown(snackbar);
            }

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (snackListener!=null)
                    snackListener.onDismissed(snackbar,event);
            }
        }).show();
    }


    static interface SnackListener{
        public void onClick(View v);
        public void onShown(Snackbar snackbar);
        public void onDismissed(Snackbar snackbar,int event);
    }


}
