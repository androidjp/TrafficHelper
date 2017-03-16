package com.androidjp.lib_custom_view.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.androidjp.lib_custom_view.R;
import com.androidjp.lib_custom_view.titlebar.ImmerseAdapter;

/**
 * 沉浸式Toolbar
 * Created by androidjp on 16-7-22.
 */
public class ImmerseToolbar extends Toolbar {

    public ImmerseToolbar(Context context) {
        this(context, null);
    }

    public ImmerseToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public ImmerseToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof Activity){
            ImmerseAdapter.setImmerseStyle((Activity)context);
        }

        init();
    }

    /**
     * 初始化：由于在xml中定义属性，在其他模块中用不到，所以，使用java方式定义属性
     */
    private void init() {
        this.setFitsSystemWindows(true);
    }

}
