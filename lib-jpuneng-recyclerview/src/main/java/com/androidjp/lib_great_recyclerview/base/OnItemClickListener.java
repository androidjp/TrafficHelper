package com.androidjp.lib_great_recyclerview.base;

/**
 * 自定义的点击事件接口
 * Created by androidjp on 16-7-24.
 */
public interface OnItemClickListener<V> {

    /**
     * 当item被点击时触发本事件
     * @param itemValue 点击的item传递的值
     * @param viewID 点击控件的id
     * @param position 被点击的item的位置
     */
    void onItemClick(V itemValue, int viewID, int position);

}
