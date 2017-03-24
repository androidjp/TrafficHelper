package com.androidjp.traffichelper.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.androidjp.lib_common_util.system.ActivityUtil;
import com.androidjp.traffichelper.BaseSubActivity;
import com.androidjp.traffichelper.R;

/**
 * UserAct
 * Created by androidjp on 2016/12/9.
 */

public class UserActivity extends BaseSubActivity{
    private UserContract.Presenter userPresenter;
    private UserContract.View mView;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mToolbar.setTitle("用户信息");

        mView = new UserFragment();
        userPresenter = new UserPresenter(this,mView);
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),(Fragment)mView,R.id.frame_common_sub_content);
        mView.setPresenter(userPresenter);
    }
}
