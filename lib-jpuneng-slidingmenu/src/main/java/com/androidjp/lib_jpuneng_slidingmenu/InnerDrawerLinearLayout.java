package com.androidjp.lib_jpuneng_slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 用于配合 SuperSlidingMenu 的 滑动样式为DrawerInner + Both（双向都有侧滑） 的情况
 * Created by androidjp on 2016/12/24.
 */

public class InnerDrawerLinearLayout extends LinearLayout {


    public InnerDrawerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingCacheEnabled(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i==0)
            return 0;
        if (i==1)
            return 2;
        if (i==2)
            return 1;
        return super.getChildDrawingOrder(childCount, i);
    }
}
