package com.androidjp.traffichelper.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.ResultInfo;

import butterknife.Bind;

/**
 * Created by androidjp on 2017/3/20.
 */

public class ResultHolderB extends BaseViewHolder<ResultInfo> {
    @Bind(R.id.title)
    TextView tvTitle;
    @Bind(R.id.money)
    TextView tvMoney;
    @Bind(R.id.content)
    TextView tvContent;

    /**
     * 新的自定义的基类构造方法：
     *
     * @param context   ViewHolder所在上下文
     * @param root      依附的RecyclerView
     */
    public ResultHolderB(Context context, ViewGroup root) {
        super(context, root, R.layout.item_record_result);
    }

    @Override
    protected void bindData(ResultInfo itemValue, int position, OnItemClickListener<ResultInfo> listener) {
        tvTitle.setText(itemValue.getTitle());
        tvMoney.setText(itemValue.getMoney());
        tvContent.setText(itemValue.getContent());
    }
}
