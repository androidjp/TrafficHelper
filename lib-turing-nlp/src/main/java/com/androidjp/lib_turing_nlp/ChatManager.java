package com.androidjp.lib_turing_nlp;

import android.content.Context;
import android.text.TextUtils;

import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.RecognizeManager;
import com.turing.androidsdk.TuringManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 对话管理员
 * 使用：Turing机器人 API
 * Created by androidjp on 2017/2/20.
 */

public class ChatManager {

    private Context mContext;
    /// 语音识别（语音转文本相关）
    private RecognizeManager mRecognizeManager;
    ///文本请求发送与接收
    private TuringManager mTuringManager;
    private ChatListener mChatListener;
    private static ChatManager sInstance;


    private ChatManager(Context context) {
        this.mContext = context;
//        init();
    }


    public static ChatManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ChatListener.class) {
                if (sInstance == null)
                    sInstance = new ChatManager(context);
            }
        }
        return sInstance;
    }

    public ChatManager init(String turing_api_key, String turing_secret) {
        mTuringManager = new TuringManager(mContext, turing_api_key, turing_secret);
        mTuringManager.setHttpRequestListener(new HttpRequestListener() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject!=null){
                        String answer = jsonObject.getString("text");
                        if (!TextUtils.isEmpty(answer)){
                            if (mChatListener != null)
                                mChatListener.onChatSuccess(s);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int i, String s) {
                if (mChatListener != null)
                    mChatListener.onChatFail(i, s);
            }
        });
        return this;
    }

    /**
     * 设置监听
     * @param listener 对话监听
     */
    public ChatManager setChatListener(ChatListener listener) {
        this.mChatListener = listener;
        return this;
    }

    /**
     * 语音录入 -> 语音识别 -> 获取识别结果 -> 利用识别结果进行请求
     */
    public void startRecognize(){
        this.mRecognizeManager.startRecognize();
    }

    public void stopRecognize(){
        this.mRecognizeManager.stopRecognize();
    }

    /**
     * 异步发送请求消息
     * @param info 请求消息
     */
    public void sendDialogue(String info){
        //TODO: 是否是异步地？ 怎么用R小Java来改？
        this.mTuringManager.requestTuring(info);
    }



    public interface ChatListener {
        /**
         * 返回机器人对话成功
         *
         * @param s 返回对话
         */
        public void onChatSuccess(String s);

        /**
         * 返回机器人对话失败
         *
         * @param i 返回码
         * @param s 返回msg
         */
        public void onChatFail(int i, String s);
    }
}
