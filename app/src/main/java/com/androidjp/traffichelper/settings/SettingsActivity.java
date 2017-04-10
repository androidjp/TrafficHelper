package com.androidjp.traffichelper.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidjp.lib_common_util.ui.SnackUtil;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.litepal.DBManager;
import com.androidjp.traffichelper.data.model.retrofit.ServiceGenerator;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.orhanobut.logger.Logger;

import org.litepal.crud.callback.UpdateOrDeleteCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 设置界面
 * Created by androidjp on 2017/2/6.
 */
public class SettingsActivity extends SwipeBackActivity implements View.OnClickListener{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.layout_clear_dialogue)
    RelativeLayout mLayoutClearCache;
    @Bind(R.id.layout_about_us)
    RelativeLayout mLayoutAboutUs;

    SweetAlertDialog mSDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        ButterKnife.bind(this);
        mToolbar.setTitle(getResources().getString(R.string.settings));
        mToolbar.setNavigationIcon(R.drawable.back);
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.mLayoutClearCache.setOnClickListener(this);
        this.mLayoutAboutUs.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_clear_dialogue:
                mSDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                mSDialog.setTitleText("是否清空问答记录？")
                        .setConfirmText("清空")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                                sweetAlertDialog.setTitleText("正在清理问答记录").show();
                                DBManager.getInstance().clearDialogueList(new UpdateOrDeleteCallback() {
                                    @Override
                                    public void onFinish(int rowsAffected) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        SnackUtil.show(mLayoutClearCache,"清理完成");
                                    }
                                });
                            }
                        }).setCancelable(true);
                mSDialog.setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                mSDialog.show();
                break;


            case R.id.layout_about_us:
                ///TODO: 根据初步的理赔计算公式 ，开始计算
                //TODO:使用Retrofit2 上传Record并后端计算
                SnackUtil.show(mLayoutAboutUs,"有理赔，最快捷贴心的理赔助理App");
                break;
        }
    }
}
