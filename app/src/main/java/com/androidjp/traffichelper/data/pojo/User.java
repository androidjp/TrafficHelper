package com.androidjp.traffichelper.data.pojo;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户 POJO类
 * Created by androidjp on 2016/12/27.
 */
public class User implements Cloneable,Serializable {

    //    @PrimaryKey
    @Expose
    private String user_id;
    @Expose
    private String user_name;
    //    @Required
    @Expose
    private String user_pwd;
    @Expose
    private String email;
    @Expose
    private String phone;
    @Expose
    private int sex;///0 man  1 woman
    @Expose
    private String user_pic;
    @Expose
    private int kind;//用户类型（保留）
    @Expose
    private int age;///年龄

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUser_pic() {

        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }


    public Map<String, String> getFieldMap() {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(user_name))
            map.put("user_name", user_name);
        if (!TextUtils.isEmpty(user_pwd))
            map.put("user_pwd", user_pwd);
        if (!TextUtils.isEmpty(user_pic))
            map.put("user_pic", user_pic);
        if (!TextUtils.isEmpty(email))
            map.put("email", email);
        if (!TextUtils.isEmpty(phone))
            map.put("phone", phone);
        map.put("sex", sex+"");
        map.put("age", age+"");
        map.put("kind", kind+"");
        return map;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"user_id\":\"").append(user_id)
                .append("\",\"user_pwd\":\"").append(user_pwd)
                .append("\",\"user_name\":\"").append(user_name)
                .append("\",\"email\":\"").append(email)
                .append("\",\"phone\":\"").append(phone)
                .append("\",\"user_pic\":\"").append(user_pic)
                .append("\",\"age\":").append(age)
                .append(",\"sex\":").append(sex)
                .append(",\"kind\":").append(kind)
                .append("}");
        return sb.toString();
    }
}