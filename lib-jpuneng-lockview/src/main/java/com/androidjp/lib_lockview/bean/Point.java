package com.androidjp.lib_lockview.bean;

/**
 * 九宫格点
 * Created by androidjp on 16/9/12.
 */
public class Point {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PRESSED = 1;
    public static final int STATE_ERROR = 2;

    public int state = STATE_NORMAL;//当前状态
    public float x;
    public float y;

    public int index = 0;//下标

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
