package com.androidjp.traffichelper.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 理赔计算结果
 * Created by androidjp on 2016/12/27.
 */
public class RecordRes implements Parcelable{

//    @PrimaryKey
    private String result_id = null;//主键
    public float money_pay;///总花费（两位小数）
    public float money_hurt;//伤残赔偿金
    public float money_heart;//精神损失费
    public float money_nursing;//护理费
    public float money_tardy;//误工费
    public float money_medical;//医药费
    public float money_nutrition;//营养费
    public float money_hospital_allowance;//住院伙食补贴
    public float money_bury; //安葬费
    ///各个详情
    public String money_hurt_info;
    public String money_heart_info;
    public String money_nursing_info;//护理费
    public String money_tardy_info;//误工费
    public String money_medical_info;//医药费
    public String money_nutrition_info;//营养费
    public String money_hospital_allowance_info;//住院伙食补贴
    public String money_bury_info; //安葬费

    public RecordRes(){

    }

    protected RecordRes(Parcel in) {
        result_id = in.readString();
        money_pay = in.readFloat();
        money_hurt = in.readFloat();
        money_heart = in.readFloat();
        money_nursing = in.readFloat();
        money_tardy = in.readFloat();
        money_medical = in.readFloat();
        money_nutrition = in.readFloat();
        money_hospital_allowance = in.readFloat();
        money_bury = in.readFloat();
        money_hurt_info = in.readString();
        money_heart_info = in.readString();
        money_nursing_info = in.readString();
        money_tardy_info = in.readString();
        money_medical_info = in.readString();
        money_nutrition_info = in.readString();
        money_hospital_allowance_info = in.readString();
        money_bury_info = in.readString();
    }

    public static final Creator<RecordRes> CREATOR = new Creator<RecordRes>() {
        @Override
        public RecordRes createFromParcel(Parcel in) {
            return new RecordRes(in);
        }

        @Override
        public RecordRes[] newArray(int size) {
            return new RecordRes[size];
        }
    };

    public String getResult_id() {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_id);
        dest.writeFloat(money_pay);
        dest.writeFloat(money_hurt);
        dest.writeFloat(money_heart);
        dest.writeFloat(money_medical);
        dest.writeFloat(money_tardy);
        dest.writeFloat(money_nursing);
        dest.writeFloat(money_nutrition);
        dest.writeFloat(money_hospital_allowance);
        dest.writeFloat(money_bury);
        dest.writeString(money_hurt_info);
        dest.writeString(money_heart_info);
        dest.writeString(money_nursing_info);//护理费
        dest.writeString(money_tardy_info);//误工费
        dest.writeString(money_medical_info);//医药费
        dest.writeString(money_nutrition_info);//营养费
        dest.writeString(money_hospital_allowance_info);//住院伙食补贴
        dest.writeString(money_bury_info); //安葬费
    }
}
