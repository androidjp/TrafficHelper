package com.androidjp.traffichelper.result;

import android.content.Context;

import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.ResultInfo;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidjp on 2017/3/20.
 */

public class ResultPresenter implements ResultContract.Presenter {
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

        //读取 xml loyal_config 中的相关法律


        List<ResultInfo> list = new ArrayList<>();
        list.add(new ResultInfo(null, data.money_pay, null));
        if(this.recordRes.money_bury>0)
        list.add(new ResultInfo("安葬费",data.money_bury,data.money_bury_info+"\n"+ mContext.getResources().getString(R.string.loyal_bury)));
        if (this.recordRes.money_hurt>0)
        list.add(new ResultInfo("受伤费用",data.money_hurt,data.money_hurt_info+"\n"+mContext.getResources().getString(R.string.loyal_hurt)));
        if (this.recordRes.money_heart>0)
        list.add(new ResultInfo("精神损失费",data.money_heart, data.money_heart_info +"\n"+ mContext.getResources().getString(R.string.loyal_heart)));
        if (this.recordRes.money_hospital_allowance>0)
        list.add(new ResultInfo("医疗费",data.money_hospital_allowance,data.money_hospital_allowance_info));
        if (this.recordRes.money_medical>0)
            list.add(new ResultInfo("医药费",data.money_medical,data.money_medical_info));
        if (this.recordRes.money_nursing>0)
            list.add(new ResultInfo("护理费",data.money_nursing,data.money_nursing_info +"\n"+ mContext.getResources().getString(R.string.loyal_nursing)));
        if (this.recordRes.money_nutrition>0)
            list.add(new ResultInfo("营养花费",data.money_nutrition,data.money_nutrition_info));
        if (this.recordRes.money_relatives>0)
            list.add(new ResultInfo("总赡养费",data.money_relatives, data.money_relatives_info));

        if (this.mView != null)
            this.mView.get().showDatas(list);
    }
}
