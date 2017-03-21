package com.androidjp.lib_custom_view.scrollview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决与 父布局HorizontalScrollView滑动冲突的内部NestedScrollView
 * Created by androidjp on 2017/3/21.
 */

public class BetterNestedScrollView extends NestedScrollView{
    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;


    public BetterNestedScrollView(Context context) {
        super(context);
    }

    public BetterNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BetterNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                //如果是左右滑动
                if (Math.abs(deltaX) > 3*Math.abs(deltaY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            }
        }
        mLastXIntercept = x;
        mLastYIntercept = y;
        return super.dispatchTouchEvent(ev);
    }
}
