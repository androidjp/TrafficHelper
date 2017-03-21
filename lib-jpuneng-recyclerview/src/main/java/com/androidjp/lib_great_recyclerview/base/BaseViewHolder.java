package com.androidjp.lib_great_recyclerview.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;

import butterknife.ButterKnife;

/**
 * 加重ViewHolder的工作：绑定Item的View布局等
 * Created by androidjp on 16-7-24.
 */
public abstract class BaseViewHolder<V> extends RecyclerView.ViewHolder {
    private SoftReference<View> softReference;

    /**
     * 新的自定义的基类构造方法：
     *
     * @param context   ViewHolder所在上下文
     * @param root      依附的RecyclerView
     * @param layoutRes 对应要绑定的Item layout 的id
     */
    public BaseViewHolder(Context context, ViewGroup root, int layoutRes) {

        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        ButterKnife.bind(this, itemView);///本身ViewHolder就有一个public修饰的绑定着的item View
        softReference = new SoftReference<View>(itemView);
    }


    ///本来需要实现的构造方法
//    public BaseViewHolder(View itemView) {
//        super(itemView);
//    }

    /**
     * 方便其子类进行一些需要Context的操作.
     *
     * @return 调用者的Context
     */
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 用于传递数据和信息,在Adapter要将数据绑定ViewHolder时调用
     *
     * @param itemValue Item的数据
     * @param position  当前item的position
     * @param listener  设置Holder所绑定的ItemView拥有点击事件
     */
    public void setData(V itemValue, int position, OnItemClickListener<V> listener) {
        bindData(itemValue, position, listener);
    }

    //=-==============================================

    /**
     * 抽象方法，绑定数据.
     * 让子类自行对数据和view进行绑定
     *
     * @param itemValue Item的数据
     * @param position  当前item的position
     * @param listener  点击事件监听者
     */
    protected abstract void bindData(V itemValue, int position, OnItemClickListener<V> listener);

    /**
     * 获取绑定的item View
     * @return
     */
    public View getView(){
        if (this.softReference!=null)
            return this.softReference.get();
        else
            return null;
    }

    /**
     * 通过Id 获取 对应的控件
     * @param id 控件id
     * @return 控件引用
     */
    public View getViewById(int id){
        if (this.softReference!=null){
            return this.softReference.get().findViewById(id);
        }else
            return null;
    }

}
