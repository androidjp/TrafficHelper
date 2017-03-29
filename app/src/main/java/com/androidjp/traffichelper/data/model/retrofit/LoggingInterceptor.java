package com.androidjp.traffichelper.data.model.retrofit;

import com.androidjp.traffichelper.BuildConfig;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 统一打印 请求 log
 * Created by androidjp on 2017/2/25.
 */

public class LoggingInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (BuildConfig.DEBUG) {
            Logger.e(String.format("发送请求 %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
        }
        return chain.proceed(request);
    }
}
