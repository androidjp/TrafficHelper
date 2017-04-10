package com.androidjp.traffichelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidjp.lib_common_util.data.TimeUtil;
import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.Record;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by androidjp on 2017/3/22.
 */

public class RecordHolder extends BaseViewHolder<Record> {

    @Bind(R.id.tv_accident_location)
    TextView tvLocation;
    @Bind(R.id.tv_accident_pay)
    TextView tvPay;
    @Bind(R.id.tv_accident_responsibility)
    TextView tvContent;
    @Bind(R.id.tv_accident_time)
    TextView tvTime;

    /**
     * 新的自定义的基类构造方法：
     *
     * @param context   ViewHolder所在上下文
     * @param root      依附的RecyclerView
     */
    public RecordHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_history_record);
    }

    @Override
    protected void bindData(final Record itemValue, final int position, final OnItemClickListener<Record> listener) {
        if (itemValue.getLocation()!=null)
            tvLocation.setText(itemValue.getLocation().toString());
        tvPay.setText(itemValue.getPay() + getContext().getResources().getString(R.string.yuan));
        String[] responsibilies = getContext().getResources().getStringArray(R.array.traffic_responsibility);
        tvContent.setText(responsibilies[itemValue.responsibility]);
        tvTime.setText(TimeUtil.formatDate(new Date(itemValue.record_time)));
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null)
                    listener.onItemClick(itemValue,123321,position);
            }
        });
    }
}
