package com.androidjp.traffichelper.welcome;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;

/**
 * 启动约定
 * Created by androidjp on 2016/12/1.
 */

public class WelcomeContract {

    interface View extends BaseView<Presenter>{

        /**
         * 跳转到主界面
         */
        void jumpMain();

        /**
         * 跳转到介绍界面
         */
        void jumpGuide();
    }

    interface Presenter extends BasePresenter{

        void checkIfFirstLoad();

    }
}
