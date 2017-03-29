package com.androidjp.lib_common_util.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * 封装好的SharedPreferences辅助类
 * Created by androidjp on 2017/1/4.
 */

public class SharedPrefHelper {

    private static final String FILE_NAME = "traffic_helper_spf";

    private static SharedPrefHelper sInstance;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    private SharedPrefHelper(Context context) {
        if (context==null){
            Log.e("SharedPrefHelper", "context is null !!!");
        }
        this.spf = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.editor = this.spf.edit();
    }

    //获取资源
    public static SharedPrefHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SharedPrefHelper.class) {
                if (sInstance == null) {
                    sInstance = new SharedPrefHelper(context);
                }
            }
        }
        return sInstance;
    }

    ///释放资源
    public void release() {
        if (sInstance != null) {
            sInstance = null;
            System.gc();
        }
    }

    //清除所有数据
    public boolean clear() {
        return editor.clear().commit();
    }

    public boolean put(String key ,String value){
        return editor.putString(key,value).commit();
    }
    public String getAsString(String key,String def){
        return spf.getString(key,def);
    }

    public boolean put(String key, boolean value) {
        return editor.putBoolean(key, value).commit();
    }

    public boolean getAsBoolean(String key, boolean def) {
        return spf.getBoolean(key, def);
    }

    public boolean put(String key, int value) {
        return editor.putInt(key, value).commit();
    }

    public int getAsInt(String key, int def) {
        return spf.getInt(key, def);
    }

    public boolean put(String key, long value) {
        return editor.putLong(key, value).commit();
    }

    public long getAsLong(String key, long def) {
        return spf.getLong(key, def);
    }

    public boolean put(String key, float value) {
        return editor.putFloat(key, value).commit();
    }

    public float getAsLong(String key, float def) {
        return spf.getFloat(key, def);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean put(String key, Set<String> value) {
        return editor.putStringSet(key, value).commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getAsLong(String key, Set<String> def) {
        return spf.getStringSet(key, def);
    }

    /**
     * JSONObject
     */
    public void put(String key, JSONObject value) {
        saveObject(key, value);

    }

    public JSONObject getAsJSONObject(String key) {
        Object object = readObject(key);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        return null;
    }

    /**
     * JSONArray
     */
    public void put(String key, JSONArray value) {
        saveObject(key, value);
    }

    public JSONArray getAsJSONArray(String key) {
        Object object = readObject(key);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        return null;
    }

    /**
     * byte[]
     */
    public void put(String key , byte[] value){
        saveObject(key,value);
    }

    public byte[] getAsBytes(String key){
        Object object = readObject(key);
        if (object instanceof byte[]){
            return (byte[])object;
        }
        return null;
    }

    /**
     * Serializable数据
     */
    public void put(String key , Serializable value){
        saveObject(key , value);
    }

    public Serializable getAsSerializable(String key){
        Object object = readObject(key);
        if (object instanceof Serializable)
            return (Serializable)object;
        return null;
    }

    /**
     * ditmap
     */
    public void put(String key  , Bitmap value){
        saveObject(key,value);
    }
    public Bitmap getAsBitmap(String key){
        Object object = readObject(key);
        if (object instanceof Bitmap)
            return (Bitmap)object;
        return null;
    }
    /**
     * Drawable
     */
    public void put(String key  , Drawable value){
        saveObject(key,value);
    }
    public Drawable getAsDrawable(String key){
        Object object = readObject(key);
        if (object instanceof Drawable)
            return (Drawable) object;
        return null;
    }



    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------

    private Object readObject(String key) {
        try {
            if (spf.contains(key)) {
                String str = spf.getString(key, "");
                if (TextUtils.isEmpty(str))
                    return null;
                else {
                    //16进制数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(str);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    ///返回出一个Object对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //16进制字符串 -> byte数组
    private byte[] StringToBytes(String str) {
        String hexString = str.toUpperCase().trim();

        if (hexString.length() % 2 != 0)
            return null;
        ///最终结果返回
        byte[] resBytes = new byte[hexString.length() / 2];
        ////两位两位16进制数的转换
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;///两位16进制数转换后的10进制数
            char hex_char1 = hexString.charAt(i);

            int higher_ch, lower_ch;///高位*16 + 低位
            ///高位
            if (hex_char1 >= '0' && hex_char1 <= '9')
                higher_ch = (hex_char1 - 48) * 16;
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                higher_ch = (hex_char1 - 55) * 16;
            else
                return null;
            //低位
            char hex_char2 = hexString.charAt(++i);
            ///高位
            if (hex_char2 >= '0' && hex_char2 <= '9')
                lower_ch = (hex_char2 - 48);
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                lower_ch = (hex_char1 - 55);
            else
                return null;

            int_ch = higher_ch + lower_ch;
            resBytes[i / 2] = (byte) int_ch;///int -> byte
        }
        return resBytes;
    }

    private void saveObject(String key, Object obj) {
        try {
            ///先将序列化结果写到byte缓存中（分配一个内存空间）
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            ///将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化对象转为16进制
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            ///保存该16进制数组
            editor.putString(key, bytesToHexString).commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SharedPrefHelper", "保存obj失败");
        }
    }

    /**
     * 将数组转成16进制
     *
     * @param bytes 源bytes数组
     * @return 16进制字符串
     */
    private String bytesToHexString(byte[] bytes) {
        if (bytes == null) return null;
        if (bytes.length == 0)
            return "";
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 删除指定字段
     */
    public boolean deleteKeyValue(String key){
        return editor.remove(key).commit();
    }
}
