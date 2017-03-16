package com.androidjp.lib_lockview.listener;

import com.androidjp.lib_lockview.enums.LockMode;

/**
 * 轨迹球画完监听事件
 */
public interface OnCompleteListener {
    /**
     * 画完了
     */
    void onComplete(String password, int[] indexs);
    /**
     * 绘制错误
     */
    void onError(String errorTimes);

    /**
     * 密码太短
     */
    void onPasswordIsShort(int passwordMinLength);


    /**
     * 设置密码再次输入密码
     */
    void onAginInputPassword(LockMode mode, String password, int[] indexs);


    /**
     * 修改密码，输入新密码
     */
    void onInputNewPassword();

    /**
     * 两次输入密码不一致
     */
    void onEnteredPasswordsDiffer();

    /**
     * 密码输入错误次数，已达到设置次数
     */
    void onErrorNumberMany();

}
