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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    public RecordListPresenter(Context mContext,HistoryContract.View view) {
        this.mContext = mContext;
        mView = new SoftReference<HistoryContract.View>(view);
    }


    private void refreshCurrentPage(){
        if (this.mView!=null){
            currentCount =this.mView.get().getRecDataList().size();
            this.page = currentCount/ServiceAPI.PAGE_COUNT;
        }
    }

    @Override
    public void start() {
        refresh();
    }

    @Override
    public void refresh() {
        if (mView==null)
            return;
        refreshCurrentPage();
        //TODO:考虑特殊情况（当前 mDataList 为空， 即：还没有加载过）
        //TODO: 获取网络数据：Result<List<Record>>, page = 0
        final ServiceAPI.RecordAPI recordAPI = ServiceGenerator.createService(ServiceAPI.RecordAPI.class);
        Call<Result<List<Record>>> call = recordAPI.queryRecordList(
                UserManager.getInstance(THApplication.getContext()).getUserId()
                , 0
                , (this.mView.get().getRecDataList().size()==0?0:this.mView.get().getRecDataList().get(0).record_time));
        call.enqueue(new Callback<Result<List<Record>>>() {
            @Override
            public void onResponse(Call<Result<List<Record>>> call, Response<Result<List<Record>>> response) {
                if (response.body()!=null){
                    Logger.d(response.body().toJsonString());
                    List<Record> list = response.body().data;
                    if (list==null || list.size()==0){
                        ///无更新, 这时，mDataList无变化，mView只需要Show个SnackBar提示"无变化"即可
                        if (mView != null)
                            mView.get().refreshRecordList(0,null);
                    }else{
                        Logger.i("原来：Presenter中的mDataList size() --> "+ currentCount);
                        if (mView!=null)
                            mView.get().refreshRecordList(0,list);
                        //当前本地拥有的总完整页数，刷新：
                        refreshCurrentPage();
                        Logger.i("refreshRecordList之后：Presenter中的mDataList size() --> "+ currentCount);
                    }

                }
            }

            @Override
            public void onFailure(Call<Result<List<Record>>> call, Throwable t) {
                if (mView != null)
                    mView.get().loadFail(t.getMessage());
            }
        });

//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
//                e.onNext(0);
//                e.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER)
//                .map(new Function<Integer, List<Record>>() {
//                    @Override
//                    public List<Record> apply(Integer page) throws Exception {
//                        List<Record> records = new ArrayList<>();
//                        for (int i=0;i<5;i++){
//                            Record record = new Record();
//                            Location location = new Location("广州市","广东省","天河区",0,0);
//                            RecordRes result = new RecordRes();
//                            result.money_pay = (i+1)*10000;
//                            record.setLocation(location);
//                            record.setResult(result);
//                            records.add(record);
//                        }
//                        return records;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Record>>() {
//
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Logger.i(RecordListPresenter.class.getSimpleName(), "onSubscribe()");
//                        s.request(1);
//                    }
//
//                    @Override
//                    public void onNext(List<Record> records) {
//                        Logger.i(RecordListPresenter.class.getSimpleName(), "onNext()");
//                        if (records==null || records.size()==0){
//                            if (mView!=null)
//                                mView.get().loadFail("加载更多失败，没有更多数据了~~");
//                            return;
//                        }
//                        if (mView != null)
//                            mView.get().refreshRecordList(0, records);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        t.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Logger.i(RecordListPresenter.class.getSimpleName(), "onComplete()");
//                    }
//                });

    }

    @Override
    public void loadMore() {
        refreshCurrentPage();
        //TODO: 获取网络数据：Result<List<Record>>, page = this.page
        ServiceAPI.RecordAPI recordAPI = ServiceGenerator.createService(ServiceAPI.RecordAPI.class);
        Call<Result<List<Record>>> call = recordAPI.queryRecordList(
                UserManager.getInstance(THApplication.getContext()).getUserId()
                , this.page
                , (this.currentCount>0?this.mView.get().getRecDataList().get(this.currentCount-1).record_time:0));
        call.enqueue(new Callback<Result<List<Record>>>() {
            @Override
            public void onResponse(Call<Result<List<Record>>> call, Response<Result<List<Record>>> response) {
                if (response.body()!=null){
                    List<Record> list = response.body().data;
                    if (list!=null || list.size()==0){
                        ///无更新, 这时，mDataList无变化，mView只需要Show个SnackBar提示"无变化"即可
                        if (mView != null)
                            mView.get().refreshRecordList(page,null);
                    } else{
                       ///为本地mDataList加上加载到的数据
                        if (mView!=null)
                            mView.get().refreshRecordList(page,list);
                        refreshCurrentPage();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<Record>>> call, Throwable t) {
                if (mView != null)
                    mView.get().loadFail(t.getMessage());
            }
        });

//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
//                e.onNext(page);
//                e.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER)
//                .map(new Function<Integer, List<Record>>() {
//                    @Override
//                    public List<Record> apply(Integer page) throws Exception {
//                        List<Record> records = new ArrayList<>();
//                        for (int i=0;i<5;i++){
//                            Record record = new Record();
//                            Location location = new Location("广州市","广东省","天河区",0,0);
//                            RecordRes result = new RecordRes();
//                            result.money_pay = (i+1)*10000;
//                            record.setLocation(location);
//                            record.setResult(result);
//                            records.add(record);
//                        }
//                        return records;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Record>>() {
//
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Logger.i(RecordListPresenter.class.getSimpleName(), "onSubscribe()");
//                        s.request(1);
//                    }
//
//                    @Override
//                    public void onNext(List<Record> records) {
//                        Logger.i(RecordListPresenter.class.getSimpleName(), "onNext()");
//                        if (records==null || records.size()==0){
//                            if (mView!=null)
//                                mView.get().loadFail("加载更多失败，没有更多数据了~~");
//                            return;
//                        }
//                        page +=1;
//                        if (mView != null)
//                            mView.get().refreshRecordList(page, records);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        t.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Logger.i(RecordListPresenter.class.getSimpleName(), "onComplete()");
//                    }
//                });
    }

    @Override
    public void loadDetail(final int pos, Record record) {
        if (record.getResult()!=null)
            if (mView!=null)
                mView.get().finishLoadDetail(pos,null);
        ServiceAPI.RecordAPI recordAPI = ServiceGenerator.createService(ServiceAPI.RecordAPI.class);
        Call<Result<RecordRes>> call = recordAPI.getRecordDetail(record.getResult_id());
        call.enqueue(new Callback<Result<RecordRes>>() {
            @Override
            public void onResponse(Call<Result<RecordRes>> call, Response<Result<RecordRes>> response) {
                if (response.body()==null || response.body().data==null) {
                    if (mView != null)
                        mView.get().loadFail("加载详情失败，请重试！");
                }else {
                    if (mView!=null)
                        mView.get().finishLoadDetail(pos, response.body().data);
                }
            }

            @Override
            public void onFailure(Call<Result<RecordRes>> call, Throwable t) {
                if (mView!=null)
                    mView.get().loadFail("加载异常~ "+ t.getMessage());
            }
        });
    }


}
