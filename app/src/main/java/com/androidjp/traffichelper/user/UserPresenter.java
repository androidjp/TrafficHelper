package com.androidjp.traffichelper.user;

import android.content.Context;

import java.lang.ref.SoftReference;

/**
 * UserPresenter
 * Created by androidjp on 2017/1/12.
 */

public class UserPresenter implements UserContract.Presenter{
    private SoftReference<UserContract.View> mView;
    private Context mContext;


    public UserPresenter(Context context, UserContract.View view){
        this.mContext = context;
        this.mView = new SoftReference<UserContract.View>(view);
    }



    @Override
    public void selectUserPic() {

    }

    @Override
    public void login(String username, String password) {

    }

    @Override
    public void modifyName(String newName) {

    }

    @Override
    public void modifyPhone(String newPhone) {

    }

    @Override
    public void modifyEmail(String newEmail) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void start() {

    }
}
