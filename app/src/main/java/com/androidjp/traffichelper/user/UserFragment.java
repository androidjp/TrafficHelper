package com.androidjp.traffichelper.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidjp.traffichelper.R;

/**
 * Created by androidjp on 2017/3/22.
 */

public class UserFragment extends Fragment implements UserContract.View {

    View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView==null){
            mRootView = inflater.inflate(R.layout.fragment_user,container,false);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void loading() {

    }

    @Override
    public void finishLoad() {

    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {

    }
}
