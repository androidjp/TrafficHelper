package com.androidjp.traffichelper.home;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.RelativeItemMsg;

import java.util.List;

/**
 * Created by androidjp on 2017/1/17.
 */

public class RelativesMsgContract {
    public interface View extends BaseView<Presenter>{
        void returnMsg(String msg);
    }

    public interface Presenter extends BasePresenter{
        void statisticData(List<RelativeItemMsg> datas);
    }
}
