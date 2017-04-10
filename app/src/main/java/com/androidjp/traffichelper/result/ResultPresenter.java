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
//        list.add(new ResultInfo("安葬费",data.money_bury,mContext.getResources().getString(R.string.loyal_bury)));
//        list.add(new ResultInfo("受伤费用",data.money_hurt,mContext.getResources().getString(R.string.loyal_hurt)));
//        list.add(new ResultInfo("精神损失费",data.money_heart,mContext.getResources().getString(R.string.loyal_heart)));
//        list.add(new ResultInfo("医疗费",data.money_hospital_allowance,data.money_hospital_allowance_info));
//        list.add(new ResultInfo("医药费",data.money_medical,data.money_medical_info));
//        list.add(new ResultInfo("护理费",data.money_nursing,mContext.getResources().getString(R.string.loyal_nursing)));
//        list.add(new ResultInfo("营养花费",data.money_nutrition,"营养花费 = 营养期 x 每日平均营养花费"));
        list.add(new ResultInfo("安葬费", data.money_bury, "交通事故误工费：\n" +
                "最高人民法院关于审理人身损害赔偿案件\n" +
                "适用法律若干问题的解释\n" +
                "第二十条\n" +
                "误工费根据受害人的误工时间和收入状况确定。\n" +
                "误工时间根据受害人接收治疗机构出具的证明确定。受害人因伤残持续误工的，误工时间可以计算至定残疾日前一天。\n" +
                "受害人有固定收入的，误工费按照实际减少的收入计算。受害人无固定收入的，按照其最近三年的平均收入计算；受害人不能举证证明其最近三年的平均收入状况的，可以参照受诉讼法院所在地相同或者相近行业上一年度职工的平均工资计算。"));
        list.add(new ResultInfo("受伤费用", data.money_hurt, mContext.getResources().getString(R.string.loyal_hurt)));
        list.add(new ResultInfo("精神损失费", data.money_heart, mContext.getResources().getString(R.string.loyal_heart)));
        list.add(new ResultInfo("医疗费", data.money_hospital_allowance, data.money_hospital_allowance_info));
        list.add(new ResultInfo("医药费", data.money_medical, data.money_medical_info));
        list.add(new ResultInfo("护理费", data.money_nursing, mContext.getResources().getString(R.string.loyal_nursing)));
        list.add(new ResultInfo("营养花费", data.money_nutrition, "营养花费 = 营养期 x 每日平均营养花费"));


        if (this.mView != null)
            this.mView.get().showDatas(list);
    }
}
