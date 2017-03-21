package com.androidjp.traffichelper.data.pojo;

import com.androidjp.lib_common_util.data.NumberUtil;

/**
 * Created by androidjp on 2017/3/20.
 */

public class ResultInfo {

    private String title;
    private float money;
    private String content;

    public ResultInfo(String title,float money, String content) {
        this.title = title;
        this.money = money;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getMoney(){
        return NumberUtil.doubleRestStr(money);
    }
}
