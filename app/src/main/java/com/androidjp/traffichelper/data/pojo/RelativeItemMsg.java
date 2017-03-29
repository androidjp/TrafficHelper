package com.androidjp.traffichelper.data.pojo;

import com.androidjp.lib_common_util.data.StringRandomUtil;

/**
 * 亲属抚养关系item
 * relation：0：子女 1：父母
 * age：[1,55] 的整数
 * Created by androidjp on 2017/1/17.
 */
public class RelativeItemMsg {
    //    @PrimaryKey
    private String relativeItemMsg_id;
    //    @Required
    private String record_id = null;
    private int relation;
    private int age;

    public RelativeItemMsg(int relation, int age) {
        this.relativeItemMsg_id = StringRandomUtil.getStringRandom(10);
        this.relation = relation;
        this.age = age;
    }

    public String getRelativeItemMsg_id() {
        return relativeItemMsg_id;
    }

    public void setRelativeItemMsg_id(String relativeItemMsg_id) {
        this.relativeItemMsg_id = relativeItemMsg_id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }
}
