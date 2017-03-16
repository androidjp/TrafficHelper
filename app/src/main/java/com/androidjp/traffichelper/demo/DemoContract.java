package com.androidjp.traffichelper.demo;

import android.os.Bundle;

/**
 * Created by androidjp on 2017/3/14.
 */

public class DemoContract {
    interface View{
        void showText(String text);
        void showBtnText(String text);
    }

    interface Presenter{
        void startYuYin();
        void stopAndDeal();
        void cancelYuYin();
        void toggleYuYin();
        void getIntentBundle(Bundle bundle);
    }
}
