package com.androidjp.lib_common_util.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 * Created by JP on 2016/4/14.
 */
public class ExecutorManager {

    private static ExecutorManager instance;

    private ExecutorService mFixedThreadPool;
    private ExecutorService mCachedThreadPool;
    private ScheduledExecutorService mScheduledThreadPool;
    private ExecutorService mSingledThreadExecutor;

    private ExecutorManager(){

    }
    public static ExecutorManager getInstance(){
        if (instance==null){
            synchronized (ExecutorManager.class){
                if (instance == null){
                    instance = new ExecutorManager();
                }
            }
        }
        return instance;
    }


    /**
     * 使用 FixedThreadPool 执行任务
     * @param runnable
     */
    public void execTaskByFixed(Runnable runnable){
        if (mFixedThreadPool==null){
            mFixedThreadPool = Executors.newFixedThreadPool(20);
        }
        mFixedThreadPool.execute(runnable);
    }


    /**
     * 使用 CachedThreadPool 执行任务
     * @param runnable
     */
    public void execTaskByCached(Runnable runnable){
        if (mCachedThreadPool==null){
            mCachedThreadPool = Executors.newCachedThreadPool();
        }
        mCachedThreadPool.execute(runnable);
    }

    /**
     * 使用 ScheduledThreadPool 执行任务
     * @param runnable
     */
    public void execTaskByScheduled(Runnable runnable){
        if (mScheduledThreadPool==null){
            mScheduledThreadPool = Executors.newScheduledThreadPool(20);
        }
        mScheduledThreadPool.execute(runnable);
    }

    /**
     * 使用 ScheduledThreadPool 执行任务(延时)
     * @param runnable
     */
    public void execTaskByScheduledDelayed(Runnable runnable, long ms){
        if (mScheduledThreadPool==null){
            mScheduledThreadPool = Executors.newScheduledThreadPool(20);
        }
        mScheduledThreadPool.schedule(runnable, ms, TimeUnit.MILLISECONDS);
    }

    /**
     * 使用 ScheduledThreadPool 执行任务(延时循环)
     * @param runnable
     */
    public void execTaskByScheduledRecycle(Runnable runnable, long ms){
        if (mScheduledThreadPool==null){
            mScheduledThreadPool = Executors.newScheduledThreadPool(20);
        }
        mScheduledThreadPool.scheduleAtFixedRate(runnable, ms,ms, TimeUnit.MILLISECONDS);
    }


    /**
     * 使用 SingledThreadExecutor 执行任务
     * @param runnable
     */
    public void execTaskBySingled(Runnable runnable){
        if (mSingledThreadExecutor==null){
            mSingledThreadExecutor = Executors.newSingleThreadExecutor();
        }
        mSingledThreadExecutor.execute(runnable);
    }



}
