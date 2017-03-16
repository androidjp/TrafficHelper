package com.androidjp.traffichelper.consult;

import android.os.Bundle;

import com.androidjp.lib_google_mvp.BasePresenter;
import com.androidjp.lib_google_mvp.BaseView;
import com.androidjp.traffichelper.data.pojo.Dialogue;

import java.util.List;

/**
 * 咨询 约定类
 * Created by androidjp on 2016/12/26.
 */

public class ConsultContract {

    public interface View extends BaseView<Presenter>{
        //输出回答
        void loadList(List<Dialogue> list);
        void showAnswer(Dialogue answer);
        void showQuestion(Dialogue question);

        void showSpeechText(String text);
        void showSpeechBtnText(String text);
    }

    public interface Presenter extends BasePresenter {

        ///提交问题（String）,TODO:注意，语音输入
        void sendQuestion(String question);
        ///开始语音
        void startYuYin();
        //录音停止，解析中
        void stopAndDeal();
        //取消语音录入
        void cancelYuYin();
        //
        void toggleYuYin();

        void getIntentBundle(Bundle bundle);
    }

}
