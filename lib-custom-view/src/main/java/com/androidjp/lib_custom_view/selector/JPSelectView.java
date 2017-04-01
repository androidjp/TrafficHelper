package com.androidjp.lib_custom_view.selector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.androidjp.lib_custom_view.R;

/**
 * 自定义改进版的选择控件
 * 参考： https://github.com/mcxtzhang/JPSelectView
 * Created by androidjp on 2017/1/14.
 */

public class JPSelectView extends View {

    protected static final String TAG = JPSelectView.class.getName();

    /**
     * 类型（数字选择/字符串列表选择）
     */
    public static final int TYPE_NUMBER = 0x11;
    public static final int TYPE_STRING = 0x12;
    protected int currentType = TYPE_NUMBER;

    /**
     * 字符串数组类型下的成员属性
     */
    private String[] stringList;
    private int currentPos = -1;

    private float mPadding;

    protected static final int DEFAULT_DURATION = 350;
    //控件 paddingLeft paddingTop + paint的width
    protected int mLeft, mTop;
    //宽高
    protected int mWidth, mHeight;


    //加减的圆的Path的Region
    protected Region mAddRegion, mDelRegion;
    protected Path mAddPath, mDelPath;

    /**
     * 加按钮
     */
    protected Paint mAddPaint;
    //加按钮是否开启fill模式 默认是stroke(xml)(false)
    protected boolean isAddFillMode;
    //加按钮的背景色前景色(xml)
    protected int mAddEnableBgColor;
    protected int mAddEnableFgColor;
    //加按钮不可用时的背景色前景色(xml)
    protected int mAddDisableBgColor;
    protected int mAddDisableFgColor;

    /**
     * 减按钮
     */
    protected Paint mDelPaint;
    //按钮是否开启fill模式 默认是stroke(xml)(false)
    protected boolean isDelFillMode;
    //按钮的背景色前景色(xml)
    protected int mDelEnableBgColor;
    protected int mDelEnableFgColor;
    //按钮不可用时的背景色前景色(xml)
    protected int mDelDisableBgColor;
    protected int mDelDisableFgColor;

    //最大、最小数量和当前数量(xml)
    protected int mMaxCount;
    protected int mCount;
    protected int mMinCount;

    //圆的半径
    protected float mRadius;
    //圆圈的宽度
    protected float mCircleWidth;
    //线的宽度
    protected float mLineWidth;


    /**
     * 两个圆之间的间距(xml)
     */
    protected float mGapBetweenCircle;
    //绘制数量的textSize
    protected float mTextSize;
    protected Paint mTextPaint;
    protected Paint.FontMetrics mFontMetrics;

    //动画的基准值 动画：减 0~1, 加 1~0 ,
    // 普通状态下都显示时是0
    protected ValueAnimator mAnimAdd, mAniDel;
    protected float mAnimFraction;

    //展开 加入购物车动画
    protected ValueAnimator mAnimExpandHint;
    protected ValueAnimator mAnimReduceHint;

    protected int mPerAnimDuration = DEFAULT_DURATION;///默认的动画持续时间都是0.35s

    /**
     * 增加一个开关 ignoreHintArea：UI显示、动画是否忽略hint收缩区域
     */
    protected boolean ignoreHintArea;

    //是否处于HintMode下 count = 0 时，且第一段收缩动画做完了，是true
    protected boolean isHintMode;

    //提示语收缩动画 0-1 展开1-0
    //普通模式时，应该是1， 只在 isHintMode true 才有效
    protected float mAnimExpandHintFraction;

    //展开动画结束后 才显示文字
    protected boolean isShowHintText;

    //数量为0时，hint文字 背景色前景色(xml) 大小
    protected Paint mHintPaint;
    protected int mHintBgColor;
    protected int mHingTextSize;
    protected String mHintText;
    protected int mHintFgColor;
    /**
     * 圆角值(xml)
     */
    protected int mHintBgRoundValue;

    //点击回调
    private OnChangeStringListener mOnChangeStringListener;
    private OnAddSubNumberListener mOnAddSubNumberListener;


    public JPSelectView(Context context) {
        this(context, null);
    }

    public JPSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JPSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 设置当前数量
     *
     * @param count
     * @return
     */
    public JPSelectView setCount(int count) {
        mCount = count;
        //先暂停所有动画
        cancelAllAnim();

        //复用机制的处理

        initAnimSettings();
        return this;
    }

    /**
     * 暂停所有动画
     */
    private void cancelAllAnim() {
        if (mAnimAdd != null && mAnimAdd.isRunning()) {
            mAnimAdd.cancel();
        }
        if (mAniDel != null && mAniDel.isRunning()) {
            mAniDel.cancel();
        }
        if (mAnimExpandHint != null && mAnimExpandHint.isRunning()) {
            mAnimExpandHint.cancel();
        }
        if (mAnimReduceHint != null && mAnimReduceHint.isRunning()) {
            mAnimReduceHint.cancel();
        }
    }


    public int getMaxCount() {
        return mMaxCount;
    }


    public boolean isAddFillMode() {
        return isAddFillMode;
    }

    public JPSelectView setAddFillMode(boolean addFillMode) {
        isAddFillMode = addFillMode;
        return this;
    }

    public int getAddEnableBgColor() {
        return mAddEnableBgColor;
    }

    public JPSelectView setAddEnableBgColor(int addEnableBgColor) {
        mAddEnableBgColor = addEnableBgColor;
        return this;
    }

    public int getAddEnableFgColor() {
        return mAddEnableFgColor;
    }

    public JPSelectView setAddEnableFgColor(int addEnableFgColor) {
        mAddEnableFgColor = addEnableFgColor;
        return this;
    }

    public int getAddDisableBgColor() {
        return mAddDisableBgColor;
    }

    public JPSelectView setAddDisableBgColor(int addDisableBgColor) {
        mAddDisableBgColor = addDisableBgColor;
        return this;
    }

    public int getAddDisableFgColor() {
        return mAddDisableFgColor;
    }

    public JPSelectView setAddDisableFgColor(int addDisableFgColor) {
        mAddDisableFgColor = addDisableFgColor;
        return this;
    }

    public boolean isDelFillMode() {
        return isDelFillMode;
    }

    public JPSelectView setDelFillMode(boolean delFillMode) {
        isDelFillMode = delFillMode;
        return this;
    }

    public int getDelEnableBgColor() {
        return mDelEnableBgColor;
    }

    public JPSelectView setDelEnableBgColor(int delEnableBgColor) {
        mDelEnableBgColor = delEnableBgColor;
        return this;
    }

    public int getDelEnableFgColor() {
        return mDelEnableFgColor;
    }

    public JPSelectView setDelEnableFgColor(int delEnableFgColor) {
        mDelEnableFgColor = delEnableFgColor;
        return this;
    }

    public int getDelDisableBgColor() {
        return mDelDisableBgColor;
    }

    public JPSelectView setDelDisableBgColor(int delDisableBgColor) {
        mDelDisableBgColor = delDisableBgColor;
        return this;
    }

    public int getDelDisableFgColor() {
        return mDelDisableFgColor;
    }

    public JPSelectView setDelDisableFgColor(int delDisableFgColor) {
        mDelDisableFgColor = delDisableFgColor;
        return this;
    }

    public float getRadius() {
        return mRadius;
    }

    public JPSelectView setRadius(float radius) {
        mRadius = radius;
        return this;
    }

    public float getCircleWidth() {
        return mCircleWidth;
    }

    public JPSelectView setCircleWidth(float circleWidth) {
        mCircleWidth = circleWidth;
        return this;
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public JPSelectView setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
        return this;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public JPSelectView setTextSize(float textSize) {
        mTextSize = textSize;
        return this;
    }

    public float getGapBetweenCircle() {
        return mGapBetweenCircle;
    }

    public JPSelectView setGapBetweenCircle(float gapBetweenCircle) {
        mGapBetweenCircle = gapBetweenCircle;
        return this;
    }

    public int getPerAnimDuration() {
        return mPerAnimDuration;
    }

    public JPSelectView setPerAnimDuration(int perAnimDuration) {
        mPerAnimDuration = perAnimDuration;
        return this;
    }

    public boolean isIgnoreHintArea() {
        return ignoreHintArea;
    }

    public JPSelectView setIgnoreHintArea(boolean ignoreHintArea) {
        this.ignoreHintArea = ignoreHintArea;
        return this;
    }

    public int getHintBgColor() {
        return mHintBgColor;
    }

    public JPSelectView setHintBgColor(int hintBgColor) {
        mHintBgColor = hintBgColor;
        return this;
    }

    public int getHingTextSize() {
        return mHingTextSize;
    }

    public JPSelectView setHingTextSize(int hingTextSize) {
        mHingTextSize = hingTextSize;
        return this;
    }

    public String getHintText() {
        return mHintText;
    }

    public JPSelectView setHintText(String hintText) {
        mHintText = hintText;
        return this;
    }

    public int getHintFgColor() {
        return mHintFgColor;
    }

    public JPSelectView setHintFgColor(int hintFgColor) {
        mHintFgColor = hintFgColor;
        return this;
    }

    public int getHintBgRoundValue() {
        return mHintBgRoundValue;
    }

    public JPSelectView setHintBgRoundValue(int hintBgRoundValue) {
        mHintBgRoundValue = hintBgRoundValue;
        return this;
    }

    public int getCurrentType() {
        return currentType;
    }

    public JPSelectView setCurrentType(int currentType) {
        this.currentType = currentType;
        return this;
    }

    public String[] getStringList() {
        return stringList;
    }

    public int getCurrentPos() {
        return currentPos;
    }


    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {

        //模拟参数传入(设置初始值)
        initDefaultValue(context);
        //end

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JPSelectView, defStyleAttr, 0);
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = ta.getIndex(i);
            if (index == R.styleable.JPSelectView_selectViewType) {
                currentType = ta.getInteger(index, currentType);
                if (currentType == 0)
                    currentType = TYPE_NUMBER;
                else if (currentType == 1)
                    currentType = TYPE_STRING;
                else
                    currentType = TYPE_NUMBER;
            } else if (index == R.styleable.JPSelectView_gapBetweenCircle) {
                mGapBetweenCircle = ta.getDimension(index, mGapBetweenCircle);
            } else if (index == R.styleable.JPSelectView_isAddFillMode) {
                isAddFillMode = ta.getBoolean(index, isAddFillMode);
            } else if (index == R.styleable.JPSelectView_addEnableBgColor) {
                mAddEnableBgColor = ta.getColor(index, mAddEnableBgColor);
            } else if (index == R.styleable.JPSelectView_addEnableFgColor) {
                mAddEnableFgColor = ta.getColor(index, mAddEnableFgColor);
            } else if (index == R.styleable.JPSelectView_addDisableBgColor) {
                mAddDisableBgColor = ta.getColor(index, mAddDisableBgColor);
            } else if (index == R.styleable.JPSelectView_addDisableFgColor) {
                mAddDisableFgColor = ta.getColor(index, mAddDisableFgColor);
            } else if (index == R.styleable.JPSelectView_isDelFillMode) {
                isDelFillMode = ta.getBoolean(index, isDelFillMode);
            } else if (index == R.styleable.JPSelectView_subEnableBgColor) {
                mDelEnableBgColor = ta.getColor(index, mDelEnableBgColor);
            } else if (index == R.styleable.JPSelectView_subEnableFgColor) {
                mDelEnableFgColor = ta.getColor(index, mDelEnableFgColor);
            } else if (index == R.styleable.JPSelectView_subDisableBgColor) {
                mDelDisableBgColor = ta.getColor(index, mDelDisableBgColor);
            } else if (index == R.styleable.JPSelectView_subDisableFgColor) {
                mDelDisableFgColor = ta.getColor(index, mDelDisableFgColor);
            } else if (index == R.styleable.JPSelectView_maxCount) {
                mMaxCount = ta.getInteger(index, mMaxCount);
            } else if (index == R.styleable.JPSelectView_minCount) {
                mMinCount = ta.getInteger(index, mMinCount);
            } else if (index == R.styleable.JPSelectView_count) {
                mCount = ta.getInteger(index, mCount);
            } else if (index == R.styleable.JPSelectView_radius) {
                mRadius = ta.getDimension(index, mRadius);
            } else if (index == R.styleable.JPSelectView_circleStrokeWidth) {
                mCircleWidth = ta.getDimension(index, mCircleWidth);
            } else if (index == R.styleable.JPSelectView_lineWidth) {
                mLineWidth = ta.getDimension(index, mLineWidth);
            } else if (index == R.styleable.JPSelectView_numTextSize) {
                mTextSize = ta.getDimension(index, mTextSize);
            } else if (index == R.styleable.JPSelectView_hintText) {
                mHintText = ta.getString(index);
            } else if (index == R.styleable.JPSelectView_hintBgColor) {
                mHintBgColor = ta.getColor(index, mHintBgColor);
            } else if (index == R.styleable.JPSelectView_hintFgColor) {
                mHintFgColor = ta.getColor(index, mHintFgColor);
            } else if (index == R.styleable.JPSelectView_hintTextSize) {
                mHingTextSize = ta.getDimensionPixelSize(index, mHingTextSize);
            } else if (index == R.styleable.JPSelectView_hintBgRoundValue) {
                mHintBgRoundValue = ta.getDimensionPixelSize(index, mHintBgRoundValue);
            } else if (index == R.styleable.JPSelectView_ignoreHintArea) {
                ignoreHintArea = ta.getBoolean(index, false);
            } else if (index == R.styleable.JPSelectView_perAnimDuration) {
                mPerAnimDuration = ta.getInteger(index, DEFAULT_DURATION);
            }
        }
        ta.recycle();


        mAddRegion = new Region();
        mDelRegion = new Region();
        mAddPath = new Path();
        mDelPath = new Path();

        mAddPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isAddFillMode) {
            mAddPaint.setStyle(Paint.Style.FILL);
        } else {
            mAddPaint.setStyle(Paint.Style.STROKE);
        }
        mDelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isDelFillMode) {
            mDelPaint.setStyle(Paint.Style.FILL);
        } else {
            mDelPaint.setStyle(Paint.Style.STROKE);
        }

        mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHintPaint.setStyle(Paint.Style.FILL);
        mHintPaint.setTextSize(mHingTextSize);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mFontMetrics = mTextPaint.getFontMetrics();


        //动画 +
        mAnimAdd = ValueAnimator.ofFloat(1, 0);
        mAnimAdd.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimAdd.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
        mAnimAdd.setDuration(mPerAnimDuration);

        //提示语收缩动画 0-1
        mAnimReduceHint = ValueAnimator.ofFloat(0, 1);
        mAnimReduceHint.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimExpandHintFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimReduceHint.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ///改进：现在是：无论怎么样，只要点击了hint，就会进入选择
                isHintMode = false;
                if (mAnimAdd != null && !mAnimAdd.isRunning()) {
                    mAnimAdd.start();
                }
//                if (mCount >= 1) {
//                    //然后底色也不显示了
//                    isHintMode = false;
//                }
//                if (mCount >= 1) {
//                    Log.d(TAG, "现在还是》=1 开始收缩动画");
//                    if (mAnimAdd != null && !mAnimAdd.isRunning()) {
//                        mAnimAdd.start();
//                    }
//                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
//                if (mCount == 1) {
                //先不显示文字了
                isShowHintText = false;
//                }
            }
        });
        mAnimReduceHint.setDuration(ignoreHintArea ? 0 : mPerAnimDuration);


        //动画 -
        mAniDel = ValueAnimator.ofFloat(0, 1);
        mAniDel.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //1-0的动画
        mAniDel.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCount == 0) {
                    Log.d(TAG, "现在还是0onAnimationEnd() called with: animation = [" + animation + "]");
                    if (mAnimExpandHint != null && !mAnimExpandHint.isRunning()) {
                        mAnimExpandHint.start();
                    }
                }
            }
        });
        mAniDel.setDuration(mPerAnimDuration);
        //提示语展开动画
        //分析这个动画，最初是个圆。 就是left 不断减小
        mAnimExpandHint = ValueAnimator.ofFloat(1, 0);
        mAnimExpandHint.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimExpandHintFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimExpandHint.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCount == 0) {
                    isShowHintText = true;
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (mCount == 0) {
                    isHintMode = true;
                }
            }
        });
        mAnimExpandHint.setDuration(ignoreHintArea ? 0 : mPerAnimDuration);
    }

    /**
     * 设置初始值
     *
     * @param context
     */
    private void initDefaultValue(Context context) {
        this.currentType = TYPE_NUMBER;
        this.currentPos = -1;
        mGapBetweenCircle = mPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());

        isAddFillMode = true;
        mAddEnableBgColor = 0xFF03A9F4;
        mAddEnableFgColor = Color.WHITE;
        mAddDisableBgColor = Color.GRAY;
        mAddDisableFgColor = Color.WHITE;

        isDelFillMode = false;
        mDelEnableBgColor = 0xFF03A9F4;
        mDelEnableFgColor = 0xFF03A9F4;
        mDelDisableBgColor = Color.GRAY;
        mDelDisableFgColor = Color.GRAY;

        mMinCount = mMaxCount = 1;
        mCount = 0;
/*        mMaxCount = 4;
        mCount = 1;*/

        mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.5f, getResources().getDisplayMetrics());
        mCircleWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics());
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.5f, getResources().getDisplayMetrics());

        mHintText = "加入购物车";
        mHintBgColor = mAddEnableBgColor;
        mHintFgColor = mAddEnableFgColor;
        mHingTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources().getDisplayMetrics());
        mHintBgRoundValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (wMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                //不超过父控件给的范围内，自由发挥
                int computeSize = (int) (getPaddingLeft() + mRadius * 2 +/* mGap * 2 + mTextPaint.measureText(mCount + "")*/mGapBetweenCircle + mRadius * 2 + getPaddingRight() + mCircleWidth * 2);
                wSize = computeSize < wSize ? computeSize : wSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                //自由发挥
                computeSize = (int) (getPaddingLeft() + mRadius * 2 + /*mGap * 2 + mTextPaint.measureText(mCount + "")*/mGapBetweenCircle + mRadius * 2 + getPaddingRight() + mCircleWidth * 2);
                wSize = computeSize;
                break;
        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                int computeSize = (int) (getPaddingTop() + mRadius * 2 + getPaddingBottom() + mCircleWidth * 2);
                hSize = computeSize < hSize ? computeSize : hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                computeSize = (int) (getPaddingTop() + mRadius * 2 + getPaddingBottom() + mCircleWidth * 2);
                hSize = computeSize;
                break;
        }


        setMeasuredDimension(wSize, hSize);

        //先暂停所有动画
        cancelAllAnim();
        //复用时会走这里，所以初始化一些UI显示的参数
        initAnimSettings();
    }

    /**
     * 根据当前count数量 初始化 hint提示语相关变量
     */
    private void initAnimSettings() {
       if (this.currentType == TYPE_NUMBER){
           if (mCount == 0) {
               // 0 不显示 数字和-号
               mAnimFraction = 1;
           } else {
               mAnimFraction = 0;
           }

           if (mCount == 0) {
               isHintMode = true;
               isShowHintText = true;
               mAnimExpandHintFraction = 0;
           } else {
               isHintMode = false;
               isShowHintText = false;
               mAnimExpandHintFraction = 1;
           }
       }else{
           if (mCount == 0) {
               // 0 不显示 数字和-号
               mAnimFraction = 1;
           } else {
               mAnimFraction = 0;
           }

           if (mCount == 0) {
               isHintMode = true;
               isShowHintText = true;
               mAnimExpandHintFraction = 0;
           } else {
               isHintMode = false;
               isShowHintText = false;
               mAnimExpandHintFraction = 1;
           }
       }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeft = (int) (getPaddingLeft() + mCircleWidth);
        mTop = (int) (getPaddingTop() + mCircleWidth);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!ignoreHintArea && isHintMode) {
            //add hint 展开动画
            //if (mCount == 0) {
            //背景
            mHintPaint.setColor(mHintBgColor);
            RectF rectF = new RectF(mLeft + (mWidth - mRadius * 2) * mAnimExpandHintFraction, mTop
                    , mWidth - mCircleWidth, mHeight - mCircleWidth);
            canvas.drawRoundRect(rectF, mHintBgRoundValue, mHintBgRoundValue, mHintPaint);
            if (isShowHintText) {
                //前景文字
                mHintPaint.setColor(mHintFgColor);
                // 计算Baseline绘制的起点X轴坐标
                int baseX = (int) (mWidth / 2 - mHintPaint.measureText(mHintText) / 2);
                // 计算Baseline绘制的Y坐标
                int baseY = (int) ((mHeight / 2) - ((mHintPaint.descent() + mHintPaint.ascent()) / 2));
                canvas.drawText(mHintText, baseX, baseY, mHintPaint);
            }
            //}
        } else {

            //动画 mAnimFraction ：减 0~1, 加 1~0 ,
            //动画位移Max,

            if (currentType == TYPE_NUMBER)
                mGapBetweenCircle = mPadding + mTextPaint.measureText(mCount+"");
            else
                mGapBetweenCircle = mPadding + mTextPaint.measureText(stringList[currentPos]);

            float animOffsetMax = (mRadius * 2 + /*mGap * 2 + mTextPaint.measureText(mCount + "")*/mGapBetweenCircle);
            //透明度动画的基准
            int animAlphaMax = 255;
            int animRotateMax = 360;

            //左边
            //背景 圆
            if (currentType == TYPE_NUMBER) {
                if (mCount > mMinCount) {
                    mDelPaint.setColor(mDelEnableBgColor);
                } else {
                    mDelPaint.setColor(mDelDisableBgColor);
                }
            } else {
                if (currentPos > 0) {
                    mDelPaint.setColor(mDelEnableBgColor);
                } else {
                    mDelPaint.setColor(mDelDisableBgColor);
                }
            }

            mDelPaint.setAlpha((int) (animAlphaMax * (1 - mAnimFraction)));

            mDelPaint.setStrokeWidth(mCircleWidth);
            mDelPath.reset();
            //改变圆心的X坐标，实现位移
            mDelPath.addCircle(animOffsetMax * mAnimFraction + mLeft + mRadius, mTop + mRadius, mRadius, Path.Direction.CW);
            mDelRegion.setPath(mDelPath, new Region(mLeft, mTop, mWidth - getPaddingRight(), mHeight - getPaddingBottom()));
            //canvas.drawCircle(mAnimOffset + mLeft + mRadius, mTop + mRadius, mRadius, mPaint);
            canvas.drawPath(mDelPath, mDelPaint);

            //前景 -
            if (currentType == TYPE_NUMBER) {
                if (mCount > mMinCount) {
                    mDelPaint.setColor(mDelEnableFgColor);
                } else {
                    mDelPaint.setColor(mDelDisableFgColor);
                }
            } else {
                if (currentPos > 0) {
                    mDelPaint.setColor(mDelEnableFgColor);
                } else {
                    mDelPaint.setColor(mDelDisableFgColor);
                }
            }
            mDelPaint.setStrokeWidth(mLineWidth);
            //旋转动画
            canvas.save();
            canvas.translate(animOffsetMax * mAnimFraction + mLeft + mRadius, mTop + mRadius);
            canvas.rotate((int) (animRotateMax * (1 - mAnimFraction)));
        /*canvas.drawLine(mAnimOffset + mLeft + mRadius / 2, mTop + mRadius,
                mAnimOffset + mLeft + mRadius / 2 + mRadius, mTop + mRadius,
                mPaint);*/
            if (currentType == TYPE_NUMBER) {
                canvas.drawLine(-mRadius / 2, 0,
                        +mRadius / 2, 0,
                        mDelPaint);

            } else {
                canvas.drawLine(-mRadius * 2 / 3, 0,
                        +mRadius / 3, +mRadius * 2 / 3,
                        mDelPaint);
                canvas.drawLine(-mRadius * 2 / 3, 0,
                        +mRadius / 3, -mRadius * 2 / 3,
                        mDelPaint);

            }
            canvas.restore();


            //数量
            canvas.save();
            //平移动画
            if (currentType == TYPE_NUMBER)
                canvas.translate(mAnimFraction * (/*mGap*/mGapBetweenCircle / 2 - mTextPaint.measureText(mCount + "") / 2 + mRadius), 0);
            else
                canvas.translate(mAnimFraction * (/*mGap*/mGapBetweenCircle / 2 - mTextPaint.measureText(stringList[currentPos]) / 2 + mRadius), 0);

            //旋转动画,旋转中心点，x 是绘图中心,y 是控件中心
            canvas.rotate(360 * mAnimFraction,
                /*mGap*/ mGapBetweenCircle / 2 + mLeft + mRadius * 2 /*+ mTextPaint.measureText(mCount + "") / 2*/,
                    mTop + mRadius);
            //透明度动画
            mTextPaint.setAlpha((int) (255 * (1 - mAnimFraction)));
            //是没有动画的普通写法,x left, y baseLine
            if (currentType == TYPE_NUMBER)
                canvas.drawText(mCount + "", /*mGap*/ mGapBetweenCircle / 2 - mTextPaint.measureText(mCount + "") / 2 + mLeft + mRadius * 2, mTop + mRadius - (mFontMetrics.top + mFontMetrics.bottom) / 2, mTextPaint);
            else
                canvas.drawText(stringList[currentPos], /*mGap*/ mGapBetweenCircle / 2 - mTextPaint.measureText(stringList[currentPos]) / 2 + mLeft + mRadius * 2, mTop + mRadius - (mFontMetrics.top + mFontMetrics.bottom) / 2, mTextPaint);

            canvas.restore();

            //右边
            //背景 圆
            if (currentType == TYPE_NUMBER) {
                if (mCount < mMaxCount) {
                    mAddPaint.setColor(mAddEnableBgColor);
                } else {
                    mAddPaint.setColor(mAddDisableBgColor);
                }
            } else {
                if (currentPos < stringList.length - 1) {
                    mAddPaint.setColor(mAddEnableBgColor);
                } else {
                    mAddPaint.setColor(mAddDisableBgColor);
                }
            }

            mAddPaint.setStrokeWidth(mCircleWidth);
            float left = mLeft + mRadius * 2 + /*mGap * 2 + mTextPaint.measureText(mCount + "")*/ mGapBetweenCircle;
            mAddPath.reset();
            mAddPath.addCircle(left + mRadius, mTop + mRadius, mRadius, Path.Direction.CW);
            mAddRegion.setPath(mAddPath, new Region(mLeft, mTop, mWidth - getPaddingRight(), mHeight - getPaddingBottom()));
            //canvas.drawCircle(left + mRadius, mTop + mRadius, mRadius, mPaint);
            canvas.drawPath(mAddPath, mAddPaint);
            //前景 +
            if (this.currentType == TYPE_NUMBER) {
                if (mCount < mMaxCount) {
                    mAddPaint.setColor(mAddEnableFgColor);
                } else {
                    mAddPaint.setColor(mAddDisableFgColor);
                }
            } else {
                if (currentPos < stringList.length - 1) {
                    mAddPaint.setColor(mAddEnableFgColor);
                } else {
                    mAddPaint.setColor(mAddDisableFgColor);
                }
            }

            mAddPaint.setStrokeWidth(mLineWidth);
            if (this.currentType == TYPE_NUMBER) {
                canvas.drawLine(left + mRadius / 2, mTop + mRadius, left + mRadius / 2 + mRadius, mTop + mRadius, mAddPaint);
                canvas.drawLine(left + mRadius, mTop + mRadius / 2, left + mRadius, mTop + mRadius / 2 + mRadius, mAddPaint);
            } else {
                canvas.drawLine(left + mRadius * 2 / 3, mTop + mRadius / 3, left + mRadius * 2 / 3 + mRadius, mTop + mRadius, mAddPaint);
                canvas.drawLine(left + mRadius * 2 / 3, mTop + mRadius + mRadius * 2 / 3, left + mRadius * 2 / 3 + mRadius, mTop + mRadius, mAddPaint);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //hint文字模式
                if (isHintMode) {
                    onAddClick();
                    return true;
                } else {
                    if (mAddRegion.contains((int) event.getX(), (int) event.getY())) {
                        onAddClick();
                        return true;
                    } else if (mDelRegion.contains((int) event.getX(), (int) event.getY())) {
                        onDelClick();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }


        return super.onTouchEvent(event);

    }

    //左键监听
    protected void onDelClick() {
        switch (this.currentType) {
            case TYPE_NUMBER:
                if (this.mCount > this.mMinCount) {
                    --this.mCount;
                    this.onCountDelSuccess();
                    if (null != this.mOnAddSubNumberListener) {
                        this.mOnAddSubNumberListener.onfinishSub(this.mCount);
                    }
                }
                break;
            case TYPE_STRING:
                if (this.currentPos > 0) {
                    --this.currentPos;
                    this.onCountDelSuccess();
                    if (null != this.mOnChangeStringListener) {
                        this.mOnChangeStringListener.onFinishChangeString(this.stringList[this.currentPos]);
                    }
                }
                break;
        }
    }

    ///右键监听
    protected void onAddClick() {
        switch (this.currentType) {
            case TYPE_NUMBER:
                if (this.mCount < this.mMaxCount) {
                    ++this.mCount;
                    this.onCountAddSuccess();
                    if (null != this.mOnAddSubNumberListener) {
                        this.mOnAddSubNumberListener.onfinishAdd(this.mCount);
                    }
                }
                break;
            case TYPE_STRING:
                if (this.currentPos < this.stringList.length - 1) {
                    ++this.mCount;
                    ++this.currentPos;
                    this.onCountAddSuccess();
                    if (null != this.mOnChangeStringListener) {
                        this.mOnChangeStringListener.onFinishChangeString(this.stringList[this.currentPos]);
                    }
                }
                break;
        }
    }


    public void onCountAddSuccess() {
        switch (this.currentType) {
            case TYPE_NUMBER:
                if (this.mCount == 1) {
                    this.cancelAllAnim();
                    this.mAnimReduceHint.start();
                } else {
                    this.mAnimFraction = 0.0F;
                    this.invalidate();
                }
//                现在变成：不会再变回hint界面了
//                this.cancelAllAnim();
//                this.mAnimReduceHint.start();
                break;
            case TYPE_STRING:
                if (this.currentPos == 0) {
                    this.cancelAllAnim();
                    this.mAnimReduceHint.start();
                } else {
                    this.mAnimFraction = 0.0F;
                    this.invalidate();
                }
                break;
        }
    }

    public void onCountDelSuccess() {
        switch (this.currentType) {
            case TYPE_NUMBER:
//                if (this.mCount == 0) {
//                    this.cancelAllAnim();
//                    this.mAniDel.start();
//                } else {
                this.mAnimFraction = 0.0F;
                this.invalidate();
//                }
                break;
            case TYPE_STRING:
//                if (this.currentPos == 0) {
//                    this.cancelAllAnim();
//                    this.mAniDel.start();
//                } else {
                this.mAnimFraction = 0.0F;
                this.invalidate();
//                }
                break;
        }

    }


    //---------------------------------------
    // setListener()
    //---------------------------------------
    public JPSelectView setOnAddSubClickListener(OnAddSubNumberListener listener) {
        this.mOnAddSubNumberListener = listener;
        return this;
    }

    public JPSelectView setOnChangeStringListener(OnChangeStringListener listener) {
        this.mOnChangeStringListener = listener;
        return this;
    }


    //---------------------------------------
    // 监听器
    //---------------------------------------
    public interface OnAddSubNumberListener {
        void onfinishAdd(int count);//"+"键监听

        void onfinishSub(int count);//"-"键监听
    }

    public interface OnChangeStringListener {
        void onFinishChangeString(String value);///重新选择了String Item之后
    }

    public interface OnHintClickListener {
        void onHintClick();
    }

    //-----------------------------------------
    // getter and setter
    //-----------------------------------------

    /**
     * 获取当前数字
     */
    public int getCount() {
        return this.mCount;
    }

    /**
     * 设置当前数组position
     */
    public JPSelectView setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        return this;
    }

    /**
     * 设置要显示的数据数组
     */
    public JPSelectView setStringList(String[] stringList) {
        this.stringList = stringList;
        return this;
    }

    /**
     * 设置数字左区间
     */
    public JPSelectView setMinCount(int mMinCount) {
        this.mMinCount = mMinCount;
        return this;
    }

    /**
     * 设置数字右区间
     */
    public JPSelectView setMaxCount(int maxCount) {
        this.mMaxCount = maxCount;
        return this;
    }

    /**
     * 设置此控件的显示类型（数字/字符串）
     */
    public JPSelectView setType(int type) {
        this.currentType = type;
        if (this.currentType != TYPE_NUMBER && this.currentType != TYPE_STRING) {
            this.currentType = TYPE_NUMBER;
        }
        return this;
    }
}
