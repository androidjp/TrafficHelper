package com.androidjp.traffichelper.data.model;

import android.content.Context;
import android.text.TextUtils;

import com.androidjp.traffichelper.data.pojo.User;
import com.orhanobut.logger.Logger;

/**
 * 用户管理类
 * 克隆模式（）
 * Created by androidjp on 2017/1/7.
 */
public class UserManager {

    private User mUser;
    private Context mContext;
    private static UserManager sInstance;

    private UserManager(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        //持久层获取用户信息
        if (this.mUser == null) {
            this.mUser = SharedPrefManager.getInstance().getUserMsg(mContext);
        }
        if (!isUserLegal(this.mUser)){
            this.mUser = null;
        }
    }

    public static UserManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null)
                    sInstance = new UserManager(context);
            }
        }
        sInstance.mContext = context;
        return sInstance;
    }


    /**
     * 删除用户（包括：内存和SPF文件）
     */
    public void removeUser(){
        this.mUser = null;
        SharedPrefManager.getInstance().deleteUserMsg(mContext);
    }

    /**
     * 获取用户信息
     */
    public User getUser() {
        if (this.mUser == null) {
            this.mUser = SharedPrefManager.getInstance().getUserMsg(mContext);
        }
        if (!isUserLegal(this.mUser))
            return null;
        try {
            ///获取克隆体
            User user = mUser.clone();
            if (user != null){
                Logger.i("从 UserManager.mUser中克隆出来的user不为空~");
                return user;
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 获取登录用户的用户ID
     * @return 用户ID
     */
    public String getUserId(){
        getUser();
        if (this.mUser!=null && this.mUser.getUser_id()!=null)
            return this.mUser.getUser_id();
        else
            return null;
    }

    /**
     * 更新用户信息
     *
     * @param user 新的用户信息
     */
    public void refreshUser(User user) {
        if (!isUserLegal(user))return;
        if (this.mUser == null) {
            this.mUser = user;
        } else {
            this.mUser.setUser_id(user.getUser_id());
            this.mUser.setUser_pwd(user.getUser_pwd());
            this.mUser.setUser_name(user.getUser_name());
            this.mUser.setSex(user.getSex());
            this.mUser.setKind(user.getKind());
            this.mUser.setEmail(user.getEmail());
            this.mUser.setPhone(user.getPhone());
            this.mUser.setAge(user.getAge());
            if (!TextUtils.isEmpty(user.getUser_pic()))
                this.mUser.setUser_pic(user.getUser_pic());
        }
        saveUser();
    }

    private synchronized void saveUser(){
        if (isUserLegal(this.mUser))
        SharedPrefManager.getInstance().saveUserMsg(
                this.mContext,this.mUser.getUser_id(),this.mUser.getUser_name(),this.mUser.getUser_pwd(),
                this.mUser.getEmail(),this.mUser.getPhone(),this.mUser.getSex(),this.mUser.getUser_pic(),this.mUser.getAge()
                );
    }

    ///用户信息是否完整
    private boolean isUserLegal(User user) {
        return user != null && !(TextUtils.isEmpty(user.getUser_id()) || TextUtils.isEmpty(user.getUser_pwd()));
    }
}
