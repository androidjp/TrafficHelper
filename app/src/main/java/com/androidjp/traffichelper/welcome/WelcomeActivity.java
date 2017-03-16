package com.androidjp.traffichelper.welcome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.home.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 启动页Activity
 * Created by androidjp on 16-8-11.
 */
public class WelcomeActivity extends AppCompatActivity implements WelcomeContract.View,Runnable{
    @Bind(R.id.fab_welcome)
    FloatingActionButton fab;

    WelcomeContract.Presenter mPresenter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setPresenter(new WelcomePresenter(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        fab.post(this);
    }

    @Override
    public void run() {
        final View parentView = (View) fab.getParent();
        float scale = (float) (Math.sqrt(parentView.getHeight() * parentView.getHeight() + parentView.getWidth() * parentView.getWidth()) / fab.getHeight());
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", scale);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", scale);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(fab, scaleX, scaleY).setDuration(1800);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                parentView.setBackgroundColor(ContextCompat.getColor(WelcomeActivity.this, R.color.colorPrimary));
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });
//        PropertyValuesHolder holderA = PropertyValuesHolder.ofFloat("alpha", 0, 1);
//        PropertyValuesHolder holderYm = PropertyValuesHolder.ofFloat("translationY", 0, 300);
//        ObjectAnimator textAnimator = ObjectAnimator.ofPropertyValuesHolder(textView, holderA, holderYm).setDuration(1000);
//        textAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        textAnimator.setStartDelay(800);

//        textAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
//        });
        objectAnimator.start();
//        textAnimator.start();
    }



    @Override
    public void jumpMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void jumpGuide() {


    }

    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
