package com.androidjp.lib_custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

/**
 * Created by androidjp on 16-7-29.
 */
public class LogView extends View{

    ///控制是否输出Toast
    private static final boolean IS_TOAST = false;
    ///控制是否输出Log
    private static final boolean IS_LOG = true;



    public LogView(Context context) {
        super(context);
        onLog("LogView(1)");
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onLog("LogView(1, 2)");
    }


    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onLog("LogView(1, 2, 3)");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onLog("onMeasure()");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onLog("onLayout()");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onLog("onDraw()");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onLog("onTouchEvent()");
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        onLog("onFocuesChanged("+ gainFocus+", xx, xx)");
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        onLog("onWindowFocusChanged("+ hasWindowFocus+")");
    }


    @Override
    public boolean onCheckIsTextEditor() {
        onLog("onCheckIsTextEditor()");
        return super.onCheckIsTextEditor();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        onLog("onCreateInputConnection()");
        return super.onCreateInputConnection(outAttrs);
    }

    //-------------------------------------------
    //-------------------------------------------


    private String tag(){
        return this.getClass().getName();
    }

    public  void onLog(String logMsg){
        if (IS_LOG){
            Log.i(tag(),logMsg);
        }
        if (IS_TOAST){
            Toast.makeText(this.getContext(), tag()+": "+logMsg, Toast.LENGTH_SHORT).show();
        }
    }


}
