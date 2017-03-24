package com.androidjp.traffichelper.history;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.androidjp.lib_great_recyclerview.base.BaseRecAdapter;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by androidjp on 2017/3/23.
 */

public abstract class SuperRecListFragment<V> extends Fragment implements OnItemClickListener<V> {

    protected View rootLayout;
    protected FrameLayout frameLayout;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected LinearLayout mFootView;
    protected RecyclerView mRecView;
    protected BaseRecAdapter<V> mListAdapter;

    protected View emptyView;//这里，放一个空的View

    protected boolean isEnableSwipeRefresh = false;///默认是不允许下拉刷新等操作
    protected LoadListener loadListener = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootLayout == null) {
            rootLayout = inflater.inflate(R.layout.fragment_record_list, container, false);
            frameLayout = (FrameLayout) rootLayout.findViewById(R.id.frame_record_list);
            mSwipeRefreshLayout = (SwipeRefreshLayout) rootLayout.findViewById(R.id.swipe_layout);
            mRecView = (RecyclerView) rootLayout.findViewById(R.id.recview_record);
            mFootView = (LinearLayout) rootLayout.findViewById(R.id.layout_footer);
        }
//        ButterKnife.bind(this, rootLayout);

        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(249,161,21));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadListener!=null)
                    loadListener.refreshDoing();
            }
        });
        if (!isEnableSwipeRefresh){
            mSwipeRefreshLayout.setEnabled(false);
        }


        mListAdapter = onSetAdapter();
        mListAdapter.setmOnItemClickListener(this);
        mRecView.setAdapter(mListAdapter);
        mRecView.setLayoutManager(onSetLayoutManager());
        RecyclerView.LayoutManager layoutManager = onSetLayoutManager();
        if (layoutManager!=null)
            mRecView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration[] decorations = onAddItemDecoration();
        if (decorations != null) {
            for (RecyclerView.ItemDecoration item : decorations) {
                mRecView.addItemDecoration(item);
            }
        }

        mRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
//                    srlLayout.setEnabled(true);
                    if (isEnableSwipeRefresh){
                        if (loadListener!=null)
                            loadListener.askForMoreDoing();
                    }
                }
            }
        });

        return rootLayout;
    }



    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        destroyViewDoing();
        super.onDestroyView();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Logger.i("SuperRecListFragment.onViewCreated()");
        emptyView = onSetEmptyView();
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
            frameLayout.addView(emptyView);
        }
        initData();
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    public void showEmptyView() {

        if (emptyView == null) return;
        emptyView.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        if (emptyView == null) return;
        emptyView.setVisibility(View.GONE);
    }

    public BaseRecAdapter<V> getAdapter() {
        return this.mListAdapter;
    }


    public boolean isEnableSwipeRefresh(){
        return this.isEnableSwipeRefresh;
    }

    public void setEnableSwipeRefresh(boolean isEnable){
        this.isEnableSwipeRefresh = isEnable;
        if (!isEnableSwipeRefresh){
            mSwipeRefreshLayout.setEnabled(false);
        }else
            mSwipeRefreshLayout.setEnabled(true);
    }

    ///footerView 显示/隐藏
    public void showFooter(){
        mFootView.setVisibility(View.VISIBLE);
    }

    public void hideFooter(){
        mFootView.setVisibility(View.GONE);
    }

    ///刷新View 显示/隐藏
    public void showHeader(){
        mSwipeRefreshLayout.setRefreshing(true);
    }
    public void hideHeader(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void refreshEmptyView(){
        if (getAdapter().getItemCount()==0)
            showEmptyView();
        else
            hideEmptyView();
    }


    public void setLoadlListener(LoadListener listener){
        this.loadListener = listener;
    }

    public interface LoadListener{
        void refreshDoing();
        void askForMoreDoing();
    }

    //==========================================================================;

    /**
     * 设置RecyclerView是整体列表布局形式
     *
     * @return LayoutManager对象（LinearLayoutManager、GridLayoutManager等）
     */
    protected abstract RecyclerView.LayoutManager onSetLayoutManager();


    /**
     * 设置RecyclerView的Adapter
     */
    protected abstract BaseRecAdapter<V> onSetAdapter();


    /**
     * @return RecyclerView的EmptyView
     */
    protected abstract View onSetEmptyView();


    /**
     * @return 初始化数据（同步或异步方式，加载数据给 getAdapter()）
     */
    protected abstract boolean initData();

    /**
     * @return RecyclerView item 分割线
     */
    protected abstract RecyclerView.ItemDecoration[] onAddItemDecoration();


    /**
     * 在onDestroyView()中做什么
     */
    protected abstract void destroyViewDoing();

}
