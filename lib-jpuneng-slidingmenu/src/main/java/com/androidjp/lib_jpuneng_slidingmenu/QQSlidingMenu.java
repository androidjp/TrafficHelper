package com.androidjp.lib_jpuneng_slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.androidjp.lib_common_util.ui.DisplayUtil;

/**
 * 仿QQ的侧滑菜单
 * Created by androidjp on 2016/12/20.
 */

public class QQSlidingMenu extends HorizontalScrollView {

    private boolean once;//初始化标志
    private boolean isOpen;//是否已打开 标志

    private int mScreenWidth;//屏幕宽度
    private int mMenuRightPadding;//dp


    //菜单宽度
    private int mMenuWidth;
    private int mHalfMenuWidth;

    //侧滑菜单 和 主页
    private ViewGroup mMenu;
    private ViewGroup mContent;


    public QQSlidingMenu(Context context) {
        this(context, null, 0);
    }

    public QQSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public QQSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ///获取屏幕宽度
        mScreenWidth = DisplayUtil.getScreenWidth((Activity) context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.QQSlidingMenu, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.QQSlidingMenu_rightPadding) {
                mMenuRightPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics()));
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) wrapper.getChildAt(0);
            mContent = (ViewGroup) wrapper.getChildAt(1);

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth = mMenuWidth / 3;
            mMenu.getLayoutParams().width = mMenuWidth;
            mContent.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            ///菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:

                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;///菜单已打开
                }
                return true;
        }


        return super.onTouchEvent(event);
    }

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        Log.e("Tag", "========X轴距离差：" + dx);
        Log.e("Tag", "========Y轴距离差：" + dy);
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        ///变化只在x轴上，所以，直接分析x上的变化即可
        // 初始：l = mMenuWidth
        // 滑出菜单后： l = 0
        float scale = l * 1.0f / mMenuWidth;//缩放进度（从1.0 到 0.0）

        /**
         * 内容区缩放比例
         */
        float contentScale = 0.8f + scale * 0.2f;///（1.0~0.0）变成（1.0~0.8）

        /**
         * 菜单的缩放比例
         */
        float menuScale = 1.0f - scale * 0.3f;/// (0.7 ~ 1.0)

        float menuAlpha = 1.0f - scale * 0.5f;/// (0.5 ~ 1.0)

        /**
         * X方向 的 菜单偏移量
         * 初始：菜单向左偏移60%
         * 拖出菜单后：菜单向左偏移 0
         */
        float menuTranslateX = mMenuWidth * scale * 0.6f;////(0.6*menuWidth ~ 0)


        /**
         * 实际调用
         */

        onScrollAnimation(contentScale, menuScale, menuAlpha, menuTranslateX);

    }

    /**
     * 滑动操作相关ViewGroup刷新
     */
    private void onScrollAnimation(float contentScale, float menuScale, float menuAlpha, float menuTranslateX) {
//        mContent.setScaleX(contentScale);
        mMenu.setScaleX(menuScale);
        mMenu.setScaleY(menuScale);
        mMenu.setAlpha(menuAlpha);
        mMenu.setTranslationX(menuTranslateX);
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

    /**
     * 是否被打开了
     * @return
     */
    public boolean isOpen(){
        return isOpen;
    }
}
