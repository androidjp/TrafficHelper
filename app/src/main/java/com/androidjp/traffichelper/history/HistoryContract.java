package com.androidjp.traffichelper.history;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;

import java.util.List;

/**
 * 理赔历史记录
 * Created by androidjp on 2017/3/22.
 */

public class HistoryContract {
    public interface View extends BaseView<Presenter>{
        List<Record> getRecDataList();
        void refreshRecordList(int page,List<Record> recordList);
        void finishLoadDetail(int pos, RecordRes recordRes);
        void loadFail(String msg);
    }

    public interface Presenter extends BasePresenter{
        //下拉刷新列表
        void refresh();
        //上拉加载更多
        void loadMore();
        //加载详情与结果
        void loadDetail(int pos,Record record);
    }
}
