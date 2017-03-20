package com.androidjp.traffichelper.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidjp.lib_custom_view.selector.JPSelectView;
import com.androidjp.lib_great_recyclerview.base.BaseRecAdapter;
import com.androidjp.lib_great_recyclerview.base.BaseRecViewDivider;
import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.adapter.RelativesMsgItemHolder;
import com.androidjp.traffichelper.consult.ConsultActivity;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.RelativeItemMsg;
import com.androidjp.traffichelper.result.ResultActivity;
import com.dd.CircularProgressButton;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 首页的计算过程
 * Created by androidjp on 2016/12/9.
 */

public class MainFragment extends Fragment implements MainContract.View, View.OnClickListener {

    private View mRoot;
    private MainContract.Presenter mPresenter;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab_consult)
    FloatingActionButton btnConsult;
    @Bind(R.id.fab_calculate)
    FloatingActionButton btnCalculte;

    /**
     * 第一部分，定位
     */
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.btn_refresh_location)
    CircularProgressButton btnRefreshLocation;
    @Bind(R.id.btn_choice_location)
    CircularProgressButton btnChoiceLocation;

    /**
     * 第二部分，实际情况问答与输入
     */
    @Bind(R.id.layout_hurtlevel)
    RelativeLayout layoutHurtlevel;
    @Bind(R.id.label_hurtlevel)
    TextView labelHurtlevel;
    @Bind(R.id.line_hurtlevel)
    View lineHurtlevel;
    @Bind(R.id.tv_hurtlevel)
    JPSelectView tvHurtlevel;
    @Bind(R.id.btn_question)
    ImageView btnQuestion;
    @Bind(R.id.layout_sister_count)
    RelativeLayout layoutSisterCount;
    @Bind(R.id.label_sister_count)
    TextView labelSisterCount;
    @Bind(R.id.line_sister_count)
    View lineSisterCount;
    @Bind(R.id.tv_sister_count)
    JPSelectView tvSisterCount;
    @Bind(R.id.layout_has_spouse)
    RelativeLayout layoutHasSpouse;
    @Bind(R.id.label_has_spouse)
    TextView labelHasSpouse;
    @Bind(R.id.line_has_spouse)
    View lineHasSpouse;
    @Bind(R.id.tv_has_spouse)
    JPSelectView tvHasSpouse;
    @Bind(R.id.layout_id_type)
    RelativeLayout layoutIdType;
    @Bind(R.id.label_id_type)
    TextView labelIdType;
    @Bind(R.id.line_id_type)
    View lineIdType;
    @Bind(R.id.tv_id_type)
    JPSelectView tvIdType;
    @Bind(R.id.layout_responsibility)
    RelativeLayout layoutResponsibility;
    @Bind(R.id.label_responsibility)
    TextView labelResponsibility;
    @Bind(R.id.line_responsibility)
    View lineResponsibility;
    @Bind(R.id.tv_responsibility)
    JPSelectView tvResponsibility;
    @Bind(R.id.layout_driving_tools)
    RelativeLayout layoutDrivingTools;
    @Bind(R.id.label_driving_tools)
    TextView labelDrivingTools;
    @Bind(R.id.line_driving_tools)
    View lineDrivingTools;
    @Bind(R.id.tv_driving_tools)
    JPSelectView tvDrivingTools;


    @Bind(R.id.layout_hospital_consume)
    RelativeLayout layoutHospitalConsume;
    @Bind(R.id.label_hospital_consume)
    TextView labelHospitalConsume;
    @Bind(R.id.line_hospital_consume)
    View lineHospitalConsume;
    @Bind(R.id.layout_hospital_days)
    RelativeLayout layoutHospitalDays;
    @Bind(R.id.label_hospital_days)
    TextView labelHospitalDays;
    @Bind(R.id.line_hospital_days)
    View lineHospitalDays;
    @Bind(R.id.layout_tardy_days)
    RelativeLayout layoutTardyDays;
    @Bind(R.id.label_tardy_days)
    TextView labelTardyDays;
    @Bind(R.id.line_tardy_days)
    View lineTardyDays;
    @Bind(R.id.layout_nutrition_days)
    RelativeLayout layoutNutritionDays;
    @Bind(R.id.label_nutrition_days)
    TextView labelNutritionDays;
    @Bind(R.id.line_nutrition_days)
    View lineNutritionDays;
    @Bind(R.id.layout_nursing_days)
    RelativeLayout layoutNursingDays;
    @Bind(R.id.label_nursing_days)
    TextView labelNursingDays;
    @Bind(R.id.line_nursing_days)
    View lineNursingDays;

    @Bind(R.id.layout_family_custom)
    RelativeLayout layoutFamilyCustom;
    @Bind(R.id.label_family_custom)
    TextView labelFamilyCustom;
    @Bind(R.id.line_family_custom)
    View lineFamilyCustom;
    @Bind(R.id.tv_family_custom)
    TextView tvFamilyCustom;
    @Bind(R.id.layout_add_relatives)
    RelativeLayout layoutAddRelatives;
    @Bind(R.id.recview_relatives_msg)
    RecyclerView recRelativesMsg;
    @Bind(R.id.nestedScrollView_main_fragment)
    NestedScrollView mNestedScrollView;
    BaseRecAdapter<RelativeItemMsg> msgBaseRecAdapter;
    SweetAlertDialog sDialog;

    Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_main, container, false);
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        initView();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mPresenter.stopLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter.destroyLocation();
    }

    private void initView() {
        btnRefreshLocation.setIndeterminateProgressMode(true);
        btnRefreshLocation.setOnClickListener(this);
        btnChoiceLocation.setIndeterminateProgressMode(true);
        btnChoiceLocation.setOnClickListener(this);

        layoutDrivingTools.setOnClickListener(this);
        layoutFamilyCustom.setOnClickListener(this);
        layoutHasSpouse.setOnClickListener(this);
        btnQuestion.setOnClickListener(this);
        tvFamilyCustom.setOnClickListener(this);

        tvHurtlevel.setCurrentType(JPSelectView.TYPE_STRING).setStringList(getResources().getStringArray(R.array.hurlevel_array));
        tvDrivingTools.setCurrentType(JPSelectView.TYPE_STRING).setStringList(getResources().getStringArray(R.array.vehicle));
        tvResponsibility.setCurrentType(JPSelectView.TYPE_STRING).setStringList(getResources().getStringArray(R.array.traffic_responsibility));
        tvIdType.setCurrentType(JPSelectView.TYPE_STRING).setStringList(getResources().getStringArray(R.array.id_type));
        tvHasSpouse.setCurrentType(JPSelectView.TYPE_STRING).setStringList(getResources().getStringArray(R.array.has_spouse));


        layoutAddRelatives.setOnClickListener(this);

        msgBaseRecAdapter = new BaseRecAdapter<RelativeItemMsg>() {
            @Override
            protected int onGetItemViewType(RelativeItemMsg item) {
                return 0;
            }

            @Override
            protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int type) {
//                return new RelativesMsgItemHolder(context,parent);
                return new RelativesMsgItemHolder(context,parent);
            }
        };
        msgBaseRecAdapter.setmOnItemClickListener(new OnItemClickListener<RelativeItemMsg>() {
            @Override
            public void onItemClick(RelativeItemMsg itemValue, int viewID, int position) {
                switch (viewID){
                    case R.id.iv_sub:
                        msgBaseRecAdapter.deleteData(itemValue);
                        break;
                }
            }
        });
        this.recRelativesMsg.addItemDecoration(new BaseRecViewDivider(getActivity(), LinearLayout.VERTICAL));
        this.recRelativesMsg.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        this.recRelativesMsg.setAdapter(msgBaseRecAdapter);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choice_location:
                if (btnChoiceLocation.getProgress() == 0) {
                    btnChoiceLocation.setProgress(50);
                } else if (btnChoiceLocation.getProgress() == -1) {
                    btnChoiceLocation.setProgress(0);
                } else {
                    btnChoiceLocation.setProgress(-1);
                }
                break;

            case R.id.btn_refresh_location:
                if (btnRefreshLocation.getProgress() == 0) {
                    btnRefreshLocation.setProgress(50);
                    this.mPresenter.getLocation();
                }
                break;

            case R.id.btn_question:
////                ((MainActivity)getActivity()).toggleConsultPage();
//                startActivity(new Intent(getActivity(), DemoActivity.class));
//                break;
                Intent intent = new Intent(getActivity(), ConsultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.tv_family_custom:
            case R.id.layout_add_relatives:
//                RelativesMsgFragment dialog = new RelativesMsgFragment();
//                dialog.setTargetFragment(MainFragment.this,11);
//                dialog.show(getFragmentManager(),"");
//                dialog.setPresenter(new RelativeMsgPresenter());
                //TODO: 添加一行需抚养人
                Logger.i("添加一行需抚养人");
                msgBaseRecAdapter.addData(new RelativeItemMsg(0,60));
                //TODO:并将整个MainFragment滚动到最底部
//                mNestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                //
                mHandler.post(new Runnable() {
                    public void run() {
                        mNestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        recRelativesMsg.scrollToPosition(msgBaseRecAdapter.getItemCount()-1);
                    }
                });
                break;
        }
    }

    @Override
    public void prepareCalculateMsg() {
        ///收集计算信息
        final Record record = new Record();
        ///TODO：开始收集信息，准备提交

        this.mPresenter.startCalculate(record);
    }

    @Override
    public void showLocationMsg(String location) {
        this.tvLocation.setText(location);
        this.btnRefreshLocation.setProgress(100);
        this.btnRefreshLocation.setCompleteText("定位成功");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                btnRefreshLocation.setProgress(0);
            }
        },2000);
    }

    @Override
    public void showRecordResult(final RecordRes recordRes) {
        if (sDialog!=null){
            if (recordRes !=null){
                sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sDialog.setTitleText("理赔总额："+ recordRes.money_pay)
                        .setConfirmText("查看详情")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(getActivity(), ResultActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("recordRes", recordRes);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
            }else{
                sDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                sDialog.setTitleText("计算失败")
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmText("重试")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mPresenter.prepareCalculate();
                            }
                        }).setCancelable(true);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11)
        {
            String evaluate = data
                    .getStringExtra(RelativesMsgFragment.REQUSET_PERSON_COUNT);
            tvFamilyCustom.setText(evaluate);
        }
    }

    /**
     * 咨询
     */
    @OnClick({R.id.fab_consult})
    public void openConsultPage(View view){
        ///打开下方的咨询界面
//        toggleConsultPage();
        Intent intent = new Intent(getActivity(), ConsultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 准备计算
     */
    @OnClick(R.id.fab_calculate)
    public void startCalculate(View view){
//        closeConsultPage();

         sDialog = new SweetAlertDialog(getContext());
        sDialog.setCancelable(true);
        sDialog.setTitleText("计算理赔？").setCancelText("取消").setConfirmText("开始计算")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText("计算中。。。");
                sweetAlertDialog.setCancelable(false);
                ///开始计算
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.prepareCalculate();
                    }
                },2000);
//                mPresenter.prepareCalculate();

            }
        }).show();
    }


}
