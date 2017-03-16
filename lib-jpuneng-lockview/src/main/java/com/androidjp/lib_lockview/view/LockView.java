package com.androidjp.lib_lockview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.androidjp.lib_lockview.enums.LockMode;
import com.androidjp.lib_lockview.LockViewApi;
import com.androidjp.lib_lockview.R;
import com.androidjp.lib_lockview.bean.Point;
import com.androidjp.lib_lockview.encrypt.Base64;
import com.androidjp.lib_lockview.listener.OnCompleteListener;
import com.androidjp.lib_lockview.util.ConfigUtil;
import com.androidjp.lib_lockview.util.LockUtil;
import com.androidjp.lib_lockview.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**九宫格View
 * Created by androidjp on 16/9/11.
 */
public class LockView extends View implements LockViewApi{

    //保存密码key
    private String saveLockKey =null;
    //是否保存保存PIN
    private boolean isSavePin = false;
    //是否已缓存
    private boolean isCache = false;
    //当前是否正在绘制并且没有到达下一个点
    private boolean movingNoPoint = false;
    //控件宽度和高度
    private float width = 0,height = 0;
    //画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //9个点
    private Point[][] pointMap = new Point[3][3];
    //选中的点集合
    private List<Point> seletedList = new ArrayList<>();

    //当前手势的x、y坐标
    float movingX, movingY;

    //密码最小长度
    private int passwordMinLength = 3;
    //判断是否触摸屏幕
    private boolean checking = false;
    //刷新
    private TimerTask task = null;
    //计时器
    private Timer timer = new Timer();
    //监听
    private OnCompleteListener mCompleteListener;
    //清除痕迹的时间
    private long CLEAR_TIME = 100;
    //错误限制 默认为4次
    private int errorNumber = 4;
    //记录上一次滑动的密码
    private String oldPassword = null;
    //记录当前第几次触发 默认为0次
    private int showTimes = 0;
    //当前密码是否正确 默认为正确
    private boolean isCorrect = true;
    //是否显示滑动方向 默认为显示
    private boolean isShow = true;
    //验证或者设置 0:设置 1:验证
    private LockMode mode = LockMode.SETTING_PASSWORD;
    //用于执行清除界面
    private Handler handler = new Handler();
    //间距
    float roundW;
    //普通状态下圈的颜色
    private int mColorUpRing = 0xFF378FC9;
    //按下时圈的颜色
    private int mColorOnRing = 0xFF378FC9;
    //松开手时的颜色
    private int mColorErrorRing = 0xFF378FC9;

    //外圈大小
    private float mOuterRingWidth = 120;
    //内圆大小
    private float mInnerRingWidth = mOuterRingWidth / 3;
    //内圆间距
    private float mCircleSpacing;
    //圆圈半径
    private float mRadius;
    //小圆半径
    private float mInnerRingRadius;
    //小圆半透明背景半径
    private float mInnerBackgroudRadius;
    //内圆背景大小（半透明内圆）
    private float mInnerBackgroudWidth;
    //三角形边长
    private float mArrowLength;
    //未按下时圆圈的边宽
    private int mNoTouchWidth = 2;
    //按下时圆圈的边宽
    private int mOnStrokeWidth = 4;
    //编辑密码前是否验证
    private boolean isEditVerify = false;
    //是否立即清除密码
    private boolean isClearPasssword = true;

    //屏幕尺寸相关
    private float screenW;
    private float screenH;

    //x和y的偏移值
    private float offsetX,offsetY;


    //用于定时执行清除界面
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(run);
            reset();
            postInvalidate();
        }
    };




    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化自定义属性
        initCustomAttr(context,attrs,defStyleAttr);
    }






    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = ViewUtils.measure(widthMeasureSpec);
        height = ViewUtils.measure(heightMeasureSpec);
        width = Math.min(width,height);

        if (width > height){
            width = height;
        }else{
            height = width;
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isCache) {
            initCache();
        }
        //绘制圆 以及 显示当前状态
        drawToCanvas(canvas);
    }



    /**
     * ==========================================
     * 内部方法
     * ==========================================
     */




    //图像绘制
    private void drawToCanvas(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        ///画线
        drawAllLine(canvas);
        //画点
        drawAllPoint(canvas);
        //是否绘制方向图标
        if (isShow){
            drawDirectionArrow(canvas);
        }
    }

    ///绘制方向图标(三角形绘制)
    private void drawDirectionArrow(Canvas canvas) {
        // 绘制方向图标
        if (seletedList.size() <= 0) {
            return;
        }
        Point tp = seletedList.get(0);
        for (int i = 1; i < seletedList.size(); i++) {
            //根据移动的方向绘制方向图标
            Point p = seletedList.get(i);
            if (p.state == Point.STATE_ERROR) {
                drawDirectionArrow(canvas, tp, p, mColorErrorRing);
            } else {
                drawDirectionArrow(canvas, tp, p, mColorOnRing);
            }
            tp = p;
        }
    }

    private void drawDirectionArrow(Canvas canvas,Point a, Point b, int color){
        //获取角度
        float degrees = LockUtil.getDegrees(a, b) + 90;
        //根据两点方向旋转
        canvas.rotate(degrees, a.x, a.y);
        drawArrow(canvas, a, color);
        //旋转方向
        canvas.rotate(-degrees, a.x, a.y);
    }


    /**
     * 绘制三角形指示标
     *
     * @param canvas
     * @param a
     * @param color
     */
    private void drawArrow(Canvas canvas, Point a, int color) {
        // 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        Path mArrowPath = new Path();
        float offset = mInnerBackgroudRadius + (mArrowLength + mRadius - mInnerBackgroudRadius) / 2;//偏移量,定位三角形位置
        mArrowPath.moveTo(a.x, a.y - offset);
        mArrowPath.lineTo(a.x - mArrowLength, a.y - offset
                + mArrowLength);
        mArrowPath.lineTo(a.x + mArrowLength, a.y - offset
                + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        canvas.drawPath(mArrowPath, mPaint);
    }

    ///画所有点
    private void drawAllPoint(Canvas canvas) {
        for (int i = 0; i < pointMap.length; i++) {
            for (int j = 0; j < pointMap[i].length; j++) {
                Point p = pointMap[i][j];
                if (p != null) {
                    if (p.state == Point.STATE_PRESSED) {
                        onDrawOn(canvas, p);
                    } else if (p.state == Point.STATE_ERROR) {
                        onDrawError(canvas, p);
                    } else {
                        onDrawNoFinger(canvas, p);
                    }
                }
            }
        }
    }

    //<editor-fold desc="绘制不同状态下的圆">

    /**
     * 绘制按下时状态
     *
     * @param canvas
     */
    private void onDrawOn(Canvas canvas, Point p) {
        // 绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(p.x, p.y, mRadius, mPaint);
        // 绘制外圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColorOnRing);
        mPaint.setStrokeWidth(mOnStrokeWidth);
        canvas.drawCircle(p.x, p.y, mRadius, mPaint);
        // 绘制内圆背景
        onDrawInnerCircleBackground(canvas, p, mColorOnRing);
        // 绘制内圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColorOnRing);
        canvas.drawCircle(p.x, p.y, mInnerRingRadius, mPaint);
    }


    /**
     * 绘制松开手时状态
     *
     * @param canvas
     */
    private void onDrawError(Canvas canvas, Point p) {
        // 绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(p.x, p.y, mRadius, mPaint);
        // 绘制圆圈
        mPaint.setColor(mColorErrorRing);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mOnStrokeWidth);
        canvas.drawCircle(p.x, p.y, mRadius, mPaint);
        // 绘制内圆背景
        onDrawInnerCircleBackground(canvas, p, mColorErrorRing);
        // 绘制内圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColorErrorRing);
        canvas.drawCircle(p.x, p.y, mInnerRingRadius, mPaint);
    }


    /**
     * 绘制普通状态
     *
     * @param canvas
     */
    private void onDrawNoFinger(Canvas canvas, Point p) {
        // 绘制外圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColorUpRing);
        mPaint.setStrokeWidth(mNoTouchWidth);
        canvas.drawCircle(p.x, p.y, mRadius, mPaint);
    }

    /**
     * 绘制内圆透明背景
     *
     * @param canvas
     */
    private void onDrawInnerCircleBackground(Canvas canvas, Point p, int color) {
        // 绘制内圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        mPaint.setAlpha(100);
        canvas.drawCircle(p.x, p.y, mInnerBackgroudRadius, mPaint);
    }
    //</editor-fold>

    //画所有线
    private void drawAllLine(Canvas canvas) {
        if (seletedList.size()>0){
            Point firstP = seletedList.get(0);
            for (int i=1;i<seletedList.size();i++){
                Point p = seletedList.get(i);
                if (p.state == Point.STATE_ERROR){
                    drawErrorLine(canvas, firstP,p);
                }else{
                    drawLine(canvas, firstP, p);
                }
                firstP = p;
            }
            if (this.movingNoPoint){
                ///到达下一个点停止移动绘制固定方向
                drawLine(canvas, firstP, new Point((int)movingX +20 , (int)movingY));
            }
        }
    }

    //<editor-fold desc="根据点的状态,画不同颜色的线">
    ///画错线
    private void drawErrorLine(Canvas canvas, Point firstP, Point p) {
        mPaint.setColor(mColorErrorRing);
        mPaint.setStrokeWidth(4);
        canvas.drawLine(firstP.x, firstP.y, p.x, p.y, mPaint);
    }
    ///画正常线(手势滑动中)
    private void drawLine(Canvas canvas, Point firstP, Point p) {
        mPaint.setColor(mColorOnRing);
        mPaint.setStrokeWidth(4);
        canvas.drawLine(firstP.x, firstP.y, p.x, p.y, mPaint);
    }
    //</editor-fold>


    //初始化自定义属性
    private void initCustomAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LockView_styleable, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.LockView_styleable_color_on_ring) {
                mColorOnRing = a.getColor(attr, mColorOnRing);
            } else if (attr == R.styleable.LockView_styleable_color_up_ring) {
                mColorUpRing = a.getColor(attr, mColorUpRing);
            } else if (attr == R.styleable.LockView_styleable_color_error_ring) {
                mColorErrorRing = a.getColor(attr, mColorErrorRing);
            } else if (attr == R.styleable.LockView_styleable_inner_ring_width) {
                mInnerRingWidth = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.LockView_styleable_outer_ring_spacing_width) {
                mCircleSpacing = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.LockView_styleable_inner_background_width) {
                mInnerBackgroudWidth = a.getDimensionPixelSize(attr, 0);
            }
        }
        a.recycle();
    }



    ///重置九宫格
    private void reset() {
        for(Point p: seletedList){
            p.state = Point.STATE_NORMAL;
        }
        seletedList.clear();
    }


    /**
     * 初始化Cache信息
     *
     */
    private void initCache() {
        float y = 0;
        initGestureLockViewWidth();
        // 计算圆圈的大小及位置
        roundW = width - (mOuterRingWidth * 3);
        roundW = roundW / 4 + mOuterRingWidth / 2;
        pointMap[0][0] = new Point(getX(0), y + roundW);
        pointMap[0][1] = new Point(getX(1), y + roundW);
        pointMap[0][2] = new Point(getX(2), y + roundW);
        pointMap[1][0] = new Point(getX(0), y + height / 2);
        pointMap[1][1] = new Point(getX(1), y + height / 2);
        pointMap[1][2] = new Point(getX(2), y + height / 2);
        pointMap[2][0] = new Point(getX(0), y + height - roundW);
        pointMap[2][1] = new Point(getX(1), y + height - roundW);
        pointMap[2][2] = new Point(getX(2), y + height - roundW);
        int k = 0;
        for (Point[] ps : pointMap) {
            for (Point p : ps) {
                p.index = k;
                k++;
            }
        }
        isCache = true;
    }

    // 计算圆以及连接线的尺寸
    private void initGestureLockViewWidth() {
        if (mCircleSpacing == 0) {
            initCircleSpacing();
        } else {
            float mSpacing = mCircleSpacing * (3 + 1);
            mOuterRingWidth = (width - mSpacing) / 3;
        }
        if (mOuterRingWidth <= 0) {//防止手动设置圆圆之间间距过大问题
            initCircleSpacing();
        }
        if (mInnerRingWidth == 0 || mInnerRingWidth >= mOuterRingWidth) {
            mInnerRingWidth = mOuterRingWidth / 3;
        }
        if (mInnerBackgroudWidth == 0 || mInnerBackgroudWidth >= mOuterRingWidth) {
            mInnerBackgroudWidth = mInnerRingWidth * 1.3f;
        }
        mInnerBackgroudRadius = mInnerBackgroudWidth / 2;
        mRadius = mOuterRingWidth / 2;
        mInnerRingRadius = mInnerRingWidth / 2;
        mArrowLength = mRadius * 0.25f;//三角形的边长
    }


    // 当外圈间距没有设置时，初始化外圆之间的间距
    private void initCircleSpacing() {
        // 计算每个GestureLockView的宽度
        mOuterRingWidth = width / 6;
        //计算每个GestureLockView的间距
        mCircleSpacing = (width - mOuterRingWidth * 3) / 4;
    }

    //获取x轴第几个点的x坐标
    private float getX(int i) {
        if (i == 0) {
            return mCircleSpacing + mRadius;
        } else if (i == 1) {
            return width / 2;
        }
        return mCircleSpacing * 3 + mOuterRingWidth * 2 + mRadius;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 不可操作
        if (errorNumber <= 0) {
            return false;
        }
        isCorrect = true;
        handler.removeCallbacks(run);
        movingNoPoint = false;
        float ex = event.getX();
        float ey = event.getY();
        boolean isFinish = false;
        Point p = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 点下
                // 如果正在清除密码,则取消
                p = actionDown(ex, ey);
                break;
            case MotionEvent.ACTION_MOVE: // 移动
                p = actionMove(ex, ey);
                break;
            case MotionEvent.ACTION_UP: // 提起
                p = checkSelectPoint(ex, ey);
                checking = false;
                isFinish = true;
                break;
            default:
                movingNoPoint = true;
                break;
        }
        if (!isFinish && checking && p != null) {
            int rk = crossPoint(p);
            if (rk == 2) {
                //与非最后一重叠
                movingNoPoint = true;
                movingX = ex;
                movingY = ey;
            } else if (rk == 0) {
                //一个新点
                p.state = Point.STATE_PRESSED;
                addPoint(p);
            }
        }
        if (isFinish) {
            actionFinish();
        }
        postInvalidate();
        return true;
    }

    /**
     * 解锁图案绘制完成
     */
    private void actionFinish() {
        handler.postDelayed(run, 500);
        if (this.seletedList.size() == 1) {
            this.reset();
            return;
        }
        if (this.seletedList.size() < passwordMinLength
                && this.seletedList.size() > 0) {
            // clearPassword(CLEAR_TIME);
            error();
            if (mCompleteListener != null) {
                mCompleteListener.onPasswordIsShort(passwordMinLength);  //密码太短
            }
            return;
        }
        if (this.seletedList.size() >= passwordMinLength) {
            int[] indexs = new int[seletedList.size()];
            for (int i = 0; i < seletedList.size(); i++) {
                indexs[i] = seletedList.get(i).index;
            }
            if (mode == LockMode.SETTING_PASSWORD || isEditVerify) {
                invalidSettingPass(Base64.encryptionString(indexs), indexs);
            } else {
                onVerifyPassword(Base64.encryptionString(indexs), indexs);
            }
        }
    }

    /**
     * 按下
     *
     * @param ex
     * @param ey
     */
    private Point actionDown(float ex, float ey) {
        // 如果正在清除密码,则取消
        if (task != null) {
            task.cancel();
            task = null;
        }
        // 删除之前的点
        reset();
        Point p = checkSelectPoint(ex, ey);
        if (p != null) {
            checking = true;
        }
        return p;
    }

    /**
     * 移动
     *
     * @param ex
     * @param ey
     */
    private Point actionMove(float ex, float ey) {
        Point p = null;
        if (checking) {
            p = checkSelectPoint(ex, ey);
            if (p == null) {
                movingNoPoint = true;
                movingX = ex;
                movingY = ey;
            }
        }
        return p;
    }


    /**
     * 向选中点集合中添加一个点
     *
     * @param point
     */
    private void addPoint(Point point) {
        this.seletedList.add(point);
    }

    /**
     * 检查点是否被选择
     *
     * @param x
     * @param y
     * @return
     */
    private Point checkSelectPoint(float x, float y) {
        for (int i = 0; i < pointMap.length; i++) {
            for (int j = 0; j < pointMap[i].length; j++) {
                Point p = pointMap[i][j];
                if (LockUtil.checkInRound(p.x, p.y, mRadius, (int) x, (int) y)) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * 判断点是否有交叉 返回 0,新点 ,1 与上一点重叠 2,与非最后一点重叠
     *
     * @param p
     * @return
     */
    private int crossPoint(Point p) {
        // 重叠的不最后一个则 reset
        if (seletedList.contains(p)) {
            if (seletedList.size() > 2) {
                // 与非最后一点重叠
                if (seletedList.get(seletedList.size() - 1).index != p.index) {
                    return 2;
                }
            }
            return 1; // 与最后一点重叠
        } else {
            return 0; // 新点
        }
    }


    /**
     * 清空当前信息
     */
    public void clearCurrent() {
        showTimes = 0;
        errorNumber = 4;
        isCorrect = true;
        reset();
        postInvalidate();
    }

    /**
     * 验证设置密码，滑动两次密码是否相同
     *
     * @param password
     */
    private void invalidSettingPass(String password, int[] indexs) {
        if (showTimes == 0) {
            oldPassword = password;
            if (mCompleteListener != null) {
                mCompleteListener.onAginInputPassword(mode, password, indexs);
            }
            showTimes++;
            reset();
        } else if (showTimes == 1) {
            onVerifyPassword(password, indexs);
        }
    }

    /**
     * 验证本地密码与当前滑动密码是否相同
     *
     * @param indexs
     */
    private void onVerifyPassword(String password, int[] indexs) {
        if (oldPassword != null && oldPassword.length() == password.length()) {
            if (!TextUtils.equals(oldPassword, password)) {
                isCorrect = false;
            } else {
                isCorrect = true;
            }
        } else {
            isCorrect = false;
        }
        if (!isCorrect) {
            drawPassWordError();
        } else {
            drawPassWordRight(password, indexs);
        }
    }

    /**
     * 密码输入错误回调
     */
    private void drawPassWordError() {
        if (mCompleteListener == null) {
            return;
        }
        if (mode == LockMode.SETTING_PASSWORD) {
            mCompleteListener.onEnteredPasswordsDiffer();
        } else if (mode == LockMode.EDIT_PASSWORD && isEditVerify) {
            mCompleteListener.onEnteredPasswordsDiffer();
        } else {
            errorNumber--;
            if (errorNumber <= 0) {
                mCompleteListener.onErrorNumberMany();
            } else {
                mCompleteListener.onError(errorNumber + "");
            }
        }
        error();
        postInvalidate();
    }


    /**
     * 输入密码正确相关回调
     *
     * @param indexs
     * @param password
     */
    private void drawPassWordRight(String password, int[] indexs) {
        if (mCompleteListener == null) {
            return;
        }
        if (mode == LockMode.EDIT_PASSWORD && !isEditVerify) {//修改密码，旧密码正确，进行新密码设置
            mCompleteListener.onInputNewPassword();
            isEditVerify = true;
            showTimes = 0;
            return;
        }
        if (mode == LockMode.EDIT_PASSWORD && isEditVerify) {
            savePassWord(password);
        } else if (mode == LockMode.CLEAR_PASSWORD) {//清除密码
            if (isClearPasssword) {
                ConfigUtil.getInstance(getContext()).remove(saveLockKey);
            }
        } else if (mode == LockMode.SETTING_PASSWORD) {//完成密码设置，存储到本地
            savePassWord(password);
        } else {
            isEditVerify = false;
        }
        mCompleteListener.onComplete(password, indexs);
    }

    /**
     * 存储密码到本地
     *
     * @param password
     */
    private void savePassWord(String password) {
        if (isSavePin) {
            ConfigUtil.getInstance(getContext()).putString(saveLockKey, password);
        }
    }


    /**
     * 设置已经选中的为错误
     */
    private void error() {
        for (Point p : seletedList) {
            p.state = Point.STATE_ERROR;
        }
    }





    /////////////////////////////////////////////////////////////// API /////////////////////////////////////////////

    @Override
    public void keepOldPwd(Context context,String key) {
        this.oldPassword = ConfigUtil.getInstance(context).getString(key);
    }

    /**
     * @param num 最大输入错误次数
     */
    @Override
    public void setErrorCount(int num) {
        this.errorNumber = num;
    }

    @Override
    public void setIsShowTriangle(boolean isShow) {
        this.isShow = isShow;
    }

    @Override
    public void setSaveLockKey(String saveLockKey) {
        this.saveLockKey = saveLockKey;
    }

    @Override
    public boolean isShow() {
        return this.isShow;
    }

    @Override
    public boolean isHasPwd(Context context) {
        String pwd = ConfigUtil.getInstance(context).getString(this.saveLockKey);
        return pwd != null;
    }


    @Override
    public void setMode(LockMode mode) {
        this.mode = mode;
    }

    @Override
    public void setSavePin(boolean savePin) {
        ///是否保存输入的密码
        this.isSavePin = savePin;
    }

    @Override
    public void setPasswordMinLength(int passwordMinLength) {
        this.passwordMinLength = passwordMinLength;
    }

    @Override
    public void setIsClearPassword(boolean isClearPassword) {
        this.isClearPasssword = isClearPassword;
    }


    @Override
    public void setOnCompleteListener(OnCompleteListener listener) {
        this.mCompleteListener = listener;
    }

    @Override
    public LockMode getMode() {
        return this.mode;
    }

    @Override
    public boolean isSavePin() {
        return this.isSavePin;
    }

}


