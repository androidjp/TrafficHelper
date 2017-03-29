package com.androidjp.traffichelper.data.model;

import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;

/**
 * 实现与数据库的交互等
 * Created by androidjp on 2017/1/18.
 */

public class RecordManager {
    private Record record;

    private static RecordManager sInstance;

    private RecordManager(){
        record = new Record();
    }

    public static RecordManager getInstance(){
        if (sInstance == null)
            synchronized (RecordManager.class) {
                if (sInstance == null)
                    sInstance = new RecordManager();
            }

        ///TODO: 先从本地数据持久层中获取暂存的记录数据
        init();
        return sInstance;
    }

    private static void init() {
        ///TODO:获取数据（从持久层）

    }


    /**
     * 设置/更新记录数据
     * @param record 数据获取对象
     */
    public void setRecordMsg(Record record){
//        this.record.copyData(record);
    }


    /**
     * 获取当前计算记录（准备上传服务器）
     * @return
     */
    public Record getRecord(){
        this.record.record_time = System.currentTimeMillis();
        return this.record;
    }


    public void save(){

    }

    public void saveRecordList(){

    }

    public void saveRecordResForRecord(RecordRes recordRes){

    }
}
