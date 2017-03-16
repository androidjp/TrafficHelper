package com.androidjp.lib_google_mvp;

import android.content.Context;

/**
 * Created by androidjp on 2016/12/9.
 */

public interface BaseView<P> {
    void setPresenter(P presenter);
}
