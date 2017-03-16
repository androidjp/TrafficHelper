package com.androidjp.lib_custom_view.edittext;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.androidjp.lib_common_util.ui.DisplayUtil;
import com.androidjp.lib_custom_view.LogView;
import com.androidjp.lib_custom_view.R;

/**
 * 多用法的密码输入框（仿微信）
 * Created by androidjp on 16-7-27.
 */
public class JPwdInputView extends LogView {
    private InputMethodManager inputMethod;//输入法管理器
    private InputCallback inputCallback;///输入完成时的回调接口

    private StringBuffer result;//输入结果保存

    private final int DEFAULT_COUNT = 6;//数量默认6个格
    private final int DEFAUlT_SIZE = 36;//dp（这样一算，默认的宽高为（216dp， 36dp））
    private final float DEFAUlT_PERSENTAGE = (float)0.5;//默认占方格比重

    private int count;//输入框的数量
    private int size;///输入框的尺寸
    private Paint mBorderPaint;//边界画笔
    private Paint mDotPaint;//遮掩点画笔（隐藏信息）
    private Paint mNumbarPaint;//画笔（显示信息）
    private RectF mRoundRect;// 外面的圆角矩形
    private float mRoundRedius;//圆角矩形的圆角程度
    private float contentPersentage;//输入的内容所占方格的百分比（0~1）

    /**
     * 一个控制变量，是否显示输入的内容
     */
    private boolean isShow = false;
    private boolean isJustNumber = false;


    /**
     * View提供外部的接口回调
     */
    public interface InputCallback {
        /**
         * @param result 输入完成后，把结果作为参数返回
         */
        void onInputFinish(String result);
    }


    /**
     * 构造方法
     * @param context
     */
    public JPwdInputView(Context context) {
        this(context, null);
    }

    public JPwdInputView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public JPwdInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta =  context.obtainStyledAttributes(attrs, R.styleable.JPwdInputView);
        init((Activity) context , ta);
        ta.recycle();
    }


    /**
     * 初始化相关参数
     */
    private void init(Activity activity , TypedArray ta) {
        this.setFocusable(true);///设置可获取焦点
        this.setFocusableInTouchMode(true);//设置可以通过点击事件获取焦点
        //初始化输入法管理器
        inputMethod = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //初始化输入结果
        result = new StringBuffer();
        //初始化方格的数量和尺寸
        count = ta.getInt(R.styleable.JPwdInputView_inputCount,DEFAULT_COUNT);//默认6位密码
        size = DisplayUtil.dip2px(DEFAUlT_SIZE, activity);
        contentPersentage = ta.getFloat(R.styleable.JPwdInputView_contentSizePersentage,(float) DEFAUlT_PERSENTAGE);///默认占方格的0.5
        ///初始化Paint
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(ta.getColor(R.styleable.JPwdInputView_borderColor,Color.GRAY));
        mBorderPaint.setStrokeWidth(ta.getFloat(R.styleable.JPwdInputView_borderWidth,(float)DisplayUtil.dip2px(4,activity)));
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(ta.getColor(R.styleable.JPwdInputView_dotColor,Color.GRAY));
        mDotPaint.setStyle(Paint.Style.FILL);

        mNumbarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumbarPaint.setColor(ta.getColor(R.styleable.JPwdInputView_contentColor,Color.GRAY));
        mNumbarPaint.setStyle(Paint.Style.FILL);

        mRoundRect = new RectF();
        mRoundRedius = ta.getDimension(R.styleable.JPwdInputView_radiuSize,DisplayUtil.dip2px(6, activity));

        isJustNumber = ta.getBoolean(R.styleable.JPwdInputView_justNumber,false);
        isShow = ta.getBoolean(R.styleable.JPwdInputView_showContent,false);

        setOnKeyListener(new MyKeyListener());
    }


    /**
     * 测量和设置View的布局
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        //首先，给它默认的宽和高
        int w;
        int h = size;

        //然后，通过父容器，获取它的测量宽高
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * ①先把宽初定下来
         */
        switch (wmode) {
            case MeasureSpec.UNSPECIFIED:
                //这种情况，表示：1、父容器match_parent+自身match_parent/wrap_content , 此时的 wsize= 0;
                //这时，不管取什么值，都可以，所以，w 先取默认值
                break;
            case MeasureSpec.EXACTLY://自身确定值或match_parent
                //这种情况下：一般子View已经设定好了width 或者 子View设置了match_parent，达到父容器的wsize，所以，w = 测量的wsize
                size = wsize/6;
                break;
            case MeasureSpec.AT_MOST:
                //此时，wsize 都是父容器的宽（父容器wrap_content+自身match_parent/wrap_content  , 父容器固定值+自身wrap_content）
                size = Math.min(size,wsize/6);
                break;
        }

        w = size*6;

        switch (hmode) {

            case MeasureSpec.EXACTLY:
                //高已经固定，那就只能去它的测量值了
                h = hsize;
                break;
            case MeasureSpec.AT_MOST:
                //这个时候，父容器会测量它，最大的高会是父容器剩余空间的最大高，所以，这里，判断高和 w/6哪个更小，取更小那个
                h  = Math.min(size, hsize);
                break;
            case MeasureSpec.UNSPECIFIED:
                //这里，因为高可以取任何值，所以,看宽而定
                h = size;
                break;
        }

        ///最终把测量好的数据提交
        setMeasuredDimension(w, h);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            requestFocus();
            inputMethod.showSoftInput(this, InputMethodManager.SHOW_FORCED);///强制弹出软键盘
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            ///如果获取了焦点
            inputMethod.showSoftInput(this, InputMethodManager.SHOW_FORCED);
        } else {
            inputMethod.hideSoftInputFromInputMethod(this.getWindowToken(), 0);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            inputMethod.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() - 2;
        int height = getHeight() - 2;
        //1.圆角矩形
        mRoundRect.set(0, 0, width, height);
        canvas.drawRoundRect(mRoundRect, mRoundRedius, mRoundRedius, mBorderPaint);
        //2.分割线
        for (int i = 1; i < count; i++) {
            int x = i * size;
            canvas.drawLine(x, 0, x, height, mBorderPaint);
        }

        //3. 输入的文本
        float dotRadius = (float)( size* contentPersentage*0.36); ///设置半径是每个方格边长的1/6，也就是每个点占方格边长的1/3
        if (!isShow) {
            for (int i = 0; i < result.length(); i++) {
                final float radiuX = (float) (size * (i + 0.5));///“0.5”是为了找到每个格子的中点
                final float radiuY = (float) ((size * 1.0) / 2);
                canvas.drawCircle(radiuX, radiuY, dotRadius, mDotPaint);
            }
        } else {///注意点：1、Paint在绘制文本时，是以左下角为基准的
//            mNumbarPaint.setTextSize((float) (size*0.6));
//            for (int i = 0; i < result.length(); i++) {
//                final float radiuX = (float) (size*(i+0.25));
//                final float radiuY = (float) (size*0.75);
//                canvas.drawText(result.substring(i, i + 1), radiuX, radiuY, mNumbarPaint);
//            }
            ///基准点的位置取决于Paint设置的align设置
            mNumbarPaint.setTextAlign(Paint.Align.CENTER);
            mNumbarPaint.setTextSize((float) (size*contentPersentage));
            for (int i=0;i<result.length();i++){
                final float textX = (float) (size*(i+0.5));
                final float textY = (float) (size*0.666);
                canvas.drawText(result.substring(i,i+1),textX,textY,mNumbarPaint);
            }
        }

    }

    /**
     * 判断为是一个文本编辑器
     *
     * @return true
     */
    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {


        if (isJustNumber) {
            outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        } else {
            outAttrs.inputType = InputType.TYPE_CLASS_TEXT;
        }
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new MyInputConnection(this, false);
    }

    //----------------------------------------------------
    //内部方法
    //----------------------------------------------------


    /**
     * 按键监听器（key，表示硬件按钮的按键监听）
     */
    private class MyKeyListener implements OnKeyListener {

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (result.length() > 0) {
                        result.deleteCharAt(result.length() - 1);
                        invalidate();
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    ensureFinishInput();
                    return true;
                } else if ((keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)) {
                    if (result.length() < count) {
                        result.append("" + (keyCode - 7));
                        invalidate();
                        ensureFinishInput();
                    }
                    return true;
                }

            }
            return false;
        }
    }


    /***
     * 判断是否输入完毕，一旦输入完毕，回调接口
     */
    private void ensureFinishInput() {
        if (result.length() >= count && inputCallback != null) {
            inputCallback.onInputFinish(result.substring(0,result.length()));
        }
    }


    class MyInputConnection extends BaseInputConnection {

        public MyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            ///这里，如果是不接受文本输入，那么，什么都不做
            if (!isJustNumber) {
//                Log.d("InputView", "MyInputConnection.commitText():输入：" + text);
                String str = text.toString();
                if (!str.matches("^[A-Za-z0-9]+$")){
                    Toast.makeText(JPwdInputView.this.getContext(),"不能输入非数字和字母的字符",Toast.LENGTH_SHORT).show();
                    return false;
                }
                result.append(str.trim());
                if (result.length()>count)
                    result.delete(count,result.length());
                postInvalidate();
                ensureFinishInput();
            }
            return true;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //软键盘的删除键 DEL ，由于无法直接监听，所以自己发送del事件
            if (beforeLength == 1 && afterLength == 0) {
                ///按下和松开“删除”键的操作都完成了，才发送一个true出来
                return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }


    //====================================================================


    /**
     * @param isShow 是否显示输入内容
     * @return JPwdInputView
     */
    public JPwdInputView setContentShow(boolean isShow) {
        this.isShow = isShow;
        return this;
    }

    /**
     * @param justNumber 是否只能输入数字
     * @return JPwdInputView
     */
    public JPwdInputView setJustNumber(boolean justNumber) {
        this.isJustNumber = justNumber;
        return this;
    }

    /**
     * @param inputCallback 设置接口
     */
    public JPwdInputView setInputCallback(InputCallback inputCallback) {
        this.inputCallback = inputCallback;
        return this;
    }

    /**
     * 清空输入
     */
    public JPwdInputView clearResult() {
        result.delete(0, result.length());
        invalidate();
        return this;
    }


    /**
     * 设置内容颜色
     * @param colorResId
     */
    public JPwdInputView setContentColor(int colorResId){
        this.mNumbarPaint.setColor(getResources().getColor(colorResId));
        return this;
    }
    /**
     * 设置点颜色
     * @param colorResId
     */
    public JPwdInputView setDotColor(int colorResId){
        this.mDotPaint.setColor(getResources().getColor(colorResId));
        return this;
    }

    /**
     * 设置边框颜色
     * @param colorResId
     */
    public JPwdInputView setBorderColor(int colorResId){
        this.mBorderPaint.setColor(getResources().getColor(colorResId));
        return this;
    }



    /**
     * 设置内容占方格的百分比
     * @param persentage
     */
    public JPwdInputView setContentPersentage(float persentage){
        this.contentPersentage = persentage;
        return this;
    }

    /**
     * 设置边框厚度
     * @param dp
     */
    public JPwdInputView setStrokeWidth(int dp){
        this.mBorderPaint.setStrokeWidth(DisplayUtil.dip2px(dp,(Activity)this.getContext()));
        return this;
    }

    /**
     * 设置文本的厚度
     * @param isBlod
     */
    public JPwdInputView setContentBlod(boolean isBlod){
        if(isBlod){
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            this.mNumbarPaint.setTypeface(font);
        }else{
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
            this.mNumbarPaint.setTypeface(font);
        }
        return this;
    }

    /**
     * 设置圆角的dp值
     * @param dp
     */
    public JPwdInputView setRadiuSize(int dp){
        this.mRoundRedius = DisplayUtil.dip2px((float) dp,(Activity) this.getContext());
        return this;
    }


}
