package com.androidjp.traffichelper.result;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.androidjp.lib_great_recyclerview.base.BaseRecAdapter;
import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.extra.RecListFragment;
import com.androidjp.lib_great_recyclerview.layoutmanager.swipecard.CardItemTouchHelperCallback;
import com.androidjp.lib_great_recyclerview.layoutmanager.swipecard.CardLayoutManager;
import com.androidjp.lib_great_recyclerview.layoutmanager.swipecard.OnSwipeListener;
import com.androidjp.traffichelper.adapter.ResultHolderA;
import com.androidjp.traffichelper.adapter.ResultHolderB;
import com.androidjp.traffichelper.data.pojo.ResultInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidjp on 2017/3/20.
 */

public class ResultFragment extends RecListFragment<ResultInfo> implements ResultContract.View{


    private ResultContract.Presenter mPresenter = null;


    @Override
    public void setPresenter(ResultContract.Presenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.start();
    }

    @Override
    protected RecyclerView.LayoutManager onSetLayoutManager() {
        mRecView.setItemAnimator(new DefaultItemAnimator());
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(getAdapter(),getAdapter().getDataList());
        cardCallback.setOnSwipedListener(new OnSwipeListener<ResultInfo>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, ResultInfo resultInfo, int direction) {
                getAdapter().addData(resultInfo);
            }

            @Override
            public void onSwipedClear() {
//                mRecView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        initData();
//                        mRecView.getAdapter().notifyDataSetChanged();
//                    }
//                }, 3000L);
            }
//
//            @Override
//            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
////                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
//                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
////                if (direction == CardConfig.SWIPING_LEFT) {
////                    myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
////                } else if (direction == CardConfig.SWIPING_RIGHT) {
////                    myHolder.likeImageView.setAlpha(Math.abs(ratio));
////                } else {
////                    myHolder.dislikeImageView.setAlpha(0f);
////                    myHolder.likeImageView.setAlpha(0f);
////                }
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, Integer o, int direction) {
////                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
////                viewHolder.itemView.setAlpha(1f);
////                myHolder.dislikeImageView.setAlpha(0f);
////                myHolder.likeImageView.setAlpha(0f);
////                Toast.makeText(MainActivity.this, direction == CardConfig.SWIPED_LEFT ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSwipedClear() {
////                Toast.makeText(MainActivity.this, "data clear", Toast.LENGTH_SHORT).show();
//                mRecView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        initData();
//                        mRecView.getAdapter().notifyDataSetChanged();
//                    }
//                }, 3000L);
//            }

        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(mRecView, touchHelper);
        touchHelper.attachToRecyclerView(mRecView);
//        mRecView.setLayoutManager(cardLayoutManager);
//
//        return new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        return cardLayoutManager;
    }

    @Override
    protected BaseRecAdapter<ResultInfo> onSetAdapter() {
        BaseRecAdapter<ResultInfo> adapter =  new BaseRecAdapter<ResultInfo>() {
            @Override
            protected int onGetItemViewType(ResultInfo item) {
                if (item.getTitle()!=null)
                    return 1;
                else
                    return 0;
            }

            @Override
            protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int type) {
                if (type == 1)
                    return new ResultHolderB(context,parent);
                else
                    return new ResultHolderA(context,parent);
            }
        };
        return adapter;
    }

    @Override
    protected View onSetEmptyView() {
        return null;
    }

    @Override
    protected boolean initData() {

        ///测试数据
        List<ResultInfo> datas=  new ArrayList<>();
        datas.add(new ResultInfo(null,30000.00f,null));
        datas.add(new ResultInfo("医药费",899.9f,"相关法律"));
        datas.add(new ResultInfo("误工费",10000,"相关法律"));
        datas.add(new ResultInfo("精神损失费",10000.1f,"相关法律"));

        mListAdapter.addList(datas);




        return false;
    }

    @Override
    protected RecyclerView.ItemDecoration[] onAddItemDecoration() {
        return null;
    }

    @Override
    protected void destroyViewDoing() {

    }

    @Override
    public void onItemClick(ResultInfo itemValue, int viewID, int position) {

    }

    @Override
    public void showDatas(List<ResultInfo> list) {
        mListAdapter.clearDatas();
        mListAdapter.addList(list);
    }
}
