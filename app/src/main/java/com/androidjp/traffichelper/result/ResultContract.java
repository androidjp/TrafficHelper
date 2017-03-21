package com.androidjp.traffichelper.result;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.ResultInfo;

import java.util.List;

/**
 * 理赔计算结果业务相关
 * Created by androidjp on 2017/3/20.
 */

public class ResultContract {
    public interface View extends BaseView<Presenter>{
        void showDatas(List<ResultInfo> list);
    }

    public interface Presenter extends BasePresenter{
        void prepareDatas(RecordRes data);
    }
}
