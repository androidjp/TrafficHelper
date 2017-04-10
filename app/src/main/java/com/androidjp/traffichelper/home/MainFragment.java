package com.androidjp.traffichelper.home;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidjp.lib_common_util.ui.SnackUtil;
import com.androidjp.lib_custom_view.edittext.ClearEditText;
import com.androidjp.lib_custom_view.selector.JPSelectView;
import com.androidjp.lib_great_recyclerview.base.BaseRecAdapter;
import com.androidjp.lib_great_recyclerview.base.BaseRecViewDivider;
import com.androidjp.lib_great_recyclerview.base.BaseViewHolder;
import com.androidjp.lib_great_recyclerview.base.OnItemClickListener;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.adapter.RelativesMsgItemHolder;
import com.androidjp.traffichelper.consult.ConsultActivity;
import com.androidjp.traffichelper.data.model.location.AMapLocationManager;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.RelativeItemMsg;
import com.androidjp.traffichelper.result.ResultActivity;
import com.dd.CircularProgressButton;
import com.orhanobut.logger.Logger;

import java.util.List;

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
     * 第二部分，实际情况问答与自定义View输入
     */
    @Bind(R.id.tv_hurtlevel)
    JPSelectView tvHurtlevel;
    @Bind(R.id.btn_question)
    ImageView btnQuestion;
    @Bind(R.id.tv_sister_count)
    JPSelectView tvSisterCount;
    @Bind(R.id.tv_has_spouse)
    JPSelectView tvHasSpouse;
    @Bind(R.id.tv_id_type)
    JPSelectView tvIdType;
    @Bind(R.id.tv_responsibility)
    JPSelectView tvResponsibility;
    @Bind(R.id.tv_driving_tools)
    JPSelectView tvDrivingTools;

    /**
     * 第三部分， ClearEditText的输入
     */
    @Bind(R.id.cet_salary)
    ClearEditText cet_salary;
    @Bind(R.id.cet_hospital_consume)
    ClearEditText cet_hospital_custom;
    @Bind(R.id.cet_hospital_days)
    ClearEditText cet_hospital_days;
    @Bind(R.id.cet_tardy_days)
    ClearEditText cet_tardy_days;
    @Bind(R.id.cet_nutrition_days)
    ClearEditText cet_nutrition_days;
    @Bind(R.id.cet_nursing_days)
    ClearEditText cet_nursing_days;


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
        ButterKnife.bind(this, mRoot);

        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                return new RelativesMsgItemHolder(context, parent);
            }
        };
        msgBaseRecAdapter.setmOnItemClickListener(new OnItemClickListener<RelativeItemMsg>() {
            @Override
            public void onItemClick(RelativeItemMsg itemValue, int viewID, int position) {
                switch (viewID) {
                    case R.id.iv_sub:
                        msgBaseRecAdapter.deleteData(itemValue);
                        break;
                    ///TODO:子女/父母选择问题
                    case R.id.jsv_relatives_msg:
                        break;
                    ///TODO:年龄
                }
            }
        });
        this.recRelativesMsg.addItemDecoration(new BaseRecViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        this.recRelativesMsg.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
                msgBaseRecAdapter.addData(new RelativeItemMsg(0, 60));
                //TODO:并将整个MainFragment滚动到最底部
//                mNestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                //
                mHandler.post(new Runnable() {
                    public void run() {
                        mNestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        recRelativesMsg.scrollToPosition(msgBaseRecAdapter.getItemCount() - 1);
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
        record.setLocation(AMapLocationManager.getInstance(getActivity()).getLocation());
        record.hurt_level = tvHurtlevel.getCurrentPos();
        record.relatives_count = tvSisterCount.getCount();
        record.has_spouse = (tvHasSpouse.getCurrentPos() != 0);
        record.id_type = tvIdType.getCurrentPos();
        record.responsibility = tvResponsibility.getCurrentPos();
        record.driving_tools = tvDrivingTools.getCurrentPos();
        ///月薪
        if (TextUtils.isEmpty(cet_salary.getText().toString())) {
            SnackUtil.show(cet_salary, "您的月薪不能为空");
            return;
        }
        record.salary = Float.valueOf(cet_salary.getText().toString());
        //医药费
        if (TextUtils.isEmpty(cet_hospital_custom.getText().toString()))
            record.medical_free = 0;
        else
            record.medical_free = Float.valueOf(cet_hospital_custom.getText().toString());

        //住院天数
        if (TextUtils.isEmpty(cet_hospital_days.getText().toString()))
            record.hospital_days = 0;
        else
            record.hospital_days = Integer.valueOf(cet_hospital_days.getText().toString());

        //误工天数
        if (TextUtils.isEmpty(cet_tardy_days.getText().toString()))
            record.tardy_days = 0;
        else
            record.tardy_days = Integer.valueOf(cet_tardy_days.getText().toString());

        //营养期
        if (TextUtils.isEmpty(cet_nutrition_days.getText().toString()))
            record.nutrition_days = 0;
        else
            record.nutrition_days = Integer.valueOf(cet_nutrition_days.getText().toString());

        //护理期
        if (TextUtils.isEmpty(cet_nursing_days.getText().toString()))
            record.nursing_days = 0;
        else
            record.nursing_days = Integer.valueOf(cet_nursing_days.getText().toString());

        //需抚养人列表
        List<RelativeItemMsg> relativeItemMsgList = this.msgBaseRecAdapter.getDataList();
        record.setRelative_msg_list(relativeItemMsgList);
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
        }, 2000);
    }

    @Override
    public void showRecordResult(final RecordRes recordRes) {
        if (sDialog != null) {
            if (recordRes != null) {
                sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sDialog.setTitleText("理赔总额：" + recordRes.money_pay)
                        .setConfirmText("查看详情")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                Intent intent = new Intent(getActivity(), ResultActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("recordRes", recordRes);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
            } else {
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
        if (requestCode == 11) {
            String evaluate = data
                    .getStringExtra(RelativesMsgFragment.REQUSET_PERSON_COUNT);
            tvFamilyCustom.setText(evaluate);
        }
    }

    /**
     * 咨询
     */
    @OnClick({R.id.fab_consult})
    public void openConsultPage(View view) {
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
    public void startCalculate(View view) {
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
                }, 2000);
//                mPresenter.prepareCalculate();

            }
        }).show();
    }


}
