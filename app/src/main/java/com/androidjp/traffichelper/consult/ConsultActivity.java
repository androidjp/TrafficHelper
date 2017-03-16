package com.androidjp.traffichelper.consult;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.androidjp.lib_common_util.system.ActivityUtil;
import com.androidjp.lib_common_util.system.KeyBoardUtil;
import com.androidjp.lib_common_util.system.PermissionUtil;
import com.androidjp.traffichelper.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 人机交互 界面
 * Created by androidjp on 2017/2/21.
 */

public class ConsultActivity extends SwipeBackActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    /**
     * 咨询界面
     */
    private ConsultContract.Presenter mConsultPresenter;
    private ConsultContract.View mConsultFragment;

    private String[] permissions = {
            Manifest.permission.RECORD_AUDIO
    };
    private String[] permissions2 = {
            Manifest.permission.WRITE_SETTINGS
    };
    private String[] permissions3 = {
            Manifest.permission.READ_PHONE_STATE

    };

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        ButterKnife.bind(this);
        ///配置 滑动返回（SwipeBack） 相关操作
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        /// 动态创建和添加Fragment 到 Activity
        mConsultFragment = (ConsultContract.View) getSupportFragmentManager().findFragmentById(R.id.frame_consult_content);
        if (mConsultFragment == null) {
            mConsultFragment = new ConsultFragment();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), (Fragment) mConsultFragment, R.id.frame_consult_content);
        }
        /// MVP： 为 View 绑定 Presenter
        mConsultPresenter = new ConsultPresenter(this, mConsultFragment);
        mConsultFragment.setPresenter(mConsultPresenter);

        //设置Toolbar
        this.mToolbar.setNavigationIcon(R.drawable.back);
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /// Level 23 动态申请权限
        PermissionUtil.checkForPermission(this, permissions);
//        PermissionUtil.checkForPermission(this, permissions2);
//        PermissionUtil.checkForPermission(this, permissions3);

    }

        // 用户权限 申请 的回调方法
        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == 321) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            showDialogTipUserGoToAppSettting(permissions[0]);
                        } else
                            finish();
                    } else {
                        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting(String permission) {

        dialog = new AlertDialog.Builder(this)
                .setTitle(permission+"权限不可用")
                .setMessage("请在-应用设置-权限-中，允许本App使用"+permission+"权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting(permissions[0]);
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyBoardUtil.hideKeyboard(ConsultActivity.this);
        this.mConsultPresenter = null;
    }
}
