package com.androidjp.traffichelper.login;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.androidjp.lib_common_util.pojo.network.Result;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.SharedPrefManager;
import com.androidjp.traffichelper.data.model.UserManager;
import com.androidjp.traffichelper.data.pojo.User;
import com.orhanobut.logger.Logger;

import java.lang.ref.SoftReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Login界面 Presenter实现类
 * Created by androidjp on 2016/12/27.
 */

public class LoginPresenter implements LoginContract.Presenter {
    SoftReference<LoginContract.View> mView;
    Context mContext;
    private boolean isCurrentLoginPage = true;//当前是登录界面么？初始是 true

    /**
     * 相关数据
     */
    private User mUser;


    public LoginPresenter( Context mContext,LoginContract.View view) {
        this.mView = new SoftReference<LoginContract.View>(view);
        this.mContext = mContext;
    }


    @Override
    public void login(String userId, String password) {

        //检查合法性
        if (TextUtils.isEmpty(userId)||TextUtils.isEmpty(password)){
            this.mView.get().showErrorMsg(THApplication.getContext().getString(R.string.null_username_pwd));
            return;
        }

        this.mView.get().showProgress("正在登录。。");

        ///使用Retrofit2进行登录请求
        Retrofit loginRetro = new Retrofit.Builder()
                .baseUrl((ServiceAPI.IS_DEBUG?ServiceAPI.SERVER_HOST:ServiceAPI.REMOTE_SERVER_HOST))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceAPI.LoginAPI loginAPI = loginRetro.create(ServiceAPI.LoginAPI.class);
        Call<Result<User>> call = loginAPI.login(userId,password);
        Logger.i("获取Call对象， 开始登录请求");
        call.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                Result<User> result = response.body();
                if (result!= null &&result.code == 200 && result.msg.equals("success")){
                    Logger.i(result.toString());
                    User resUser = response.body().data;
                    UserManager.getInstance(mContext).refreshUser(resUser);
                    if (mView!=null)
//                        mView.get().hideProgress(true,"成功登录");
                        mView.get().hideProgress(false,"登录测试完成");
                }else{
                    if (mView!=null)
                        mView.get().hideProgress(false,"登录失败");
                }
            }

            @Override
            public void onFailure(Call<Result<User>> call, Throwable t) {
                if (mView!=null)
                    mView.get().hideProgress(false,"登录失败");
            }
        });


    }


    @Override
    public void register(String userName, String password, String email, String phone, int sex) {

        if (this.isCurrentLoginPage){
            this.mView.get().showRegisterPage();
            this.isCurrentLoginPage = false;
        }else{
            this.mView.get().showProgress("正在注册。。");
            User user = new User();
            user.setEmail(email);
            user.setPhone(phone);
            user.setSex(sex);
            user.setUser_name(userName);
            user.setUser_pwd(password);
            //TODO：访问服务器，注册请求，并让界面显示进度条
            ///使用Retrofit2进行登录请求
            Retrofit loginRetro = new Retrofit.Builder()
                    .baseUrl((ServiceAPI.IS_DEBUG?ServiceAPI.SERVER_HOST:ServiceAPI.REMOTE_SERVER_HOST))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ServiceAPI.LoginAPI loginAPI = loginRetro.create(ServiceAPI.LoginAPI.class);
            Call<Result<User>> call = loginAPI.register(user);
            call.enqueue(new Callback<Result<User>>() {
                @Override
                public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                    Logger.d(response.toString());
                   Result<User> result = response.body();
                    if (result!= null &&result.code==200 && result.msg.equals("success")){
//                        User resUser = response.body().data;
//                        UserManager.getInstance(mContext).refreshUser(resUser);
                        Logger.i(result.toString());
                        if (mView!=null)
//                            mView.get().hideProgress(true,"注册成功");
                            mView.get().hideProgress(false,"注册测试完成");

                    }else{
                        if (mView!=null)
                            mView.get().hideProgress(false,"注册失败");
                    }
                }

                @Override
                public void onFailure(Call<Result<User>> call, Throwable t) {
                    if (mView!=null)
                        mView.get().hideProgress(false,"请求失败");
                }
            });
        }
    }

    @Override
    public void findPwd(String userId) {

    }

    @Override
    public void back() {
        if (this.isCurrentLoginPage){
            ((Activity)this.mView.get()).finish();
        }else{
            this.mView.get().showLoginPage();
            this.isCurrentLoginPage = true;
        }
    }

    @Override
    public void start() {
        ////TODO：找到spf，自动登录
        if (this.mUser==null){
            this.mUser = SharedPrefManager.getInstance().getUserMsg(mContext);
        }
        if (this.mUser.getUser_id()!=null&&this.mUser.getUser_pwd()!=null){
            login(this.mUser.getUser_id() ,this.mUser.getUser_pwd());
        }
    }
}
