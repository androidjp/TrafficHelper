package com.androidjp.traffichelper.result;

import android.support.v4.app.Fragment;

/**
 * Created by androidjp on 2017/3/20.
 */

public class ResultFragment extends Fragment implements ResultContract.View {
    private ResultContract.Presenter mPresenter = null;

    @Override
    public void setPresenter(ResultContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


}
