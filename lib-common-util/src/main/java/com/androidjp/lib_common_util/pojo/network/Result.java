package com.androidjp.lib_common_util.pojo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 请求响应类（通用）
 * Created by androidjp on 2017/2/17.
 */

public class Result<T>{

    public int code;
    public String msg;
    public T data;
    public long count;
    public long page;

    @Override
    public String toString() {
        return "Result{"+
                "code=" + code +
                ", msg='" + msg +
                "\', data=" + data +
                ", count=" + count +
                ", page=" + page+
                "}";
    }
    public String toJsonString(){
        Gson gson = new GsonBuilder().create();
        String jsonStr = gson.toJson(this);
        return jsonStr;
    }
}
