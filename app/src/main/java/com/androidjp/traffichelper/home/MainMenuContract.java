package com.androidjp.traffichelper.home;

import android.content.Intent;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.User;

/**
 * Created by androidjp on 2016/12/20.
 */

public class MainMenuContract {

    public interface View extends BaseView<Presenter>{
        ///登录或注册后，刷新用户信息
        void refreshUserMsg(User user);
        ///根据className去设置跳转
        void gotoActivity(String className, Intent intent);
    }

    public interface Presenter extends BasePresenter{
        //检查是否登录成功
        void checkUserLogin(int resultCode);
        //用户详细界面
        void gotoUserPage();
        //去设置
        void gotoSettings();
        //退出App
        void closeApp();
    }
}
