package com.androidjp.traffichelper.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidjp.lib_great_recyclerview.base.BaseRecAdapter;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.RelativeItemMsg;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

/**
 * 填写亲戚界面
 * Created by androidjp on 2017/1/15.
 */

public class RelativesMsgFragment extends DialogFragment implements RelativesMsgContract.View{

    public static final String REQUSET_PERSON_COUNT = "REQUSET_PERSON_COUNT";

    private BaseRecAdapter<RelativeItemMsg> mAdapter;
//    @Bind(R.id.recview_relatives_msg)
    private RecyclerView mRecView;

    private RelativesMsgContract.Presenter mPresenter;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_relatives_msg);
        Logger.i("onCreateDialog()");
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.i("onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setPresenter(RelativesMsgContract.Presenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.start();
    }


    @Override
    public void returnMsg(String msg) {
        // 判断是否设置了targetFragment
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(REQUSET_PERSON_COUNT, "已选择"+this.mAdapter.getItemCount()+"个人");
        getTargetFragment().onActivityResult(11,
                Activity.RESULT_OK, intent);
    }
}
