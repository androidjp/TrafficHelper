package com.androidjp.traffichelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能Activity基类
 * 1. ButterKnife
 * 2. Toolbar设置
 * 3. 滑动返回设置
 * Created by androidjp on 2017/3/20.
 */

public class BaseSubActivity extends SwipeBackActivity {
    @Bind(R.id.toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.frame_common_sub_content)
    public FrameLayout mSubContent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_sub);
        ButterKnife.bind(this);
        initToolbar();
        ///配置 滑动返回（SwipeBack） 相关操作
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        //设置swipebacklayout的底色
        getSwipeBackLayout().setBackgroundResource(R.color.blue);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
