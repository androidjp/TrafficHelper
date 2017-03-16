package com.androidjp.traffichelper.user;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;

/**
 * Created by androidjp on 2016/12/9.
 */

public class UserContract  {

    interface View extends BaseView<Presenter>{
        void loading();
        void finishLoad();
    }

    interface Presenter extends BasePresenter{
        ///设置用户头像
        void selectUserPic();
        ///用户登录
        void login(String username, String password);
        //修改昵称
        void modifyName(String newName);
        //修改电话号码
        void modifyPhone(String newPhone);
        //修改邮箱
        void modifyEmail(String newEmail);
        //退出登录
        void logout();
    }
}
