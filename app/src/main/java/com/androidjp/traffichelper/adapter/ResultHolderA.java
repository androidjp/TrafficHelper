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

public class ResultHolderA extends BaseViewHolder<ResultInfo>{
    @Bind(R.id.tv_pay_all_value)
    TextView tvPayAll;

    /**
     * 新的自定义的基类构造方法：
     *
     * @param context   ViewHolder所在上下文
     * @param root      依附的RecyclerView
     */
    public ResultHolderA(Context context, ViewGroup root) {
        super(context, root, R.layout.item_record_result_top);

    }

    @Override
    protected void bindData(ResultInfo itemValue, int position, OnItemClickListener<ResultInfo> listener) {
        tvPayAll.setText(itemValue.getMoney());
    }
}
