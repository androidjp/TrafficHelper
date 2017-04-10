package com.androidjp.traffichelper.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.UserManager;
import com.androidjp.traffichelper.data.pojo.Dialogue;
import com.androidjp.traffichelper.home.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

import butterknife.Bind;

/**
 * 提问item布局
 * Created by androidjp on 2016/12/26.
 */

public class QuestionHolder extends BaseViewHolder<Dialogue> {
    @Bind(R.id.iv_user_pic)
    ImageView ivUserPic;
    @Bind(R.id.tv_word_user)
    TextView tvUserWord;

    /**
     * 新的自定义的基类构造方法：
     *
     * @param context ViewHolder所在上下文
     * @param root    依附的RecyclerView
     */
    public QuestionHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.layout_dialogue_question);
    }

    @Override
    protected void bindData(Dialogue itemValue, int position, OnItemClickListener<Dialogue> listener) {
        if (UserManager.getInstance(getContext()).getUser()==null || UserManager.getInstance(getContext()).getUser().getUser_pic()==null)
            ivUserPic.setImageResource(R.drawable.user_blue);
        else
            Glide.with(getContext())
                    .load(ServiceAPI.REMOTE_SERVER_HOST+ UserManager.getInstance(getContext()).getUser().getUser_pic())
//                                    .asGif() // 只能加载gif文件
                    // .asBitmap() // 将gif作为静态图加载
                    .placeholder(R.drawable.load)//占位符 也就是加载中的图片，可放个gif
                    .error(R.mipmap.ic_launcher)//失败图片
                    .crossFade(1000) // 图片淡入效果：可设置时长，默认“300ms”
                    .into(ivUserPic);
        tvUserWord.setText(itemValue.word);
    }
}
