package com.androidjp.traffichelper.history;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.Record;

import java.util.List;

/**
 * 理赔历史记录
 * Created by androidjp on 2017/3/22.
 */

public class HistoryContract {
    public interface View extends BaseView<Presenter>{
        void refreshRecordList(int page,List<Record> recordList);
        void loadFail(String msg);
    }

    public interface Presenter extends BasePresenter{
        void refresh();
        void loadMore();
    }
}
