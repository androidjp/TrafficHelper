package com.androidjp.traffichelper.history;

import android.content.Context;

import com.androidjp.lib_common_util.pojo.network.Result;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.UserManager;
import com.androidjp.traffichelper.data.model.retrofit.ServiceGenerator;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.orhanobut.logger.Logger;

import java.lang.ref.SoftReference;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 记录列表Presenter实现类
 * Created by androidjp on 2017/3/22.
 */

public class RecordListPresenter implements HistoryContract.Presenter {

    private Context mContext;
    private SoftReference<HistoryContract.View> mView;

    private int page = 0;
    private int currentCount = 0;//当前的列表一共拥有多少数据
    //TODO:网络请求时用：根据列表头元素id与数据库时间排序后第一个元素id比较，如果一样，就不需要刷新
    private String firstRecordId = null;


    public RecordListPresenter(Context mContext, HistoryContract.View view) {
        this.mContext = mContext;
        mView = new SoftReference<>(view);
    }


    private void refreshCurrentPage() {
        if (this.mView != null) {
            currentCount = this.mView.get().getRecDataList().size();
            this.page = currentCount / ServiceAPI.PAGE_COUNT;
        }
    }

    @Override
    public void start() {
        refresh();
    }

    @Override
    public void refresh() {
        if (mView == null)
            return;
        refreshCurrentPage();
        //考虑特殊情况（当前 mDataList 为空， 即：还没有加载过）
        //获取网络数据：Result<List<Record>>, page = 0
        final ServiceAPI.RecordAPI recordAPI = ServiceGenerator.createService(ServiceAPI.RecordAPI.class);
        Flowable<Result<List<Record>>> call = recordAPI.queryRecordList(
                UserManager.getInstance(THApplication.getContext()).getUserId()
                , 0
                , (this.mView.get().getRecDataList().size() == 0 ? 0 : this.mView.get().getRecDataList().get(0).record_time));
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResult -> {
                    if (listResult != null) {
                        Logger.d(listResult.toJsonString());
                        List<Record> list = listResult.data;
                        if (list == null || list.size() == 0) {
                            //无更新, 这时，mDataList无变化，mView只需要Show个SnackBar提示 "无变化" 即可
                            if (mView != null)
                                mView.get().refreshRecordList(0, null);
                        } else {
                            Logger.i("原来：Presenter中的mDataList size() --> " + currentCount);
                            if (mView != null)
                                mView.get().refreshRecordList(0, list);
                            //当前本地拥有的总完整页数，刷新：
                            refreshCurrentPage();
                            Logger.i("refreshRecordList之后：Presenter中的mDataList size() --> " + currentCount);
                        }
                    }
                }, throwable -> {
                    if (mView != null)
                        mView.get().loadFail(throwable.getMessage());
                });
    }

    @Override
    public void loadMore() {
        refreshCurrentPage();
        //TODO: 获取网络数据：Result<List<Record>>, page = this.page
        ServiceAPI.RecordAPI recordAPI = ServiceGenerator.createService(ServiceAPI.RecordAPI.class);
        Flowable<Result<List<Record>>> call = recordAPI.queryRecordList(
                UserManager.getInstance(THApplication.getContext()).getUserId()
                , this.page
                , (this.currentCount > 0 ? this.mView.get().getRecDataList().get(this.currentCount - 1).record_time : 0));
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResult -> {
                    if (listResult != null) {
                        List<Record> list = listResult.data;
                        if (list != null || list.size() == 0) {
                            ///无更新, 这时，mDataList无变化，mView只需要Show个SnackBar提示"无变化"即可
                            if (mView != null)
                                mView.get().refreshRecordList(page, null);
                        } else {
                            ///为本地mDataList加上加载到的数据
                            if (mView != null)
                                mView.get().refreshRecordList(page, list);
                            refreshCurrentPage();
                        }
                    }

                }, throwable -> {
                    if (mView != null)
                        mView.get().loadFail(throwable.getMessage());
                });
    }

    @Override
    public void loadDetail(final int pos, Record record) {
        if (record.getResult() != null)
            if (mView != null)
                mView.get().finishLoadDetail(pos, null);
        ServiceAPI.RecordAPI recordAPI = ServiceGenerator.createService(ServiceAPI.RecordAPI.class);
        Flowable<Result<RecordRes>> call = recordAPI.getRecordDetail(record.getResult_id());
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordResResult -> {
                    if (recordResResult == null || recordResResult.data == null) {
                        if (mView != null)
                            mView.get().loadFail("加载详情失败，请重试！");
                    } else {
                        if (mView != null)
                            mView.get().finishLoadDetail(pos, recordResResult.data);
                    }
                }, throwable -> {
                    if (mView != null)
                        mView.get().loadFail("加载异常~" + throwable.getMessage());
                });
    }

}
