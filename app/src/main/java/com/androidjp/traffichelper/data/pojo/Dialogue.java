package com.androidjp.traffichelper.data.pojo;

import android.graphics.Bitmap;

/**
 * 咨询 -- 对话内容
 * Created by androidjp on 2017/1/5.
 */
public class Dialogue{

    private String user_id;///null 表示robot
    private Bitmap pic;
    public String word;

    public Dialogue(String user_id,Bitmap pic,  String word) {
        this.user_id = user_id;
        this.word = word;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}
