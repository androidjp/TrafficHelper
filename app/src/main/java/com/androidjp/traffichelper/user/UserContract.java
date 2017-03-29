package com.androidjp.traffichelper.user;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.User;

/**
 * Created by androidjp on 2016/12/9.
 */

public class UserContract  {

    interface View extends BaseView<Presenter>{
        void loading();
        void finishLoad(User user);

    }

    interface Presenter extends BasePresenter{
        ///设置用户头像
        void selectUserPic();
        //修改昵称
        void modifyName(String newName);
        //修改电话号码
        void modifyPhone(String newPhone);
        //修改邮箱
        void modifyEmail(String newEmail);
        //修改年龄
        void modifyAge(int age);
        //修改性别
        void modifySex(int sex);

        void modifyPwd(String pwd);
        //退出登录
        void logout();
        ///预览user_pic
        void previewUserPic();
    }
}
