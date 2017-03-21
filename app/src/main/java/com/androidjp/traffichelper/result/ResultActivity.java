package com.androidjp.traffichelper.result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.androidjp.lib_common_util.system.ActivityUtil;
import com.androidjp.traffichelper.BaseSubActivity;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.RecordRes;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by androidjp on 2017/3/20.
 */

public class ResultActivity extends BaseSubActivity{

    private ResultContract.Presenter mPresenter;
    private ResultFragment mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("理赔结果");


        ///停止滑动
        getSwipeBackLayout().setEnableFlingBack(false);
        getSwipeBackLayout().setEnablePullToBack(false);

        ///添加Fragment
        mView = new ResultFragment();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),mView, R.id.frame_common_sub_content);
        mPresenter = new ResultPresenter(this,mView);
        mView.setPresenter(mPresenter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ///获取数据
        if (getIntent()!=null){
            Bundle bundle = getIntent().getExtras();
            if (bundle !=null){
                RecordRes recordRes = bundle.getParcelable("recordRes");
                if (recordRes!=null){
                    mPresenter.prepareDatas(recordRes);
                }
            }
        }
    }
}
