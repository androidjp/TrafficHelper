package com.androidjp.lib_lockview.util;

import android.view.View;

/**
 * View的相关计算
 * Created by androidjp on 16/9/12.
 */
public class ViewUtils {

    /**
     * 在父布局中放入一个正方形的View
     * 测量正方形的边长
     */
    public static int measure(int widthSpec){
        int result = 400;
        int specMode = View.MeasureSpec.getMode(widthSpec);
        int specSize = View.MeasureSpec.getSize(widthSpec);
        if (specMode == View.MeasureSpec.EXACTLY){
            //如果是已经有确定值了
            result = specSize;
        }else if (specMode == View.MeasureSpec.AT_MOST){
            //如果View是wrap_content
            result = Math.min(specSize, result);
        }///而UNSPECFIC的情况则很少出现,一般要当父容器为这种情况的时候,View才可能会这样
        return result;
    }



}
