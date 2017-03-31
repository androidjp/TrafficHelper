package com.androidjp.traffichelper.data.pojo;

/**
 * 定位信息
 * Created by androidjp on 2017/1/4.
 */
public class Location{
    //    @PrimaryKey
    private String location_id;
    public String city;
    public String province;
    public String street;
    public double latitude;//纬度
    public double longitude;//经度

    public Location(String city, String province, String street, double latitude, double longitude) {
        this.city = city;
        this.province = province;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    @Override
    public String toString() {
        return province+city+street;
    }
}
