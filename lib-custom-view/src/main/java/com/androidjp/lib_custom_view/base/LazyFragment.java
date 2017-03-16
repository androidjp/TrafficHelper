package com.androidjp.lib_custom_view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidjp.lib_common_util.system.LogUtil;

/**
 * 强调 懒加载 的 Fragment
 * 优点：
 * 1. 保证数据的懒加载
 * 2. 保证懒加载遵守Fragment生命周期
 * Created by androidjp on 2016/12/8.
 */
public abstract class LazyFragment extends Fragment {

    /**
     * rootView 是否初始化标志
     * (防止回调函数在rootView为空时触发)
     */
    private boolean hasCreateView;

    /**
     * Fragment可见标志
     * ① 可见: 加载数据
     * ② 不可见: 不加载数据
     */
    private boolean isFragmentVisible;

    /**
     * onCreateView()返回的View【子类需要在onCreateView()中的initView()方法中初始化】
     */
    protected View rootView;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        LogUtil.info(this.getClass(),"setUserVisibleHint(): 是否可见? "+ isVisibleToUser);
        if (rootView == null){
            return;
        }
        hasCreateView = true;

        ///① 保证Fragment是已经绑定了View的
        if (isVisibleToUser){
            ///从不可见到可见
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        ///② 从可见变成不可见
        if (isFragmentVisible){
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //see: 在onCreate中进行bool变量的初始化,是因为: 已经创建的Fragment,只要没有被销毁,就不会调用onCreate()方法,只会调用onCreateView()和onDestroyView()方法
        this.hasCreateView = false;
        this.isFragmentVisible = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView==null){
            rootView = inflater.inflate(onCreateViewByLayoutId(), container, false);
        }
        return rootView;
    }


    protected abstract int onCreateViewByLayoutId();



    protected abstract void onFragmentVisibleChange(boolean b);

}
