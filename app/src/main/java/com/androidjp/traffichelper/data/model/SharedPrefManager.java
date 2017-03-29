package com.androidjp.traffichelper.data.model;

import android.content.Context;

import com.androidjp.lib_baidu_asr.data.Constant;
import com.androidjp.lib_common_util.data.SharedPrefHelper;
import com.androidjp.traffichelper.data.Constants;
import com.androidjp.traffichelper.data.pojo.User;

/**
 * 针对 业务的SPF封装管理类（单例）
 * Created by androidjp on 2016/12/26.
 */
public class SharedPrefManager {

    private final String USER_ID = "USER_ID";
    private final String USER_NAME = "USER_NAME";
    private final String USER_PWD = "USER_PWD";
    private final String USER_EMAIL = "USER_EMAIL";
    private final String USER_PHONE = "USER_PHONE";
    private final String USER_SEX = "USER_SEX";
    private final String USER_PIC = "USER_PIC";
    private final String USER_AGE = "USER_AGE";


    private SharedPrefManager(){

    }

    public static SharedPrefManager getInstance(){
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder{
        private static SharedPrefManager sInstance = new SharedPrefManager();
    }

    //------------------------------------------------
    public synchronized void saveUserMsg(Context context, String userId, String userName, String userPwd, String email , String phone, int sex ,String userPic,int age){
        SharedPrefHelper helper = SharedPrefHelper.getInstance(context);
        helper.put(USER_ID, userId);
        helper.put(USER_NAME, userName);
        helper.put(USER_EMAIL, email);
        helper.put(USER_PHONE, phone);
        helper.put(USER_PIC ,userPic);
        helper.put(USER_PWD, userPwd);
        helper.put(USER_AGE, age);
        helper.put(USER_SEX,sex);
        helper.release();
    }

    public User getUserMsg(Context context){
        User user = new User();
        SharedPrefHelper helper = SharedPrefHelper.getInstance(context);
        user.setUser_id(helper.getAsString(USER_ID,null));
        user.setUser_name(helper.getAsString(USER_NAME,null));
        user.setUser_pwd(helper.getAsString(USER_PWD,null));
        user.setEmail(helper.getAsString(USER_EMAIL,null));
        user.setPhone(helper.getAsString(USER_PHONE,null));
        user.setUser_pic(helper.getAsString(USER_PIC,null));
        user.setSex(helper.getAsInt(USER_SEX, Constants.MALE));///默认是 Male
        user.setAge(helper.getAsInt(USER_AGE, 0));
        helper.release();
        return user;
    }

    public void deleteUserMsg(Context context){
        SharedPrefHelper helper = SharedPrefHelper.getInstance(context);
        helper.deleteKeyValue(USER_ID);
        helper.deleteKeyValue(USER_NAME);
        helper.deleteKeyValue(USER_PWD);
        helper.deleteKeyValue(USER_EMAIL);
        helper.deleteKeyValue(USER_PHONE);
        helper.deleteKeyValue(USER_AGE);
        helper.deleteKeyValue(USER_PIC);
        helper.deleteKeyValue(USER_SEX);

    }

}
