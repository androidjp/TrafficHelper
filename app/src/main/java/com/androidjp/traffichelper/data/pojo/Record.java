package com.androidjp.traffichelper.data.pojo;

import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.model.UserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 理赔记录：
 * <p>
 * Created by androidjp on 2016/12/25.
 */
public class Record implements Cloneable {

//    @PrimaryKey
    private int record_id =  -1;//（主键）
//    @Required
    private String user_id;///用户ID（外键）
    public int location_id = -1;///定位（外键）
//    @Ignore
    public Location location;//实际定位信息
//    @Required
    public long record_time;
    public int hurt_level;
    public long salary;//月薪
    public int relatives_count;//兄弟姐妹数量
    public boolean has_spouse;//有无配偶（false：无， true：有）
    public int id_type;///户口性质（城镇、农村）
    public int responsibility;//责任：全责、次要责任、同等责任、主要责任、无责
    public int driving_tools;///交通工具
    public float medical_free;//医药费
    public int hospital_days;
    public int tardy_days;///误工天数
    public int nutrition_days;//营养期
    public int nursing_days;//护理期
    private int result_id = -1;///结果（外键）
//    @Ignore
    private RecordRes result;///结果
//    private RealmList<RelativeItemMsg> relative_msg_list;


    public Record() {
        this.location_id = -1;
        this.user_id = UserManager.getInstance(THApplication.getContext()).getUserId();
    }

    public RecordRes getResult() {
        return (result_id == -1) ? null : result;
    }

    public void setResult(RecordRes result) {
        this.result = result;
        this.result_id = this.result.getResult_id();
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getUser_id() {
        return user_id;
    }


    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

//    public RealmList<RelativeItemMsg> getRelative_msg_list() {
//        return relative_msg_list;
//    }
//
//    public void setRelative_msg_list(RealmList<RelativeItemMsg> relative_msg_list) {
//        this.relative_msg_list = relative_msg_list;
//    }

    @Override
    protected Record clone() throws CloneNotSupportedException {
        return (Record) super.clone();
    }



    /**
     * 将记录的数据复制到维护的对象中
     * @param record 传来的记录数据
     */
    public void copyData(Record record) {
        if (record.result_id != -1 && record.result != null) {
            this.result = record.result;
            this.result_id = record.result.getResult_id();
        }
        if (!(record.getLocation() == null || record.location_id == -1)) {
            this.setLocation(record.location);
            this.location_id = this.location.getLocation_id();
        }
        this.hurt_level = record.hurt_level;
        this.salary = record.salary;//月薪
        this.relatives_count = record.relatives_count;//兄弟姐妹数量
        this.has_spouse = record.has_spouse;//有无配偶（false：无， true：有）
        this.id_type = record.id_type;///户口性质（城镇、农村）
        this.responsibility = record.responsibility;//责任：全责、次要责任、同等责任、主要责任、无责
        this.driving_tools = record.driving_tools;///交通工具
        this.medical_free = record.medical_free;//医药费
        this.hospital_days = record.hospital_days;
        this.tardy_days = record.tardy_days;///误工天数
        this.nutrition_days = record.nutrition_days;//营养期
        this.nursing_days = record.nursing_days;//营养期
//        if (this.relative_msg_list == null)
//            this.relative_msg_list = new RealmList<>();
//
//        this.relative_msg_list.clear();
//        if (record.relative_msg_list != null)
//            this.relative_msg_list.addAll(record.relative_msg_list);
    }

    public Map<String ,Object> getFieldMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("location", location);
        map.put("record_time", record_time);
        map.put("hurt_level", hurt_level);
        map.put("salary", salary);
        map.put("relatives_count",relatives_count);
        map.put("has_spouse",has_spouse);
        map.put("id_type",id_type);
        map.put("responsibility",responsibility);
        map.put("driving_tools",driving_tools);
        map.put("medical_free",medical_free);
        map.put("hospital_days",hospital_days);
        map.put("tardy_days",tardy_days);
        map.put("nutrition_days",nutrition_days);
        map.put("nursing_days",nursing_days);
        return map;
    }
}
