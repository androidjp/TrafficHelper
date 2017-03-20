package com.androidjp.traffichelper.data.pojo;

import java.util.Map;

/**
 * 理赔计算结果
 * Created by androidjp on 2016/12/27.
 */
public class RecordRes {

//    @PrimaryKey
    private int result_id = -1;//主键
    public float money_pay;///总花费（两位小数）
    public float money_hurt;//伤残赔偿金
    public float money_heart;//精神损失费
    public float money_nursing;//护理费
    public float money_tardy;//误工费
    public float money_medical;//医药费
    public float money_nutrition;//营养费
    public float money_hospital_allowance;//住院伙食补贴
    public float money_bury; //安葬费
    ///各个详情（内容，对应url链接）
    public Map<String, String> money_hurt_info;
    public Map<String, String> money_heart_info;
    public Map<String, String> money_nursing_info;
    public Map<String, String> money_tardy_info;
    public Map<String, String> money_medical_info;
    public Map<String, String> money_nutrition_info;
    public Map<String, String> money_hospital_info;
    public Map<String, String> money_bury_info;


    public int getResult_id() {

        return result_id;
    }

    //计算所有费用
    public void calculateAllPay(){
        money_pay = money_hurt
                + money_heart
                + money_hospital_allowance
                + money_nursing
                + money_nutrition
                + money_medical
                + money_tardy
                + money_bury;
    }
}
