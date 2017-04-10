package com.androidjp.traffichelper.consult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.androidjp.lib_baidu_asr.BaiduASR;
import com.androidjp.lib_baidu_asr.data.Constant;
import com.androidjp.lib_common_util.system.KeyBoardUtil;
import com.androidjp.lib_custom_view.edittext.ClearEditText;
import com.androidjp.lib_great_recyclerview.base.BaseRecAdapter;
import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.adapter.AnswerHolder;
import com.androidjp.traffichelper.adapter.QuestionHolder;
import com.androidjp.traffichelper.data.pojo.Dialogue;
import com.skyfishjy.library.RippleBackground;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * 语言咨询界面
 * Created by androidjp on 2016/12/24.
 */

public class ConsultFragment extends Fragment implements ConsultContract.View, View.OnClickListener, View.OnLongClickListener {

    View rootView;
    @Bind(R.id.ibtn_audio)
    ImageButton fabQue;
    @Bind(R.id.ibtn_send)
    ImageButton ibtnSend;
    @Bind(R.id.et_consult)
    ClearEditText etConsult;
    @Bind(R.id.consult_recycler)
    RecyclerView mRecView;
    @Bind(R.id.frame_reclist)
    FrameLayout frameRecList;
    BaseRecAdapter<Dialogue> mListAdapter;

    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    ///控件
    @Bind(R.id.ripple_bg)
    RippleBackground rippleBackground;
    @Bind(R.id.btn_speech)
    Button btnSpeech;
    @Bind(R.id.btn_settings)
    Button btnSettings;

    protected View emptyView;//这里，放一个空的View
    private ConsultContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.fragment_consult, container, false);
            this.emptyView = inflater.inflate(R.layout.layout_empty, container, false);
        }

        return this.rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.edit().putBoolean("api", true).apply();
        sp.edit().remove(Constant.EXTRA_INFILE).apply(); // infile参数用于控制识别一个PCM音频流（或文件），每次进入程序都将该值清楚，以避免体验时没有使用录音的问题


        initRecyclerView();
        this.btnSpeech.setOnClickListener(this);
        this.fabQue.setOnLongClickListener(this);
        this.ibtnSend.setOnClickListener(this);
        this.btnSettings.setOnClickListener(this);
        this.fabQue.setOnClickListener(this);
        ////设置自定义的上划Fragment
        BottomSheetBehavior.from(nestedScrollView).setState(BottomSheetBehavior.STATE_COLLAPSED);

        this.etConsult.setOnClickListener(this);
    }

    private void initRecyclerView() {
        this.mRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mListAdapter = new BaseRecAdapter<Dialogue>() {
            @Override
            protected int onGetItemViewType(Dialogue item) {
                if (item.getUser_id() == null)
                    return 0;
                else
                    return 1;
            }

            @Override
            protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int type) {
                switch (type) {
                    case 0:
                        return new AnswerHolder(context, parent);
                    case 1:
                        return new QuestionHolder(context, parent);
                    default:
                        return null;
                }
            }
        };
//        this.mListAdapter.setmOnItemClickListener(this);//暂时item没有点击功能
        this.mRecView.setAdapter(this.mListAdapter);
        this.frameRecList.addView(this.emptyView);
        this.emptyView.setVisibility(View.VISIBLE);
    }


    //开关consult界面
    public void toggleConsultPage() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(nestedScrollView);
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    //隐藏consult界面
    private void closeConsultPage() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(nestedScrollView);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setPresenter(ConsultContract.Presenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.start();
    }

    @Override
    public void loadList(List<Dialogue> list) {
        ///初始化加载整个list
        if (list == null || list.size() == 0) {
            this.emptyView.setVisibility(View.VISIBLE);
        } else {
            this.emptyView.setVisibility(View.INVISIBLE);
            this.mListAdapter.addList(list);
            checkEmptyList();
        }
    }

    @Override
    public void showAnswer(Dialogue answer) {
        this.mListAdapter.addData(answer);
        checkEmptyList();
    }

    @Override
    public void showQuestion(Dialogue question) {
        this.mListAdapter.addData(question);
        checkEmptyList();
    }

    @Override
    public void showSpeechText(String text) {
        this.etConsult.setText(text);
    }

    @Override
    public void showSpeechBtnText(String text) {
        this.btnSpeech.setText(text);
        if (!text.equals("开始")){
            this.rippleBackground.startRippleAnimation();
        }else{
            this.rippleBackground.stopRippleAnimation();
        }
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_audio:
                //TODO:测试
                this.mPresenter.sendQuestion("你好，我有个问题");
                break;
        }

        return true;
    }

    private void checkEmptyList() {
        if (this.mListAdapter.getItemCount() > 0) {
            this.emptyView.setVisibility(View.INVISIBLE);
            this.mRecView.scrollToPosition(this.mListAdapter.getItemCount() - 1);
        } else {
            this.emptyView.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onClick(View v) {
        Handler handler = new Handler(Looper.getMainLooper());

        switch (v.getId()) {
            case R.id.ibtn_send:
                String s = this.etConsult.getText().toString();
                if (!TextUtils.isEmpty(s))
                    this.mPresenter.sendQuestion(s);
                break;
            case R.id.ibtn_audio:
                if (KeyBoardUtil.isShowKeyboard(getActivity(), etConsult)){
                    closeConsultPage();
                    KeyBoardUtil.hideKeyboard(getActivity());
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggleConsultPage();
                    }
                },300);
                break;
            case R.id.et_consult:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeConsultPage();
                    }
                },300);
                break;
            case R.id.btn_speech:
                mPresenter.toggleYuYin();
                break;
            case R.id.btn_settings:
                BaiduASR.openSettings(getActivity());
                break;
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            this.mPresenter.getIntentBundle(data.getExtras());
        }
    }

    @Override
    public void onDestroy() {
        BaiduASR.release();
        super.onDestroy();
    }
}
