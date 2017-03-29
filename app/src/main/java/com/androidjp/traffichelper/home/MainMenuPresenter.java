package com.androidjp.traffichelper.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidjp.lib_common_util.pojo.network.Result;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.Constants;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.UserManager;
import com.androidjp.traffichelper.data.pojo.User;
import com.androidjp.traffichelper.history.HistoryActivity;
import com.androidjp.traffichelper.login.LoginActivity;
import com.androidjp.traffichelper.settings.SettingsActivity;
import com.androidjp.traffichelper.user.UserActivity;
import com.orhanobut.logger.Logger;

import java.lang.ref.SoftReference;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 主界面侧滑菜单的主持类
 * Created by androidjp on 2016/12/20.
 */

class MainMenuPresenter implements MainMenuContract.Presenter {


    private SoftReference<MainMenuContract.View> mView;
    private Context mContext;

    MainMenuPresenter(Context context, MainMenuContract.View view) {
        this.mContext = context;
        this.mView = new SoftReference<>(view);
    }


    @Override
    public void start() {
        ///初始化
        ///TODO:判断SPF中是否存在用户信息，有则进行自动登录，否则提示用户缓存错误，并删除SPF的用户信息
        final User user = UserManager.getInstance(THApplication.getContext()).getUser();
        if (user != null) {
            //TODO:登录
//            Logger.i("start ， 自动登录，用户名为："+ user.getUser_id());
//            ///使用Retrofit2进行登录请求
//            Retrofit loginRetro = new Retrofit.Builder()
//                    .baseUrl((ServiceAPI.IS_DEBUG?ServiceAPI.SERVER_HOST:ServiceAPI.REMOTE_SERVER_HOST))
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            ServiceAPI.LoginAPI loginAPI = loginRetro.create(ServiceAPI.LoginAPI.class);
//            Call<Result<User>> call = loginAPI.login(user.getUser_id(),user.getUser_pwd());
//            call.enqueue(new Callback<Result<User>>() {
//                @Override
//                public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
//                    ///更新用户信息
//                    User resUser = response.body().data;
//                    UserManager.getInstance(THApplication.getContext()).refreshUser(resUser);
//                    if (mView!=null)
//                        mView.get().refreshUserMsg(resUser);
//                }
//
//                @Override
//                public void onFailure(Call<Result<User>> call, Throwable t) {
//
//                }
//            });
            Logger.i("start ， 自动登录，用户名为："+ user.getUser_id());
            if (mView!=null)
                mView.get().refreshUserMsg(user);
        }
    }

    @Override
    public void checkUserLogin(int resultCode) {

        if (resultCode == Constants.FINISH_LOGIN) {
            Logger.i("检查登录信息，成功登录啦");
            if (this.mView.get() != null) {
                this.mView.get().refreshUserMsg(UserManager.getInstance(THApplication.getContext()).getUser());
            }
        } else if (resultCode == Constants.FAIL_LOGIN) {
            if (this.mView.get() != null) {
                this.mView.get().refreshUserMsg(null);
            }
        }

    }

    @Override
    public void gotoUserPage() {
        if (UserManager.getInstance(THApplication.getContext()).getUser()==null) {
            if (mView!=null)
                mView.get().gotoActivity(LoginActivity.class.getSimpleName(),new Intent());
        } else {
            if (mView!=null)
                mView.get().gotoActivity(UserActivity.class.getSimpleName(),new Intent());
        }
//        if (mView!=null)
//                mView.get().gotoActivity(UserActivity.class.getSimpleName(),new Intent());
    }

    @Override
    public void gotoSettings() {
        if (mView!=null)
            mView.get().gotoActivity(SettingsActivity.class.getSimpleName(),new Intent());
    }

    @Override
    public void gotoHistory() {
        if(mView!=null)
            mView.get().gotoActivity(HistoryActivity.class.getSimpleName(),new Intent());
    }

    @Override
    public void closeApp() {
        if (this.mView.get() == null)
            return;
        SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        dialog.setCancelable(true);
        dialog.setTitleText("是否退出App？").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        }).setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        ((Activity) mContext).finish();
                    }
                }).show();
    }


}
