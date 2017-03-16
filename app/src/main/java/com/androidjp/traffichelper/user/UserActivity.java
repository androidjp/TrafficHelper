package com.androidjp.traffichelper.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androidjp.traffichelper.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * UserAct
 * Created by androidjp on 2016/12/9.
 */

public class UserActivity extends SwipeBackActivity implements UserContract.View{

    @Bind(R.id.toolbar_user)
    Toolbar mToolbar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
    }

    @Override
    public void loading() {

    }

    @Override
    public void finishLoad() {

    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {

    }
}
