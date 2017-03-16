package com.androidjp.traffichelper.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.androidjp.lib_common_util.system.ActivityUtil;
import com.androidjp.lib_common_util.ui.SnackUtil;
import com.androidjp.lib_jpuneng_slidingmenu.QQSlidingMenu;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.consult.ConsultActivity;
import com.androidjp.traffichelper.consult.ConsultContract;
import com.androidjp.traffichelper.consult.ConsultFragment;
import com.androidjp.traffichelper.consult.ConsultPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.menu_main)
    QQSlidingMenu mMenu;
//    @Bind(R.id.fab_consult)
//    FloatingActionButton btnConsult;
//    @Bind(R.id.fab_calculate)
//    FloatingActionButton btnCalculte;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    /**
     * 计算界面
     */
    private MainContract.Presenter mMainPresenter;
    private MainContract.View mMainFramgent;
    /**
     * 侧滑菜单
     */
    private MainMenuContract.View mMainMenuFragment;
    private MainMenuContract.Presenter mMainMenuPresenter;

    /**
     * 咨询界面
     */
    private ConsultContract.Presenter mConsultPresenter;
    private ConsultContract.View mConsultFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mMainMenuFragment = (MainMenuContract.View) getSupportFragmentManager().findFragmentById(R.id.frame_menu);
        mMainFramgent = (MainContract.View) getSupportFragmentManager().findFragmentById(R.id.frame_content);
        mConsultFragment = (ConsultContract.View) getSupportFragmentManager().findFragmentById(R.id.root_consult);

        if (mMainMenuFragment == null) {
            mMainMenuFragment = new MainMenuFragment();
            //setArgument() 之类的
        }
        if (mMainFramgent == null) {
            mMainFramgent = new MainFragment();
        }
        if (mConsultFragment == null){
            mConsultFragment = new ConsultFragment();
        }


        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), (Fragment) mMainMenuFragment, R.id.frame_menu);
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), (Fragment) mMainFramgent, R.id.frame_content);
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), (Fragment) mConsultFragment,R.id.root_consult);

        ////初始化Presenter
        mMainMenuPresenter = new MainMenuPresenter(THApplication.getContext(), mMainMenuFragment);
        mMainPresenter = new MainPresenter(THApplication.getContext(), mMainFramgent);
        mConsultPresenter = new ConsultPresenter(THApplication.getContext(),mConsultFragment);

        mMainMenuFragment.setPresenter(mMainMenuPresenter);
        mMainFramgent.setPresenter(mMainPresenter);
        mConsultFragment.setPresenter(mConsultPresenter);

        ////设置自定义的上划Fragment
        BottomSheetBehavior.from(nestedScrollView).setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            mMenu.toggle();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mMenu.isOpen())
                mMenu.toggle();
            else
                onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setCancelable(true);
        dialog.setTitleText("是否退出App？").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        }).setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
                    }
                }).show();
    }

//    /**
//     * 咨询
//     */
//    @OnClick(R.id.fab_consult)
//    public void openConsultPage(View view){
//        ///打开下方的咨询界面
////        toggleConsultPage();
//        Intent intent = new Intent(this, ConsultActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    //开关consult界面
    public void toggleConsultPage() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.nestedScrollView));
        if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    //隐藏consult界面
    private void closeConsultPage(){
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.nestedScrollView));
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


//    /**
//     * 准备计算
//     */
//    @OnClick(R.id.fab_calculate)
//    public void startCalculate(View view){
//
//        closeConsultPage();
//
//        SweetAlertDialog sDialog = new SweetAlertDialog(this);
//        sDialog.setCancelable(true);
//        sDialog.setTitleText("计算理赔？").setCancelText("取消").setConfirmText("开始计算")
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
//                ///开始计算
//                mMainPresenter.prepareCalculate();
//
//            }
//        }).show();
//    }

}
