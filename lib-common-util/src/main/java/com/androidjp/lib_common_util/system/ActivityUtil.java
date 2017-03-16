package com.androidjp.lib_common_util.system;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by androidjp on 2016/12/20.
 */

public class ActivityUtil {

    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * replace + addToBackStack ，让fragment能够支持back键的切换
     * @param fragmentManager
     * @param fragment
     * @param frameId
     * @param tag
     */
    public static void replaceFragmentAndAddBlackStack(@NonNull FragmentManager fragmentManager,@NonNull Fragment fragment, int frameId , String tag){
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId,fragment,tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static void replaceFragmentAndAddBlackStack(@NonNull FragmentManager fragmentManager,@NonNull Fragment fragment, int frameId , String tag,int enterAnim,int exitAnim){
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enterAnim,exitAnim);
        transaction.replace(frameId,fragment,tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
