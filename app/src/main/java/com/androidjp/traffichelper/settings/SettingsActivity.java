package com.androidjp.traffichelper.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidjp.traffichelper.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by androidjp on 2017/2/6.
 */

public class SettingsActivity extends SwipeBackActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.layout_about_us)
    RelativeLayout mLayoutAboutUs;

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
    }
}
