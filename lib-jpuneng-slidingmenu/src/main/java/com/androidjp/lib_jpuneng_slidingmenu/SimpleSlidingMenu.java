package com.androidjp.lib_jpuneng_slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.androidjp.lib_common_util.ui.DisplayUtil;

/**
 * 基于 HorizontalScrollView（横向滚动容器）的侧滑菜单
 * <p>
 * 布局：内部装一个横向的LinearLayout，LinearLayout中再装两个纵向的LinearLayout，左侧是menu，右侧是content
 * <p>
 * Created by androidjp on 2016/12/9.
 */

public class SimpleSlidingMenu extends HorizontalScrollView {

    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    private int mMenuRightPadding = 50;

    /**
     * 菜单宽度
     */
    private int mMenuWidth;
    private int mHalfMenuWidth;

    private boolean once;

    ///是否打开菜单的标志
    private boolean isOpen;

    public SimpleSlidingMenu(Context context) {
        this(context, null);
    }

    public SimpleSlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = DisplayUtil.getScreenWidth((Activity) context);//获取屏幕宽度
    }

    public SimpleSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = DisplayUtil.getScreenWidth((Activity) context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleSlidingMenu, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SimpleSlidingMenu_rightPadding) {
                mMenuRightPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics()));///默认50dp
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);///获取menu总布局
            ViewGroup content = (ViewGroup) wrapper.getChildAt(1);///获取content总布局
            //dp to px
//            mMenuRightPadding = DisplayUtil.dip2px(mMenuRightPadding, (Activity) getContext());
            mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, content.getResources().getDisplayMetrics());
            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth = mMenuWidth / 2;
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth ){
                    this.smoothScrollTo(mMenuWidth,0);
                    isOpen = false;
                }else{
                    this.smoothScrollTo(0,0);
                    isOpen = true;///菜单已打开
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 打开侧滑菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (!isOpen)
            return;
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }

    /**
     * 开关
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
}
