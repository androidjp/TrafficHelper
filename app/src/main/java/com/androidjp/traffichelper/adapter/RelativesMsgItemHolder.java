package com.androidjp.traffichelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.androidjp.lib_custom_view.edittext.ClearEditText;
import com.androidjp.lib_custom_view.selector.JPSelectView;
import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.pojo.RelativeItemMsg;

import butterknife.Bind;

/**
 * 亲属和年龄选择界面 item
 * Created by androidjp on 2017/1/17.
 */

public class RelativesMsgItemHolder extends BaseViewHolder<RelativeItemMsg>{
    @Bind(R.id.ibtn_fail)
    ImageButton ibtnFail;
    @Bind(R.id.jsv_relatives_msg)
    JPSelectView relationSelector;
    @Bind(R.id.cet_age)
    ClearEditText cetAge;

    /**
     * 新的自定义的基类构造方法：
     *
     * @param context   ViewHolder所在上下文
     * @param root      依附的RecyclerView
     */
    public RelativesMsgItemHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.layout_relatives_msg_item);
    }

    @Override
    protected void bindData(final RelativeItemMsg itemValue, final int position, final OnItemClickListener<RelativeItemMsg> listener) {
        ibtnFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(itemValue,R.id.ibtn_fail,position);
                }
            }
        });
        relationSelector.setCurrentType(JPSelectView.TYPE_STRING).setStringList(THApplication.getContext().getResources().getStringArray(R.array.relation_array));
    }
}
