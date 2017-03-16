package com.androidjp.traffichelper.home;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.User;
import com.androidjp.traffichelper.home.view.GlideCircleTransform;
import com.androidjp.traffichelper.login.LoginActivity;
import com.androidjp.traffichelper.settings.SettingsActivity;
import com.androidjp.traffichelper.user.UserActivity;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 左侧菜单Fragment
 * Created by androidjp on 2016/12/20.
 */

public class MainMenuFragment extends Fragment implements MainMenuContract.View, View.OnClickListener {

    private View rootView;

    @Bind(R.id.id_img2)
    TextView ivImg1;
    @Bind(R.id.id_img4)
    TextView ivImg2;
    @Bind(R.id.id_img5)
    TextView ivImg3;

    @Bind(R.id.layout_user)
    RelativeLayout btnUser;
    @Bind(R.id.id_user_img)
    ImageView ivUserPic;
    @Bind(R.id.tv_username)
    TextView tvUserName;
    @Bind(R.id.tv_not_login)
    TextView tvNotLogin;
    @Bind(R.id.btn_menu_close)
    RelativeLayout btnCloseApp;
    @Bind(R.id.btn_menu_setting)
    RelativeLayout btnSettings;
    @Bind(R.id.btn_menu_history)
    RelativeLayout btnHistory;

    private MainMenuContract.Presenter mPresenter;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mainmenu, container, false);
        }
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "iconfont/iconfont.ttf");
        ivImg1.setTypeface(iconfont);
        ivImg2.setTypeface(iconfont);
        ivImg3.setTypeface(iconfont);

        btnUser.setOnClickListener(this);
        btnCloseApp.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mPresenter.checkUserLogin(resultCode);
    }

    private void hideUserMsg() {
        this.tvNotLogin.setVisibility(View.VISIBLE);
        this.tvUserName.setVisibility(View.GONE);
        this.ivUserPic.setVisibility(View.GONE);
    }

    private void showUserMsg() {
        this.tvNotLogin.setVisibility(View.GONE);
        this.tvUserName.setVisibility(View.VISIBLE);
        this.ivUserPic.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(MainMenuContract.Presenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_user:
                mPresenter.gotoUserPage();
                break;
            case R.id.btn_menu_setting:
                mPresenter.gotoSettings();
                break;
            case R.id.btn_menu_close:
                mPresenter.closeApp();
                break;
        }
    }


    @Override
    public void refreshUserMsg(User user) {
        if (user == null) {
            this.hideUserMsg();
        } else {
            this.showUserMsg();
            Glide.with(this)
                    .load(user.getUser_pic())
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new GlideCircleTransform(getActivity()))
                    .into(ivUserPic);
            if (user.getUser_id()!=null){
                if (user.getUser_name()!=null)
                    this.tvUserName.setText(user.getUser_name());
                else
                    this.tvUserName.setText(user.getUser_id());
            }else{
                this.tvUserName.setText("用户");
            }
        }
    }

    @Override
    public void gotoActivity(String className, Intent intent) {
        if (className.equals(LoginActivity.class.getSimpleName())){
            ComponentName componentName = new ComponentName(getContext(),LoginActivity.class);
            intent.setComponent(componentName);
            startActivityForResult(intent,0);
        }else if (className.equals(UserActivity.class.getSimpleName())){
            ComponentName componentName = new ComponentName(getContext(),UserActivity.class);
            intent.setComponent(componentName);
            startActivityForResult(intent,0);
        }else if (className.equals(SettingsActivity.class.getSimpleName())){
            ComponentName componentName = new ComponentName(getContext(),SettingsActivity.class);
            intent.setComponent(componentName);
            startActivityForResult(intent,0);
        }
    }
}
