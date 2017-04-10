package com.androidjp.traffichelper.data.model.litepal;

import com.androidjp.traffichelper.data.pojo.Dialogue;
import com.androidjp.traffichelper.data.pojo.Record;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

/**
 * 使用ORM框架 LitePal 进行SQLite数据库管理
 * Created by androidjp on 2017/4/8.
 */

public class DBManager {
    private static DBManager sInstance = null;

    private DBManager(){

    }

    public static DBManager getInstance(){
        if (sInstance==null){
            synchronized (DBManager.class){
                if (sInstance == null)
                    sInstance = new DBManager();
            }
        }
        return sInstance;
    }

    /**
     * 存储所有的record
     * @param recordList 需存储的record列表（会自动存储内部的Location对象）
     */
    public  void saveRecordList(List<Record> recordList){

        if(recordList== null)
            return;
        for (Record item:recordList){
            item.save();
        }
    }

    /**
     * 获取所有的理赔问答对话记录
     * @param callback 异步回调
     */
    public void getDialogueList(FindMultiCallback callback){
        DataSupport.order("dialogue_time").findAsync(Dialogue.class).listen(callback);
    }


    /**
     * 清空所有对话记录
     */
    public void clearDialogueList(UpdateOrDeleteCallback callback){
        DataSupport.deleteAllAsync(Dialogue.class).listen(callback);
    }

    /**
     * 获取理赔历史列表
     * @param callback
     */
    public void getRecordList(FindMultiCallback callback){

    }

}
