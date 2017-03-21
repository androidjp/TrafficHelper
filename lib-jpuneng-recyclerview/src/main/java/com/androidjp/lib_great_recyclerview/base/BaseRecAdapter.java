package com.androidjp.lib_great_recyclerview.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * RecyclerView.Adapter基类
 * Created by androidjp on 16-7-24.
 */
public abstract class BaseRecAdapter<V> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    /**
     * 装载了每个Item的Value列表
     */
    private List<V> mValueList;

    /**
     * 我写的一个接口，通过回调分发点击事件
     */
    private OnItemClickListener<V> mOnItemClickListener;


    public BaseRecAdapter(){
        mValueList = new ArrayList<V>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ///定义成一个抽象方法，不同的列表，使用不同的ViewHolder
        return createViewHolder(parent.getContext(), parent, viewType);
    }


    //需要重写此方法


    @Override
    public int getItemViewType(int position) {
        return onGetItemViewType(mValueList.get(position));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //BaseViewHolder是我抽象出来的RecyclerView.ViewHolder的基类
        ((BaseViewHolder) holder).setData(mValueList.get(position), position, this.mOnItemClickListener);
    }


    /**
     *
     * @param listener 设置item点击事件
     */
    public void setmOnItemClickListener(OnItemClickListener<V> listener){
        this.mOnItemClickListener = listener;
    }




    /**
     * 返回数据列表
     */
    public List<V> getDataList(){
        return this.mValueList;
    }

    /**
     * 刷新数据
     * @param valueList 新的数据列表
     */
    public void refreshData(List<V> valueList) {
        if (this.mValueList==null){
            this.mValueList = new ArrayList<>();
        }
        this.mValueList.clear();

        this.mValueList.addAll(valueList);
        notifyDataSetChanged();

    }

    /**
     * 增加数据
     * @param data
     */
    public void addData(V data){
        if (this.mValueList==null){
            this.mValueList = new ArrayList<>();
        }
//        this.mValueList.add(data);
        this.mValueList.add(this.mValueList.size(),data);
        notifyDataSetChanged();
    }


    public void addList(List<V> data){
        if (this.mValueList==null){
            this.mValueList = new ArrayList<>();
        }
        this.mValueList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 删除
     * @param data
     */
    public void deleteData(V data){
        if (this.mValueList==null){
            return;
        }
        this.mValueList.remove(data);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearDatas(){
        if (this.mValueList == null){
            return;
        }
        this.mValueList.clear();
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return mValueList == null ? 0 : mValueList.size();
    }


    public Iterator<V> iterator(){
        return this.mValueList.iterator();
    }

    //==================================================================================
    //抽象方法
    //==================================================================================

    protected abstract int onGetItemViewType(V item);


    /**
     * 生成ViewHolder
     * @param context 上下文
     * @param parent  父ViewGroup
     * @return  ViewHolder对象
     */
    protected abstract BaseViewHolder createViewHolder(Context context, ViewGroup parent, int type);
}
