package com.androidjp.traffichelper.consult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.androidjp.lib_baidu_asr.BaiduASR;
import com.androidjp.lib_turing_nlp.ChatManager;
import com.androidjp.traffichelper.R;
import com.androidjp.traffichelper.data.pojo.Dialogue;
import com.baidu.speech.VoiceRecognitionService;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;

import static com.androidjp.lib_baidu_asr.asr.ASRStatus.STATUS_None;
import static com.androidjp.lib_baidu_asr.asr.ASRStatus.STATUS_Ready;
import static com.androidjp.lib_baidu_asr.asr.ASRStatus.STATUS_Recognition;
import static com.androidjp.lib_baidu_asr.asr.ASRStatus.STATUS_Speaking;
import static com.androidjp.lib_baidu_asr.asr.ASRStatus.STATUS_WaitingReady;

/**
 * Consult 主持人
 * Created by androidjp on 2017/1/1.
 */

public class ConsultPresenter implements ConsultContract.Presenter
    ,RecognitionListener
        ,ChatManager.ChatListener
{
    private Context mContext;
    private SoftReference<ConsultContract.View> mView;
    ///相关参数
    private int status = STATUS_None;
    private long time;
    private long speechEndTime = -1;
    private static final int EVENT_ERROR = 11;


    public ConsultPresenter(Context mContext,ConsultContract.View mView) {
        this.mContext = mContext;
        this.mView = new SoftReference<ConsultContract.View>(mView);
    }

    @Override
    public void sendQuestion(String question) {
        Dialogue que = new Dialogue("-1", null,question);
        if (this.mView.get()!=null){
            this.mView.get().showQuestion(que);
        }

        ///TODO: 传给图灵机器人 理赔姐 ，让其处理

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(mContext)) {
                // Do stuff here
                ChatManager.getInstance(mContext)
                        .init(mContext.getResources().getString(R.string.turing_api_key), mContext.getResources().getString(R.string.turing_secret))
                        .setChatListener(this).sendDialogue(question);
            }
            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }
    }


    @Override
    public void start() {

    }

    @Override
    public void startYuYin() {
        if (mView !=null)
            mView.get().showSpeechText("");
        BaiduASR.startASR((Activity)mContext, this);
    }

    @Override
    public void stopAndDeal() {
        BaiduASR.stopASR();
    }

    @Override
    public void cancelYuYin() {
        BaiduASR.cancelASR();
    }

    @Override
    public void toggleYuYin() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean api = sp.getBoolean("api", false);
        Log.i("MainPresenter","toggleYuYin() --> SharedPreferences 可读");
        if (api) {
            switch (status) {
                case STATUS_None:
                    startYuYin();
                    status = STATUS_WaitingReady;
                    if (mView !=null)
                        mView.get().showSpeechBtnText("取消");
                    break;
                case STATUS_WaitingReady:
                    cancelYuYin();
                    status = STATUS_None;
                    if (mView !=null)
                        mView.get().showSpeechBtnText("开始");
                    break;
                case STATUS_Ready:
                    cancelYuYin();
                    status = STATUS_None;
                    if (mView !=null)
                        mView.get().showSpeechBtnText("开始");
                    break;
                case STATUS_Speaking:
                    stopAndDeal();
                    status = STATUS_Recognition;
                    if (mView !=null)
                        mView.get().showSpeechBtnText("识别中");
                    break;
                case STATUS_Recognition:
                    cancelYuYin();
                    status = STATUS_None;
                    if (mView !=null)
                        mView.get().showSpeechBtnText("开始");
                    break;
            }
        } else {
            startYuYin();
        }
    }

    @Override
    public void getIntentBundle(Bundle bundle) {
        onResults(bundle);
    }


    ///----------------------------------------------------------------


    @Override
    public void onReadyForSpeech(Bundle params) {
        status = STATUS_Ready;
        Log.d("onReadyForSpeech()","准备就绪，可以开始说话");
    }

    @Override
    public void onBeginningOfSpeech() {
        time = System.currentTimeMillis();
        status = STATUS_Speaking;
        Log.d("onBeginningOfSpeech()","检测到用户的已经开始说话");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        speechEndTime = System.currentTimeMillis();
        status = STATUS_Recognition;
        Log.d("onEndOfSpeech()","检测到用户的已经停止说话");
        mView.get().showSpeechBtnText("识别中");
    }

    @Override
    public void onError(int error) {
        time = 0;
        status = STATUS_None;
        StringBuilder sb = new StringBuilder();
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                sb.append("音频问题");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                sb.append("没有语音输入");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                sb.append("其它客户端错误");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                sb.append("权限不足");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                sb.append("网络问题");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                sb.append("没有匹配的识别结果");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                sb.append("引擎忙");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                sb.append("服务端错误");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                sb.append("连接超时");
                break;
        }
        sb.append(":" + error);
        Log.e("MainPresenter.onError()","识别失败：" + sb.toString());
        mView.get().showSpeechBtnText("开始");
    }

    @Override
    public void onResults(Bundle results) {
        long end2finish = System.currentTimeMillis() - speechEndTime;
        status = STATUS_None;
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d("onResults()","识别成功：" + Arrays.toString(nbest.toArray(new String[nbest.size()])));
        String json_res = results.getString("origin_result");
        try {
            Log.d("onResults()","origin_result=\n" + new JSONObject(json_res).toString(4));
        } catch (Exception e) {
            Log.d("onResults()","origin_result=[warning: bad json]\n" + json_res);
        }
        mView.get().showSpeechBtnText("开始");
        String strEnd2Finish = "";
        if (end2finish < 60 * 1000) {
            strEnd2Finish = "(waited " + end2finish + "ms)";
        }
//        输出 识别的内容 + 语音识别总时长
//        mView.get().showSpeechText(nbest.get(0) + strEnd2Finish);
        mView.get().showSpeechText(nbest.get(0));
        time = 0;
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> nbest = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            Log.d("onPartialResults()","~临时识别结果：" + Arrays.toString(nbest.toArray(new String[0])));
            mView.get().showSpeechText(nbest.get(0));
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        switch (eventType) {
            case EVENT_ERROR:
                String reason = params.get("reason") + "";
                Log.e("MainPresenter.onEvent()","EVENT_ERROR, " + reason);
                break;
            case VoiceRecognitionService.EVENT_ENGINE_SWITCH:
                int type = params.getInt("engine_type");
                Log.d("MainPresenter.onEvent()","*引擎切换至" + (type == 0 ? "在线" : "离线"));
                break;
        }
    }

    @Override
    public void onChatSuccess(String s) {
        if (this.mView!=null){
            try{
                JSONObject jsonObject = new JSONObject(s);
                String answer = jsonObject.getString("text");
                if (this.mView!=null)
                this.mView.get().showAnswer(new Dialogue(null,null,answer));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onChatFail(int i, String s) {
        if (mView!=null)
            mView.get().showAnswer(new Dialogue(null, null ,i+" , "+s));
    }

}
