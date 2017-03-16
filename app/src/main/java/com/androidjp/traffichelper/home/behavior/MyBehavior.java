package com.androidjp.traffichelper.home.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by androidjp on 2017/3/12.
 */

public class MyBehavior extends CoordinatorLayout.Behavior<View> {

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof NestedScrollView) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_BM = lp.bottomMargin;
            int distance = child.getHeight() + fab_BM ;
            child.setY(dependency.getY() - distance);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
