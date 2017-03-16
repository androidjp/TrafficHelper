package com.androidjp.lib_great_recyclerview.extra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.androidjp.lib_great_recyclerview.R;
import com.androidjp.lib_great_recyclerview.base.BaseRecAdapter;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;

import butterknife.ButterKnife;


/**
 * 用RecyclerView作为列表的Fragment
 * Created by androidjp on 16-7-7.
 */
public abstract class RecListDialogFragment<V> extends DialogFragment implements OnItemClickListener<V> {

    View rootLayout;
    FrameLayout frameLayout;
    RecyclerView mRecView;
    BaseRecAdapter<V> mListAdapter;

    View emptyView;//这里，放一个空的View


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootLayout == null) {
            rootLayout = inflater.inflate(R.layout.fragment_reclist, container, false);
            frameLayout = (FrameLayout) rootLayout.findViewById(R.id.frame_reslist);
            mRecView = (RecyclerView) rootLayout.findViewById(R.id.recview);
        }
        ButterKnife.bind(this,rootLayout);
        mRecView.setLayoutManager(onSetLayoutManager());
        RecyclerView.ItemDecoration decoration = onAddItemDecoration();
        if (decoration!=null){
            mRecView.addItemDecoration(decoration);
        }
        return rootLayout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListAdapter = onSetAdapter();
        mListAdapter.setmOnItemClickListener(this);
        mRecView.setAdapter(mListAdapter);

        emptyView = onSetEmptyView();
        emptyView.setVisibility(View.GONE);
        frameLayout.addView(emptyView);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        destroyViewDoing();
    }


    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

    public BaseRecAdapter<V> getAdapter() {
        return this.mListAdapter;
    }

    //==========================================================================

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
     * @return 设置是否添加 分割线
     */
    protected abstract RecyclerView.ItemDecoration onAddItemDecoration();



    protected abstract void destroyViewDoing();

}
