package com.androidjp.traffichelper.data.pojo;

import com.androidjp.lib_common_util.data.NumberUtil;
import com.androidjp.lib_common_util.data.StringRandomUtil;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.model.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 理赔记录：
 * <p>
 * Created by androidjp on 2016/12/25.
 */
public class Record implements Cloneable {

    //    @PrimaryKey
    private String record_id;//（主键）
    //    @Required
    private String user_id;///用户ID（外键）
    private String location_id;///定位（外键）
    //    @Ignore
    public Location location;//实际定位信息
    //    @Required
    public long record_time;
    public int hurt_level;
    public float salary;//月薪
    public int relatives_count;//兄弟姐妹数量
    public boolean has_spouse;//有无配偶（false：无， true：有）
    public int id_type;///户口性质（城镇、农村）
    public int responsibility;//责任：全责、次要责任、同等责任、主要责任、无责
    public int driving_tools;///交通工具
    public float medical_free;//医药费
    public int hospital_days;
    public int tardy_days;///误工天数
    public int nutrition_days;//营养期
    public int nursing_days;///护理期
    private String result_id;///结果（外键）
    //    @Ignore
    private RecordRes result;///结果
    private float pay;///总理赔金额（后端保存时赋予的）
    //    private RealmList<RelativeItemMsg> relative_msg_list;
    private List<RelativeItemMsg> relative_msg_list;

    public Record() {
        this.user_id = UserManager.getInstance(THApplication.getContext()).getUserId();
        this.record_id = StringRandomUtil.getStringRandom(20);
        this.result_id = StringRandomUtil.getStringRandom(20);
        this.location_id = StringRandomUtil.getStringRandom(20);
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null)
            return;
        this.location = location;
        if (location.getLocation_id() == null) {
            this.location.setLocation_id(this.location_id);
        }
        if (!this.location_id.equals(this.location.getLocation_id())) {
            this.location_id = this.location.getLocation_id();
        }
    }

    public RecordRes getResult() {
        return result;
    }

    public void setResult(RecordRes result) {
        if (result == null)
            return;
        this.result = result;
        if (this.result_id != null && this.result.getResult_id() == null)
            this.result.setResult_id(this.result_id);
        if (!this.result_id.equals(this.result.getResult_id())) {
            this.result = null;
            return;
        }
        this.pay = this.result.money_pay;
    }

    public String getResult_id() {
        return result_id;
    }

    public void setResult_id(String result_id) {
        this.result_id = result_id;
    }

    //    public RealmList<RelativeItemMsg> getRelative_msg_list() {
//        return relative_msg_list;
//    }
//
//    public void setRelative_msg_list(RealmList<RelativeItemMsg> relative_msg_list) {
//        this.relative_msg_list = relative_msg_list;
//    }


    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public List<RelativeItemMsg> getRelative_msg_list() {
        return relative_msg_list;
    }

    public void setRelative_msg_list(List<RelativeItemMsg> relative_msg_list) {
        this.relative_msg_list = relative_msg_list;
    }

    public String getPay() {
        return NumberUtil.doubleRestStr(pay);
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public Map<String, Object> getFieldMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("record_id", record_id);
        map.put("user_id", user_id);
        map.put("location_id", location_id);
        map.put("location", location);
        map.put("record_time", record_time);
        map.put("hurt_level", hurt_level);
        map.put("salary", salary);
        map.put("relatives_count", relatives_count);
        map.put("has_spouse", has_spouse);
        map.put("id_type", id_type);
        map.put("responsibility", responsibility);
        map.put("driving_tools", driving_tools);
        map.put("medical_free", medical_free);
        map.put("hospital_days", hospital_days);
        map.put("tardy_days", tardy_days);
        map.put("nutrition_days", nutrition_days);
        map.put("nursing_days", nursing_days);
        map.put("result_id", result_id);
        map.put("relative_msg_list", relative_msg_list);
        return map;
    }

    public static class Builder {
        private Record record = null;

        public Builder() {
            init();
        }

        public Builder init() {
            record = new Record();
            return this;
        }

        public Builder setLocation(Location location) {
            if (location != null) {
                if (location.getLocation_id() == null)
                    location.setLocation_id(StringRandomUtil.getStringRandom(20));
                record.location = location;
                record.location_id = location.getLocation_id();
            }
            return this;
        }

        public Builder setHurtLevel(int hurtLevel) {
            this.record.hurt_level = hurtLevel;
            return this;
        }

        public Builder setSalary(float salary) {
            this.record.salary = salary;
            return this;
        }

        public Builder setRelativesCount(int relativesCount) {
            this.record.relatives_count = relativesCount;
            return this;
        }

        public Builder setHasSpouse(boolean hasSpouse) {
            this.record.has_spouse = hasSpouse;
            return this;
        }

        public Builder setIdType(int idType) {
            this.record.id_type = idType;
            return this;
        }

        public Builder setResponsibility(int responsibility) {
            this.record.responsibility = responsibility;
            return this;
        }

        public Builder setDriveingTools(int drivingTools) {
            this.record.driving_tools = drivingTools;
            return this;
        }

        public Builder setMedicalFree(int medicalFree) {
            this.record.medical_free = medicalFree;
            return this;
        }

        public Builder setHospitalDays(int hospitalDays) {
            this.record.hospital_days = hospitalDays;
            return this;
        }

        public Builder setTardyDays(int tardyDays) {
            this.record.tardy_days = tardyDays;
            return this;
        }

        public Builder setNutritionDays(int nutritionDays) {
            this.record.nutrition_days = nutritionDays;
            return this;
        }

        public Builder setNursingDays(int nursingDays) {
            this.record.nursing_days = nursingDays;
            return this;
        }

        public Record build() {
            this.record.record_time = System.currentTimeMillis();
            return this.record;
        }
    }

    @Override
    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("record_id", record_id);
            jsonObject.put("user_id", user_id);
            jsonObject.put("location_id", location_id);
            jsonObject.put("location", location);
            jsonObject.put("record_time", record_time);
            jsonObject.put("hurt_level", hurt_level);
            jsonObject.put("salary", salary);
            jsonObject.put("relatives_count", relatives_count);
            jsonObject.put("has_spouse", has_spouse);
            jsonObject.put("id_type", id_type);
            jsonObject.put("responsibility", responsibility);
            jsonObject.put("driving_tools", driving_tools);
            jsonObject.put("medical_free", medical_free);
            jsonObject.put("hospital_days", hospital_days);
            jsonObject.put("tardy_days", tardy_days);
            jsonObject.put("nutrition_days", nutrition_days);
            jsonObject.put("nursing_days", nursing_days);
            jsonObject.put("result_id", result_id);
            jsonObject.put("result", result);
            jsonObject.put("relative_msg_list", relative_msg_list);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJsonString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
