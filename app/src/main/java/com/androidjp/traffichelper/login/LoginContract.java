package com.androidjp.traffichelper.login;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;

/**
 * Created by androidjp on 2016/12/9.
 */

public class LoginContract {

    interface View extends BaseView<Presenter>{
        //显示找回密码
        void showPwd(String password);
        //显示登录界面
        void showLoginPage();
        //显示注册界面
        void showRegisterPage();
        // 错误信息
        void showErrorMsg(String msg);
        void showProgress(String msg);
        void hideProgress(int resultFlag,String msg);
    }

    interface Presenter extends BasePresenter{
        //登录
        void login(String userId, String password);
        //注册
        void register(String userName, String password,String email ,String phone,int sex,String age);
        //找回密码
        void findPwd(String userId);
        //true 表示可以直接退出本Activity， false 表示注册界面退回到登录界面
        void back();
    }

}
