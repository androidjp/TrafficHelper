package com.androidjp.traffichelper.result;

import android.content.Context;

import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.ResultInfo;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidjp on 2017/3/20.
 */

public class ResultPresenter implements ResultContract.Presenter{
    private Context mContext;
    private SoftReference<ResultContract.View> mView;
    private RecordRes recordRes;

    public ResultPresenter(Context mContext, ResultContract.View mView) {
        this.mContext = mContext;
        this.mView = new SoftReference<ResultContract.View>(mView);
    }

    @Override
    public void start() {

    }


    @Override
    public void prepareDatas(RecordRes data) {
        this.recordRes = data;
        List<ResultInfo> list = new ArrayList<>();
        list.add(new ResultInfo(null,data.money_pay,null));
        list.add(new ResultInfo("安葬费",data.money_bury,data.money_bury_info));
        list.add(new ResultInfo("受伤费用",data.money_hurt,data.money_hurt_info));
        list.add(new ResultInfo("精神损失费",data.money_heart,data.money_heart_info));
        list.add(new ResultInfo("医疗费",data.money_hospital_allowance,data.money_hospital_allowance_info));
        list.add(new ResultInfo("医药费",data.money_medical,data.money_medical_info));
        list.add(new ResultInfo("护理费",data.money_nursing,data.money_nursing_info));
        list.add(new ResultInfo("营养花费",data.money_nutrition,data.money_nutrition_info));

        if (this.mView !=null)
            this.mView.get().showDatas(list);
    }
}
