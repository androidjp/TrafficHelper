package com.androidjp.traffichelper.user;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.androidjp.lib_common_util.ui.SnackUtil;
import com.androidjp.lib_custom_view.edittext.ClearEditText;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.model.UserManager;
import com.androidjp.traffichelper.data.pojo.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改信息对话框
 * Created by androidjp on 2017/3/27.
 */

public class ModifyDialogFragment extends DialogFragment {
    public static final int M_USER_NAME = 0x01;
    public static final int M_USER_PWD = 0x02;
    public static final int M_USER_SEX = 0x03;
    public static final int M_USER_AGE = 0x04;
    public static final int M_USER_EMAIL = 0x05;
    public static final int M_USER_PHONE = 0x06;

    public static int mode = M_USER_NAME;

    @Bind(R.id.cet_old_data)
    ClearEditText cet_old_data;
    @Bind(R.id.cet_new_data)
    ClearEditText cet_new_data;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.rbtn_male)
    RadioButton rbtnMale;
    @Bind(R.id.rbtn_female)
    RadioButton rbtnFemale;

    @Bind(R.id.layout_old_data)
    TextInputLayout layoutOldData;
    @Bind(R.id.layout_new_data)
    TextInputLayout layoutNewData;
    @Bind(R.id.layout_sex)
    RelativeLayout layoutSex;


    private FinishedEditListener mListener;
    private User mUser;
    private String oldData;
    private String newData;

    public interface FinishedEditListener {
        public void finish(String text);
    }


    public void setFinishedEditListener(FinishedEditListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_modify,  ((ViewGroup) window.findViewById(android.R.id.content)), false);//需要用android.R.id.content这个view
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        this.mUser = UserManager.getInstance(THApplication.getContext()).getUser();

        switch (mode){
            case M_USER_NAME:
                showEditText();
                this.cet_old_data.setInputType(InputType.TYPE_CLASS_TEXT);
                this.cet_new_data.setInputType(InputType.TYPE_CLASS_TEXT);
                this.oldData = this.mUser.getUser_name();
                this.cet_old_data.setText("原用户名："+this.oldData);
                break;
            case M_USER_AGE:
                showEditText();
                this.cet_old_data.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.cet_new_data.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.oldData = this.mUser.getAge()+"";
                this.cet_old_data.setText("原年龄："+this.oldData);
                break;
            case M_USER_EMAIL:
                showEditText();
                this.cet_old_data.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                this.cet_new_data.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                this.oldData = this.mUser.getEmail();
                this.cet_old_data.setText("原邮箱地址："+this.oldData);
                break;
            case M_USER_PHONE:
                showEditText();
                this.cet_old_data.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.cet_new_data.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.oldData = this.mUser.getPhone();
                this.cet_old_data.setText("原手机号："+this.oldData);
                break;
            case M_USER_PWD:
                showEditText();
                this.cet_old_data.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                this.cet_new_data.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                this.oldData = this.mUser.getUser_pwd();
                this.cet_old_data.setText("原密码："+this.oldData);
                break;
            case M_USER_SEX:
                showRadioGroup();
                if (this.mUser.getSex()==0)
                    this.rbtnMale.setChecked(true);
                else
                    this.rbtnFemale.setChecked(true);
                break;
        }
    }

    private void showRadioGroup() {
        this.layoutSex.setVisibility(View.VISIBLE);
        this.layoutOldData.setVisibility(View.GONE);
        this.layoutNewData.setVisibility(View.GONE);
    }

    private void showEditText() {
        this.layoutSex.setVisibility(View.GONE);
        this.layoutOldData.setVisibility(View.VISIBLE);
        this.layoutNewData.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.btn_confirm)
    void finishedEdit(View v) {

        switch (mode) {
            case M_USER_NAME:
            case M_USER_AGE:
            case M_USER_EMAIL:
            case M_USER_PHONE:
            case M_USER_PWD:
                newData = cet_new_data.getText().toString();
                if (TextUtils.isEmpty(newData)) {
                    SnackUtil.show(cet_new_data, "还没有填写修改信息");
                }
                if (oldData.equals(newData)){
                    if (this.mListener!=null) {
                        this.mListener.finish(null);
                    }
                    ModifyDialogFragment.this.dismiss();
                }else{
                    if (this.mListener!=null) {
                        this.mListener.finish(newData);
                    }
                    ModifyDialogFragment.this.dismiss();
                }
                break;
            case M_USER_SEX:
                if (this.mListener!=null) {
                    this.mListener.finish((rbtnMale.isChecked()?"0":"1"));
                }
                ModifyDialogFragment.this.dismiss();
                break;
        }
    }

}
