package com.androidjp.lib_common_util.net;

import android.os.Handler;
import android.telecom.Call;

/**
 * 基本（原始方式）实现异步
 * 缺点：
 * 1. new Thread多了，消耗过大
 * 2. 没有异常处理机制
 * 3. 没有缓存机制
 * 4. 没有完善的API（请求头、参数、编码、拦截器等）和调试模式
 * 5. 无 Https
 * Created by androidjp on 2017/1/11.
 */

public class BaseAsynNetUtil {

    public interface Callback{
        void onResponse(String response);
    }

    public static void get(final String url , final Callback callback){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = HttpURLConnectionUtil.get(url);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }

    public static void post(final String url , final String content , final Callback callback){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = HttpURLConnectionUtil.post(url,content);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }
}
