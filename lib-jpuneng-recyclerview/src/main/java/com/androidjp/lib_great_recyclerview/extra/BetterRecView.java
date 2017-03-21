package com.androidjp.lib_great_recyclerview.extra;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决与Scrollview滑动冲突的RecyclerView
 * Created by androidjp on 2017/3/21.
 */

public class BetterRecView extends RecyclerView{

    private static final String TAG = "BetterRecView";

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    public BetterRecView(Context context) {
        this(context,null);
    }

    public BetterRecView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BetterRecView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
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
