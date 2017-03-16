package com.androidjp.traffichelper.data.pojo;

/**
 * 亲属抚养关系item
 * relation：0：子女 1：父母
 * age：[1,55] 的整数
 * Created by androidjp on 2017/1/17.
 */
public class RelativeItemMsg {
//    @PrimaryKey
    private int relativeItemMsg_id = -1;
//    @Required
    private int record_id = -1;
    private int relation;
    private int age;

    public RelativeItemMsg(int relation, int age) {
        this.relation = relation;
        this.age = age;
    }



    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRelativeItemMsg_id() {
        return relativeItemMsg_id;
    }

    public void setRelativeItemMsg_id(int relativeItemMsg_id) {
        this.relativeItemMsg_id = relativeItemMsg_id;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }
}
