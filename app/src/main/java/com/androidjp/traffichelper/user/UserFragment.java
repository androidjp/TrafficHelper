package com.androidjp.traffichelper.user;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidjp.lib_common_util.system.KeyBoardUtil;
import com.androidjp.lib_common_util.ui.SnackUtil;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.glide.GlideCircleTransform;
import com.androidjp.traffichelper.data.pojo.User;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 用户界面Fragment
 * Created by androidjp on 2017/3/22.
 */

public class UserFragment extends Fragment implements UserContract.View, View.OnClickListener {

    @Bind(R.id.layout_user_pic)
    RelativeLayout layoutUserPic;
    @Bind(R.id.iv_user_pic)
    ImageView ivUserPic;
    @Bind(R.id.layout_user_name)
    RelativeLayout layoutUserName;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.layout_age)
    RelativeLayout layoutAge;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.layout_email)
    RelativeLayout layoutEmail;
    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.layout_phone)
    RelativeLayout layoutPhone;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.layout_sex)
    RelativeLayout layoutSex;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.layout_user_pwd)
    RelativeLayout layoutUserPwd;
    @Bind(R.id.btn_logout)
    Button btnLogout;

    private UserContract.Presenter mPresenter;
    View mRootView;

    ///退出了右进来
    private boolean exitThenInto = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_user, container, false);
        }
        exitThenInto = true;
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        layoutUserPic.setOnClickListener(this);
        layoutAge.setOnClickListener(this);
        layoutEmail.setOnClickListener(this);
        layoutPhone.setOnClickListener(this);
        layoutSex.setOnClickListener(this);
        layoutUserName.setOnClickListener(this);
        layoutUserPwd.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        ivUserPic.setOnClickListener(this);
    }

    @Override
    public void loading() {
        SnackUtil.show(btnLogout,"上传中，请勿离开此界面。。");
    }

    @Override
    public void finishLoad(User user) {


        if (user == null) {
            SnackUtil.show(btnLogout, "修改数据失败。。");
            return;
        }

        Glide.with(getActivity())
                .load(ServiceAPI.REMOTE_SERVER_HOST+user.getUser_pic())
//                                    .asGif() // 只能加载gif文件
                // .asBitmap() // 将gif作为静态图加载
                .placeholder(R.drawable.load)//占位符 也就是加载中的图片，可放个gif
                .error(R.mipmap.ic_launcher)//失败图片
                .crossFade(1000) // 图片淡入效果：可设置时长，默认“300ms”
                .transform(new GlideCircleTransform(getActivity()))
                .into(ivUserPic);
        tvUsername.setText(user.getUser_name());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        tvSex.setText(user.getSex() == 0 ? "男" : "女");
        tvAge.setText(user.getAge() + "");

        if (!this.exitThenInto)
            SnackUtil.show(btnLogout, "修改数据成功");
        else
            this.exitThenInto = false;

    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_user_pic:
                ///选择图片
                mPresenter.selectUserPic();
                break;
            case R.id.layout_user_name:
                ModifyDialogFragment.mode = ModifyDialogFragment.M_USER_NAME;
                showDialog(new ModifyDialogFragment.FinishedEditListener() {
                    @Override
                    public void finish(String text) {
                        KeyBoardUtil.hideKeyboard(getActivity());
                        mPresenter.modifyName(text);
                    }
                });
                break;
            case R.id.layout_email:
                ModifyDialogFragment.mode = ModifyDialogFragment.M_USER_EMAIL;
                showDialog(new ModifyDialogFragment.FinishedEditListener() {
                    @Override
                    public void finish(String text) {
                        KeyBoardUtil.hideKeyboard(getActivity());
                        mPresenter.modifyEmail(text);
                    }
                });
                break;
            case R.id.layout_phone:
                ModifyDialogFragment.mode = ModifyDialogFragment.M_USER_PHONE;
                showDialog(new ModifyDialogFragment.FinishedEditListener() {
                    @Override
                    public void finish(String text) {
                        KeyBoardUtil.hideKeyboard(getActivity());
                        mPresenter.modifyPhone(text);
                    }
                });
                break;
            case R.id.layout_age:
                ModifyDialogFragment.mode = ModifyDialogFragment.M_USER_AGE;
                showDialog(new ModifyDialogFragment.FinishedEditListener() {
                    @Override
                    public void finish(String text) {
                        KeyBoardUtil.hideKeyboard(getActivity());
                        mPresenter.modifyAge(Integer.valueOf(text));
                    }
                });
                break;
            case R.id.layout_sex:
                ModifyDialogFragment.mode = ModifyDialogFragment.M_USER_SEX;
                showDialog(new ModifyDialogFragment.FinishedEditListener() {
                    @Override
                    public void finish(String text) {
                        KeyBoardUtil.hideKeyboard(getActivity());
                        mPresenter.modifySex(Integer.valueOf(text));
                    }
                });
                break;
            case R.id.layout_user_pwd:
                ModifyDialogFragment.mode = ModifyDialogFragment.M_USER_PWD;
                showDialog(new ModifyDialogFragment.FinishedEditListener() {
                    @Override
                    public void finish(String text) {
                        KeyBoardUtil.hideKeyboard(getActivity());
                        mPresenter.modifyPwd(text);
                    }
                });
                break;
            case R.id.btn_logout:
                new SweetAlertDialog(getActivity()).setTitleText("是否注销登录？")
                        .setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.setTitleText("注销中。。");
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                        mPresenter.logout();
                    }
                }).setCancelText("取消").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
                break;
            case R.id.iv_user_pic:
                ///TODO:预览图片
                this.mPresenter.previewUserPic();
                break;
        }
    }

    private void showDialog(ModifyDialogFragment.FinishedEditListener listener) {
        ModifyDialogFragment dialog = new ModifyDialogFragment();
        dialog.setFinishedEditListener(listener);
        dialog.show(getActivity().getSupportFragmentManager(), "ModifyDialog");
    }

}
