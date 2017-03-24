package com.androidjp.traffichelper.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.androidjp.lib_common_util.system.ActivityUtil;
import com.androidjp.traffichelper.BaseSubActivity;
import com.androidjp.traffichelper.R;

/**
 *
 * Created by androidjp on 2017/3/22.
 */

public class HistoryActivity extends BaseSubActivity{

    HistoryContract.Presenter mPresenter;
    HistoryContract.View mHistoryFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle("我的理赔历史");

        mHistoryFragment = new RecordListFragment();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),(Fragment)mHistoryFragment, R.id.frame_common_sub_content);
        mPresenter = new RecordListPresenter(getApplicationContext(),mHistoryFragment);
        mHistoryFragment.setPresenter(mPresenter);
    }
}
