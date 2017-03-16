package com.androidjp.lib_jpuneng_slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.androidjp.lib_common_util.ui.DisplayUtil;

/**
 * 万能的侧滑菜单控件（zhy）
 * Created by androidjp on 2016/12/24.
 */

public class SuperSlidingMenu extends HorizontalScrollView{

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BOTH = 2;
    public static final int SIMPLE = 0;//简单滑动
    public static final int DRAWER = 1;//抽屉
    public static final int DRAWER_INNER = 2;//内部抽屉

    public static final String TAG = "SuperSlidingMenu";

    /**
     * 标志
     */
    private boolean isOpen = false;
    private boolean isInit = false;
    private boolean isLeftMenuOpen = false;
    private boolean isRightMenuOpen = false;
    private boolean isOptLeft = false;///操作左侧菜单
    private boolean isOptRight = false;///操作右侧菜单

    /**
     * 屏幕宽度
     */
    private int mScreenWidth;

    /**
     * 间距相关
     */
    //右侧菜单的左边分隔量
    private int leftPadding;
    //左侧菜单的右侧分隔量
    private int rightPadding;

    /**
     * 侧滑菜单 方向
     */
    private int mSlideGravity;

    /**
     * 滑动方式
     */
    private int mLeftSlidingStyle;
    private int mRightSlidingStyle;


    /**
     * View引用
     */
    private View mLeftMenu;
    private View mRightMenu;
    private View mContent;

    /**
     * Menu Width 相关
     */
    private int mLeftMenuWidth;
    private int mRightMenuWidth;
    private int mLeftHalfWidth;// 1/2
    private int mRightHalfWidth;// 1/2

    /**
     * 监听
     */
    private OnMenuOpenListener mMenuOpenListener;

    public SuperSlidingMenu(Context context) {
        this(context,null,0);
    }

    public SuperSlidingMenu(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SuperSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initSettings(context);

        ///获取自定义属性值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SuperSlidingMenu,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i=0;i<n;i++){
            int attr = a.getIndex(i);
            if (attr == R.styleable.SuperSlidingMenu_slide_gravity) {
                this.mSlideGravity = a.getInt(attr, LEFT);
            }else if (attr == R.styleable.SuperSlidingMenu_leftPadding){///设定左间距
                this.leftPadding = a.getInt(attr, DisplayUtil.dip2px(50, (Activity) context));
            }else if (attr == R.styleable.SuperSlidingMenu_rightPadding){///设定右间距
                this.rightPadding = a.getInt(attr, DisplayUtil.dip2px(50, (Activity) context));
            }else if (attr == R.styleable.SuperSlidingMenu_left_sliding_style){
                this.mLeftSlidingStyle = a.getInt(attr, SIMPLE);
            }else if (attr == R.styleable.SuperSlidingMenu_right_sliding_style){
                this.mRightSlidingStyle = a.getInt(attr, SIMPLE);
            }
        }
        a.recycle();

    }

    private void initSettings(Context context) {
        mScreenWidth = DisplayUtil.getScreenWidth((Activity) context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 首次测量时，isInit 为 false，此时，测量并初始化相关的参数
         */
        initMeasureParams();

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void initMeasureParams(){
        /**
         * 首次测量时，isInit 为 false，此时，测量并初始化相关的参数
         */
        if (!isInit){
            LinearLayout wrapper = (LinearLayout) this.getChildAt(0);
            if (wrapper == null)
                throw new RuntimeException("SuperSlidingMenu内部没有子view！");
            switch (this.mSlideGravity){
                case LEFT:
                    this.mLeftMenu =  wrapper.getChildAt(0);
                    this.mContent = wrapper.getChildAt(1);
                    this.mRightMenu = null;
                    break;
                case RIGHT:
                    this.mLeftMenu = null;
                    this.mContent = wrapper.getChildAt(0);
                    this.mRightMenu = wrapper.getChildAt(1);
                    break;
                case BOTH:
                    this.mLeftMenu =  wrapper.getChildAt(0);
                    this.mContent = wrapper.getChildAt(1);
                    this.mRightMenu = wrapper.getChildAt(2);
                    break;
            }

            if (this.mLeftMenu!=null){
                mLeftMenuWidth = mScreenWidth - rightPadding;
                mLeftHalfWidth = mLeftMenuWidth/2;
                this.mLeftMenu.getLayoutParams().width = mLeftMenuWidth;
            }
            if (this.mRightMenu!=null){
                mRightHalfWidth = mScreenWidth - leftPadding;
                mRightHalfWidth = mRightMenuWidth/2;
                this.mRightMenu.getLayoutParams().width = mRightHalfWidth;
            }
            this.mContent.getLayoutParams().width = mScreenWidth;
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            this.scrollTo(this.mLeftMenuWidth,0);
            isInit = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();

                if (isOptLeft){///操作左侧菜单
                    if (scrollX > mLeftHalfWidth){
                        this.smoothScrollTo(mLeftMenuWidth,0);
                        ///如果当前左侧菜单开启着，且 listener不会空，则回调关闭菜单
                        if (isLeftMenuOpen && mMenuOpenListener!=null){
                            mMenuOpenListener.onMenuOpen(false,LEFT);
                        }
                        isLeftMenuOpen = false;
                    }else{
                        this.smoothScrollTo(0,0);
                        ////如果当前是关闭状态，现在要开启
                        if (!isLeftMenuOpen && mMenuOpenListener!=null){
                            mMenuOpenListener.onMenuOpen(true,LEFT);
                        }
                        isLeftMenuOpen = true;
                    }
                }
                if (isOptRight){///操作后侧菜单
                    ////scrollx = 当前显示的最左的x相对于整个rootLayout的最左侧的x差值
                    if (scrollX > mRightHalfWidth + mLeftMenuWidth){///打开右侧
                        this.smoothScrollTo(mLeftMenuWidth+mRightMenuWidth,0);
                        if (!isRightMenuOpen && mMenuOpenListener!=null){
                            mMenuOpenListener.onMenuOpen(true,RIGHT);
                        }
                        isRightMenuOpen = true;
                    }else{///关闭右侧菜单
                        this.smoothScrollTo(mLeftMenuWidth,0);
                        if (isRightMenuOpen && mMenuOpenListener!=null){
                            mMenuOpenListener.onMenuOpen(false,RIGHT);
                        }
                        isRightMenuOpen = false;
                    }
                }

                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /**
         * 这里，判断 滑动方向！！！
         */
        if (l > mLeftMenuWidth){
            isOptRight = true;
            isOptLeft = false;
        }else{
            isOptLeft = true;
            isOptRight= false;
        }

        //左侧
        if (isOptLeft){
            float scale = l*1.0f/mLeftMenuWidth;///1.0~0.0  |  1.0~2.0
            switch (mLeftSlidingStyle){
                case DRAWER:
                        mContent.setTranslationX(mLeftMenuWidth*(scale-1));//0.0 ~ -mLeftMenuWidth
                    break;
                case DRAWER_INNER:
                        mLeftMenu.setTranslationX(mLeftMenuWidth*scale);
                    break;
            }
        }
        ///右侧
        else{
            float scale = l*1.0f/mLeftMenuWidth;///1.0~0.0  |  1.0~2.0
            switch (mRightSlidingStyle){
                case DRAWER:
                    mContent.setTranslationX(mRightMenuWidth*(scale-1));//0.0 ~ mRightMenuWidth
                    break;
                case DRAWER_INNER:
                     mRightMenu.setTranslationX(-mRightMenuWidth*(2-scale));//
                    break;
            }
        }

    }


    //--------------------------------------------------------------
    // 接口方法
    //--------------------------------------------------------------

    public void setOnMenuOpenListener(OnMenuOpenListener listener){
        this.mMenuOpenListener = listener;
    }
}
