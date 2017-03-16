package com.androidjp.lib_common_util.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidjp.lib_common_util.R;

/**
 * Created by androidjp on 2016/10/8.
 */

public class DialogUtil {

    public static ProgressDialog progressDialog;

    public static Dialog dialog;

    public static void showProgressDialog(Context context, String title, String msg) {
        progressDialog = ProgressDialog.show(context, title, msg, true);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    public static void hideProgressDialog(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

    public static void showLoadingDialog(Context context, String msg){
        if (dialog == null)
            dialog = createLoadingDialog(context,msg);
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void hideLoadingDialog(){
        if (dialog!=null)
            dialog.dismiss();
    }


}
