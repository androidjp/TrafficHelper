package com.androidjp.traffichelper.welcome;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 启动页Presenter实现类
 * Created by androidjp on 2016/12/1.
 */

public class WelcomePresenter implements  WelcomeContract.Presenter{

    private SoftReference<WelcomeContract.View> mView;

    public WelcomePresenter(WelcomeContract.View view) {
        this.mView = new SoftReference<>(view);
    }

    public void start() {

    }

    @Override
    public void checkIfFirstLoad() {
        this.getView().jumpMain();
    }

    public WelcomeContract.View getView(){
        return this.mView.get();
    }
}
