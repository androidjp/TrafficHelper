package com.androidjp.traffichelper.data.pojo;


import org.litepal.crud.DataSupport;

/**
 * 咨询 -- 对话内容
 * Created by androidjp on 2017/1/5.
 */
public class Dialogue extends DataSupport{

    private String user_id;///null 表示robot
    public String word;
    private long dialogue_time;///记录问答时间

    public Dialogue(String user_id, String word) {
        this.user_id = user_id;
        this.word = word;
        this.dialogue_time = System.currentTimeMillis();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public long getDialogue_time() {
        return dialogue_time;
    }

    public void setDialogue_time(long dialogue_time) {
        this.dialogue_time = dialogue_time;
    }
}
