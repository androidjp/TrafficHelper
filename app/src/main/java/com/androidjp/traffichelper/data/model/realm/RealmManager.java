package com.androidjp.traffichelper.data.model.realm;

import android.content.Context;
/*

import android.widget.Toast;

import com.androidjp.traffichelper.data.pojo.Location;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.User;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
*/

/**
 * Realm 的单例管理类，持久化处理相关
 * Created by androidjp on 2017/2/1.
 */

public class RealmManager {

   /* private Context mContext;
    private Realm mRealm;
    private static RealmManager sInstance;

    private RealmManager(Context context){
        this.mContext = context;
        initRealm();
    }




    private void initRealm() {
        ///创建一个default.realm 的文件，位于/data/data/包名/files/ 目录
//        Realm.init(this.mContext);
//        mRealm = Realm.getDefaultInstance();
        ///初始化方式二
        RealmConfiguration configuration =new RealmConfiguration.Builder()
                .name("traffic_helper.realm")///文件名
                .schemaVersion(0)///版本
//                .inMemory()////创建非持久化的reaml文件（只保存在内存中// ）
                .build();
        this.mRealm = Realm.getInstance(configuration);
    }

    public static RealmManager getInstance(Context context){
        if (sInstance == null)
            synchronized (RealmManager.class){
                if (sInstance == null)
                    sInstance = new RealmManager(context);
            }
        return sInstance;
    }

    private void checkRealm(){
        if (this.mRealm==null)
            initRealm();
    }


    *//**
     * 释放内存，解除引用
     * 在onDestroy方法中调用
     *//*
    public void release(){
        if (this.mRealm!=null){
            this.mRealm.close();
            this.mRealm = null;
        }
    }

    *//**
     * onStop() 方法中调用，用于停止transaction
     *//*
    public void shop(RealmAsyncTask task){
        if (!task.isCancelled())
            task.cancel();
    }


    *//**
     * ==增删查改===============================================================
     *//*

*//*

    ///获取对象，开始准备想写入对象
     public <T extends RealmObject>T generateObject(Class<T> clazz){
         checkRealm();
         return this.mRealm.createObject(clazz);
     }

    ///获取Realm对象
    public Realm getRealm(){
        return this.mRealm;
    }


    //用户添加
    public RealmAsyncTask addUser(final User user){
        checkRealm();

        return this.mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
//                User user = realm.createObject(User.class);
                realm.copyToRealmOrUpdate(user);
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                Toast.makeText(mContext,"成功添加/更新用户",Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext,"添加/更新用户失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }


    ///用户查找
    public void findUser(final MyRealmChangeListListener<User> listener){
        checkRealm();
        final RealmResults<User> realmResults =  this.mRealm.where(User.class).findAllAsync();
        this.mRealm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (realmResults.isLoaded()){
                    if (listener!=null)
                        listener.onChange(realmResults);
                }
            }
        });
    }


    // 添加 记录
    public RealmAsyncTask addRecord(final Record record){
        checkRealm();

        final Location location = record.getLocation();
        final RecordRes recordRes = record.getResult();

        return this.mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(record);
                realm.copyToRealmOrUpdate(location);
                realm.copyToRealmOrUpdate(recordRes);
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                Toast.makeText(mContext,"成功添加记录",Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError(){

            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext,"添加/更新记录失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    ///获取历史计算记录
    public  void findRecordList(final MyRealmChangeListListener<Record> listener){
        checkRealm();
         RealmResults<Record> realmResults =  this.mRealm.where(Record.class).findAllAsync();
        realmResults = realmResults.sort("record_time", Sort.DESCENDING);
        final RealmResults<Record> finalRealmResults = realmResults;
        this.mRealm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (finalRealmResults.isLoaded()){
                    if (listener!=null)
                        listener.onChange(finalRealmResults);
                }
            }
        });
    }

    ///TODO：（每次打开App都弹出一个窗口"您上一次计算还未完成，继续么？"）获取未计算的暂存记录
    public void findRecord(final MyRealmChangeListener<Record> listener){
        checkRealm();
        final Record realmResults =  this.mRealm.where(Record.class).findFirstAsync();
        this.mRealm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (realmResults!=null){
                    if (listener!=null)
                        listener.onChange(realmResults);
                }
            }
        });
    }

*//*


    public interface MyRealmChangeListListener<V extends RealmModel>{
        public void onChange(RealmResults<V> result);
    }

    public interface MyRealmChangeListener<V>{
        public void onChange(V result);
    }

*/

}
