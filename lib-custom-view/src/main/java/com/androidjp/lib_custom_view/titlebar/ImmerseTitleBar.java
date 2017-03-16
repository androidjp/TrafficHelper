package com.androidjp.lib_custom_view.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidjp.lib_common_util.ui.DisplayUtil;
import com.androidjp.lib_custom_view.R;

/**
 * 自定义的TitieBar
 * Created by JP on 2016/4/4.
 */
public class ImmerseTitleBar extends LinearLayout{

    private View bottomLine;

    private RelativeLayout subLayout;


    private TextView tvTitle;
    private Button btnLeft;/*文本按钮*/
    private Button btnRight;/*文本按钮*/
    private ImageButton ibtnLeft;/*图标（“返回”图标等）*/
    private ImageButton ibtnRight;
    private View leftView;/*左侧的自定义布局*/
    private View rightView;/*右侧的自定义布局*/

    //数据
    private String titleText;
    private String leftText;
    private String rightText;
    private int leftTextColorId;
    private int rightTextColorId;
    private int titleTextColorId;
    private int leftTextBgId;
    private int rightTextBgId;

    private int leftIconId;
    private int rightIconId;
    private int leftIconBgId;
    private int rightIconBgId;

    private float titleSize;
    private float leftSize;
    private float rightSize;

    /*自定义Layout 的 id*/
    private int leftViewLayout;
    private int rightViewLayout;


    /*listener 对象*/
    private LeftBtnListener leftBtnListener;
    private LeftIconListener leftIconListener;
    private LeftViewListener leftViewListener;
    private RightBtnListener rightBtnListener;
    private RightIconListener rightIconListener;
    private RightViewListener rightViewListener;

    private LeftBtnLongListener leftBtnLongListener;
    private LeftIconLongListener leftIconLongListener;
    private LeftViewLongListener leftViewLongListener;
    private RightBtnLongListener rightBtnLongListener;
    private RightIconLongListener rightIconLongListener;
    private RightViewLongListener rightViewLongListener;



    public ImmerseTitleBar(Context context) {
        this(context,null);
    }

    public ImmerseTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public ImmerseTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImmerseTitleBar);
        /*TODO:设置组件的属性*/
        initView(context, array);
        initEvent();

        if (context instanceof Activity){
            ImmerseAdapter.setImmerseStyle((Activity)context);
        }
    }

    private void initView(Context context,TypedArray array) {

        subLayout = new RelativeLayout(context);
        subLayout.setBackgroundResource(R.color.default_transparent);
        tvTitle = new TextView(subLayout.getContext());
        btnLeft = new Button(subLayout.getContext());
        btnRight = new Button(subLayout.getContext());
        ibtnLeft = new ImageButton(subLayout.getContext());
        ibtnRight = new ImageButton(subLayout.getContext());
        bottomLine = new  View(context);

        titleText = array.getString(R.styleable.ImmerseTitleBar_titleText);
        leftText = array.getString(R.styleable.ImmerseTitleBar_leftBtnText);
        rightText = array.getString(R.styleable.ImmerseTitleBar_rightBtnText);
        leftTextColorId = array.getColor(R.styleable.ImmerseTitleBar_leftBtnTextColor,Color.BLACK);
        rightTextColorId = array.getColor(R.styleable.ImmerseTitleBar_rightBtnTextColor,Color.BLACK);
        titleTextColorId = array.getColor(R.styleable.ImmerseTitleBar_titleColor, Color.BLACK);
        leftTextBgId = array.getResourceId(R.styleable.ImmerseTitleBar_leftBtnBg,R.color.default_transparent);
        rightTextBgId = array.getResourceId(R.styleable.ImmerseTitleBar_rightBtnBg,R.color.default_transparent);

        leftIconId = array.getResourceId(R.styleable.ImmerseTitleBar_leftIcon,R.mipmap.ic_launcher);
        rightIconId = array.getResourceId(R.styleable.ImmerseTitleBar_rightIcon,R.mipmap.ic_launcher);
        leftIconBgId = array.getResourceId(R.styleable.ImmerseTitleBar_leftIconBg,R.color.default_transparent);
        rightIconBgId = array.getResourceId(R.styleable.ImmerseTitleBar_rightIconBg,R.color.default_transparent);

        titleSize = array.getDimension(R.styleable.ImmerseTitleBar_titleSize,20);
        leftSize = array.getDimension(R.styleable.ImmerseTitleBar_leftBtnTextSize,16);
        rightSize = array.getDimension(R.styleable.ImmerseTitleBar_rightBtnTextSize,16);

        leftViewLayout = array.getResourceId(R.styleable.ImmerseTitleBar_leftLayout,0);
        rightViewLayout = array.getResourceId(R.styleable.ImmerseTitleBar_rightLayout,0);

        array.recycle();

        /**
         * addViews
         */

        LayoutParams mSubLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(70,(Activity)context));

        addView(subLayout,mSubLayoutParams);

        LayoutParams mBottomLineParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,1
        );
        bottomLine.setBackgroundResource(android.R.color.darker_gray);
        addView(bottomLine,mBottomLineParams);


        tvTitle.setText(titleText);
        tvTitle.setTextSize(titleSize);
        tvTitle.setTextColor(titleTextColorId);

        btnLeft.setText(leftText);
        btnLeft.setTextSize(leftSize);
        btnLeft.setTextColor(leftTextColorId);
        btnLeft.setBackgroundResource(leftTextBgId);
        btnLeft.setPadding(0,0,0,0);

        btnRight.setText(rightText);
        btnRight.setTextSize(rightSize);
        btnRight.setTextColor(rightTextColorId);
        btnRight.setBackgroundResource(rightTextBgId);
        btnRight.setPadding(0,0,0,0);

        ibtnLeft.setImageResource(leftIconId);
        ibtnLeft.setBackgroundResource(leftIconBgId);

        ibtnRight.setImageResource(rightIconId);
        ibtnRight.setBackgroundResource(rightIconBgId);


        if (leftViewLayout!=0){
            leftView = LayoutInflater.from(context).inflate(leftViewLayout,null);
        }else{
            leftView = new View(context);
        }
        if (rightViewLayout!=0){
            rightView = LayoutInflater.from(context).inflate(rightViewLayout,null);
        }else{
            rightView = new View(context);
        }

        RelativeLayout.LayoutParams mLeftViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLeftViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mLeftViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mLeftViewParams.setMargins(DisplayUtil.dip2px(12,(Activity)context),0,0,0);
        subLayout.addView(leftView,mLeftViewParams);

        RelativeLayout.LayoutParams mRightViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRightViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mRightViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mRightViewParams.setMargins(0,0,DisplayUtil.dip2px(12,(Activity)context),0);
        subLayout.addView(rightView,mRightViewParams);

        RelativeLayout.LayoutParams mLeftParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(40,(Activity)context));
        mLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mLeftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mLeftParams.setMargins(DisplayUtil.dip2px(12,(Activity)context),0,0,0);
        subLayout.addView(btnLeft,mLeftParams);

        RelativeLayout.LayoutParams mRightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(40,(Activity)context));
        mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mRightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mRightParams.setMargins(0,0,DisplayUtil.dip2px(12,(Activity)context),0);
        subLayout.addView(btnRight,mRightParams);


        RelativeLayout.LayoutParams mTitleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        subLayout.addView(tvTitle,mTitleParams);


        RelativeLayout.LayoutParams mILeftParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(60,(Activity)context), DisplayUtil.dip2px(50,(Activity)context));
        mILeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mILeftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mILeftParams.setMargins(DisplayUtil.dip2px(12,(Activity)context),0,0,0);
        subLayout.addView(ibtnLeft,mILeftParams);

        RelativeLayout.LayoutParams mIRightParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(60,(Activity)context), DisplayUtil.dip2px(50,(Activity)context));
        mIRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mIRightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mIRightParams.setMargins(0,0,DisplayUtil.dip2px(12,(Activity)context),0);
        subLayout.addView(ibtnRight,mIRightParams);




        /**
         * 设置适配布局
         * ①两个都注释掉，布局整体上去了，没有适配好
         * ②注释了MinHeight，也一样没有适配
         * ③注释了fitSsytemWin，可以了，也就是说，没有特别大的关系，只要你内部有子View或者ViewGroup有高度，就行
         */

        setFitsSystemWindows(true);
        hideAll();
    }

    private void initEvent() {

        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftBtnListener!=null) leftBtnListener.onLeftBtnClick();
            }
        });

        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightBtnListener!=null) rightBtnListener.onRightBtnClick();
            }
        });

        ibtnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftIconListener!=null) leftIconListener.onLeftIconClick();
            }
        });

        ibtnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightIconListener!=null) rightIconListener.onRightIconClick();
            }
        });


        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftViewListener!=null) leftViewListener.onLeftViewClick();
            }
        });

        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightViewListener!=null) rightViewListener.onRightViewClick();
            }
        });


        btnLeft.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (leftBtnLongListener!=null) leftBtnLongListener.onLeftBtnLongClick();
                return false;
            }

        });

        btnRight.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (rightBtnLongListener!=null) rightBtnLongListener.onRightBtnLongClick();
                return  false;
            }
        });

        ibtnLeft.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (leftIconLongListener!=null) leftIconLongListener.onLeftIconLongClick();
                return false;
            }
        });

        ibtnRight.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (rightIconLongListener!=null) rightIconLongListener.onRightIconLongClick();
                return false;
            }
        });

        leftView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (leftViewLongListener!=null) leftViewLongListener.onLeftViewLongClick();
                return false;
            }
        });

        rightView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (rightViewLongListener!=null) rightViewLongListener.onRightViewLongClick();
                return false;
            }
        });

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    //-----------------------------------------------
    // 点击和长按事件 Listener 定义
    //-----------------------------------------------

    public interface LeftBtnListener{
        public void onLeftBtnClick();
    }

    public interface RightBtnListener{
        public void onRightBtnClick();
    }

    public interface LeftIconListener{
        public void onLeftIconClick();
    }

    public interface RightIconListener{
        public void onRightIconClick();
    }

    public interface LeftViewListener{
        public void onLeftViewClick();
    }

    public interface RightViewListener{
        public void onRightViewClick();
    }


    public interface LeftBtnLongListener{
        public void onLeftBtnLongClick();
    }

    public interface RightBtnLongListener{
        public void onRightBtnLongClick();
    }

    public interface LeftIconLongListener{
        public void onLeftIconLongClick();
    }

    public interface RightIconLongListener{
        public void onRightIconLongClick();
    }

    public interface LeftViewLongListener{
        public void onLeftViewLongClick();
    }

    public interface RightViewLongListener{
        public void onRightViewLongClick();
    }

    //-----------------------------------------------
    // Enum 表示
    //-----------------------------------------------
    public enum TitleBarItem{
        TITLE, BTN_LEFT,BTN_RIGHT,ICON_LEFT,ICON_RIGHT,VIEW_LEFT, VIEW_RIGHT
    }


    //-----------------------------------------------
    // 链式调用设置方法
    //-----------------------------------------------

    /**
     * 隐藏View
     * @param item 组件枚举
     * @return titlebar本身
     */
    public ImmerseTitleBar hideView(TitleBarItem item){
        if (item == null){
            hideAll();
            return this;
        }
        switch (item){
            case TITLE:
                tvTitle.setVisibility(GONE);
                break;
            case BTN_LEFT:
                btnLeft.setVisibility(GONE);
                break;
            case BTN_RIGHT:
                btnRight.setVisibility(GONE);
                break;
            case ICON_LEFT:
                ibtnLeft.setVisibility(GONE);
                break;

            case ICON_RIGHT:
                ibtnRight.setVisibility(GONE);
                break;

            case VIEW_LEFT:
                leftView.setVisibility(GONE);
                break;
            case VIEW_RIGHT:
                rightView.setVisibility(GONE);
                break;
        }
        return this;
    }

    /**
     * 显示View
     * @param item 组件枚举
     * @return titlebar本身
     */
     public ImmerseTitleBar showView(TitleBarItem item){
        if (item == null){
            showAll();
            return this;
        }
        switch (item){
            case TITLE:
                tvTitle.setVisibility(VISIBLE);
                break;
            case BTN_LEFT:
                btnLeft.setVisibility(VISIBLE);
                break;
            case BTN_RIGHT:
                btnRight.setVisibility(VISIBLE);
                break;
            case ICON_LEFT:
                ibtnLeft.setVisibility(VISIBLE);
                break;

            case ICON_RIGHT:
                ibtnRight.setVisibility(VISIBLE);
                break;
            case VIEW_LEFT:
                leftView.setVisibility(VISIBLE);
                break;
            case VIEW_RIGHT:
                rightView.setVisibility(VISIBLE);
                break;
        }
        return this;
    }

    /**
     * 隐藏所有View
     */
    private void hideAll() {
        tvTitle.setVisibility(GONE);
        btnLeft.setVisibility(GONE);
        btnRight.setVisibility(GONE);
        ibtnLeft.setVisibility(GONE);
        ibtnRight.setVisibility(GONE);
        leftView.setVisibility(GONE);
        rightView.setVisibility(GONE);
    }

    /**
     * 显示所有View
     */
    private void showAll() {
        tvTitle.setVisibility(VISIBLE);
        btnLeft.setVisibility(VISIBLE);
        btnRight.setVisibility(VISIBLE);
        ibtnLeft.setVisibility(VISIBLE);
        ibtnRight.setVisibility(VISIBLE);
        leftView.setVisibility(VISIBLE);
        rightView.setVisibility(VISIBLE);
    }


    /**
     * @return 左侧自定义View
     */
    public View getLeftView(){
        return this.leftView;
    }

    /**
     * @return 右侧自定义View
     */
    public View getRightView(){
        return this.rightView;
    }


    /**
     * @param title Title内容
     * @return titlebar本身
     */
    public ImmerseTitleBar setTitle(String title){
        this.tvTitle.setText((!TextUtils.isEmpty(title))?title:"");
        this.tvTitle.setTextSize(24);
        this.tvTitle.setVisibility(VISIBLE);
        return this;
    }

    /**
     *
     * @param text 文本内容
     * @return titlebar本身
     */
    public ImmerseTitleBar setLeftBtnText(String text){
        this.btnLeft.setText((!TextUtils.isEmpty(text))?text:"");
        this.btnLeft.setTextSize(18);
        this.btnLeft.setVisibility(VISIBLE);
        return this;
    }

    /**
     *
     * @param text 文本内容
     * @return titlebar本身
     */
    public ImmerseTitleBar setRightBtnText(String text){
        this.btnRight.setText((!TextUtils.isEmpty(text))?text:"");
        this.btnRight.setTextSize(18);
        this.btnRight.setVisibility(VISIBLE);
        return this;
    }

    /**
     *
     * @param id  图标id（drawable中找）
     * @return titlebar本身
     */
    public ImmerseTitleBar setLeftIcon(int id){
        this.ibtnLeft.setImageResource(id);
        this.ibtnLeft.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.ibtnLeft.setVisibility(VISIBLE);
        return this;
    }

    /**
     * @param id  图标id（drawable中找）
     * @return titlebar本身
     */
    public ImmerseTitleBar setRightIcon(int id){
        this.ibtnRight.setImageResource(id);
        this.ibtnRight.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.ibtnRight.setVisibility(VISIBLE);
        return this;
    }


    /**
     *
     * @param colorId 设置字体颜色ID（在自己的colors.xml中找）
     * @return titlebar本身
     */
    public ImmerseTitleBar setAllTextColor(int colorId){
        this.tvTitle.setTextColor(colorId);
        this.btnLeft.setTextColor(colorId);
        this.btnRight.setTextColor(colorId);
        return this;
    }

    public void setLeftBtnListener(LeftBtnListener leftBtnListener) {
        this.leftBtnListener = leftBtnListener;
    }

    public void setLeftIconListener(LeftIconListener leftIconListener) {
        this.leftIconListener = leftIconListener;
    }

    public void setLeftViewListener(LeftViewListener leftViewListener) {
        this.leftViewListener = leftViewListener;
    }

    public void setRightBtnListener(RightBtnListener rightBtnListener) {
        this.rightBtnListener = rightBtnListener;
    }

    public void setRightIconListener(RightIconListener rightIconListener) {
        this.rightIconListener = rightIconListener;
    }

    public void setRightViewListener(RightViewListener rightViewListener) {
        this.rightViewListener = rightViewListener;
    }

    public void setLeftBtnLongListener(LeftBtnLongListener leftBtnLongListener) {
        this.leftBtnLongListener = leftBtnLongListener;
    }

    public void setLeftIconLongListener(LeftIconLongListener leftIconLongListener) {
        this.leftIconLongListener = leftIconLongListener;
    }

    public void setLeftViewLongListener(LeftViewLongListener leftViewLongListener) {
        this.leftViewLongListener = leftViewLongListener;
    }

    public void setRightBtnLongListener(RightBtnLongListener rightBtnLongListener) {
        this.rightBtnLongListener = rightBtnLongListener;
    }

    public void setRightIconLongListener(RightIconLongListener rightIconLongListener) {
        this.rightIconLongListener = rightIconLongListener;
    }

    public void setRightViewLongListener(RightViewLongListener rightViewLongListener) {
        this.rightViewLongListener = rightViewLongListener;
    }
}
