package com.androidjp.traffichelper.adapter.behavior;


import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * 自定义的FloatingActionBar的Behavior
 * 1. 当界面向下滚动时， Fab向屏幕下边隐藏
 * 2. 当界面向上滚动时，Fab向上移回到屏幕
 */
public class FABBottomUpDownBehavior extends CoordinatorLayout.Behavior<View>{
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    //控件距离coordinatorLayout底部距离
    private float viewY;
    //动画是否在进行
    private boolean isAnimate;

    public FABBottomUpDownBehavior(Context context, AttributeSet attributeSet){
            super(context,attributeSet);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                      View directTargetChild, View target, int nestedScrollAxes) {
        if(child.getVisibility()==View.VISIBLE&&viewY==0){
            viewY=coordinatorLayout.getHeight()-child.getY();
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if(dy>=0&&!isAnimate&&child.getVisibility()==View.VISIBLE){
            hide(child);
        }else if(dy <0&&!isAnimate&&child.getVisibility()==View.GONE){
            show(child);
        }
    }

    //隐藏时的动画
    private void hide(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(viewY).setInterpolator(INTERPOLATOR).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimate=true;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
                isAnimate=false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {
                show(view);
            }
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animator.start();
    }

    //显示时的动画
    private void show(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(0).setInterpolator(INTERPOLATOR).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
                isAnimate=true;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimate=false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {
                hide(view);
            }
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animator.start();
    }

}

