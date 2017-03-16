package com.androidjp.traffichelper.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.Dialogue;

import butterknife.Bind;

/**
 * 回答item布局
 * Created by androidjp on 2016/12/26.
 */

public class AnswerHolder extends BaseViewHolder<Dialogue>{
    @Bind(R.id.iv_robot)
    ImageView ivRobot;
    @Bind(R.id.tv_word_robot)
    TextView tvWordRobot;

    /**
     * 新的自定义的基类构造方法：
     *
     * @param context   ViewHolder所在上下文
     * @param root      依附的RecyclerView
     */
    public AnswerHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.layout_dialogue_answer);
    }

    @Override
    protected void bindData(Dialogue itemValue, int position, OnItemClickListener<Dialogue> listener) {
        ivRobot.setImageResource(R.drawable.user_blue);
        tvWordRobot.setText(itemValue.word);
    }
}
