package com.androidjp.traffichelper.history;

import android.content.Context;

import com.androidjp.traffichelper.data.pojo.Location;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
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

    @Override
    public void start() {
        refresh();
    }

    @Override
    public void refresh() {

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.onNext(0);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .map(new Function<Integer, List<Record>>() {
                    @Override
                    public List<Record> apply(Integer page) throws Exception {
                        List<Record> records = new ArrayList<>();
                        for (int i=0;i<5;i++){
                            Record record = new Record();
                            Location location = new Location("广州市","广东省","天河区",0,0);
                            RecordRes result = new RecordRes();
                            result.money_pay = (i+1)*10000;
                            record.setLocation(location);
                            record.setResult(result);
                            records.add(record);
                        }
                        return records;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Record>>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Logger.i(RecordListPresenter.class.getSimpleName(), "onSubscribe()");
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<Record> records) {
                        Logger.i(RecordListPresenter.class.getSimpleName(), "onNext()");
                        if (records==null || records.size()==0){
                            if (mView!=null)
                                mView.get().loadFail("加载更多失败，没有更多数据了~~");
                            return;
                        }
                        if (mView != null)
                            mView.get().refreshRecordList(0, records);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Logger.i(RecordListPresenter.class.getSimpleName(), "onComplete()");
                    }
                });

    }

    @Override
    public void loadMore() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.onNext(page);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .map(new Function<Integer, List<Record>>() {
                    @Override
                    public List<Record> apply(Integer page) throws Exception {
                        List<Record> records = new ArrayList<>();
                        for (int i=0;i<5;i++){
                            Record record = new Record();
                            Location location = new Location("广州市","广东省","天河区",0,0);
                            RecordRes result = new RecordRes();
                            result.money_pay = (i+1)*10000;
                            record.setLocation(location);
                            record.setResult(result);
                            records.add(record);
                        }
                        return records;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Record>>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Logger.i(RecordListPresenter.class.getSimpleName(), "onSubscribe()");
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<Record> records) {
                        Logger.i(RecordListPresenter.class.getSimpleName(), "onNext()");
                        if (records==null || records.size()==0){
                            if (mView!=null)
                                mView.get().loadFail("加载更多失败，没有更多数据了~~");
                            return;
                        }
                        page +=1;
                        if (mView != null)
                            mView.get().refreshRecordList(page, records);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Logger.i(RecordListPresenter.class.getSimpleName(), "onComplete()");
                    }
                });
    }


}
