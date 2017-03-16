package com.androidjp.lib_common_util.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by androidjp on 16-7-23.
 */
public class GsonUtil {
    private static Gson gson = null;

    static{
        if (gson == null){
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd:HH:mm").create();
        }
    }

    private GsonUtil(){

    }

    /**
     * Object 转 json String
     * @param object 对象
     * @return json String
     */
    public static String gsonString(Object object){
        String gsonString = null;
        if (gson != null)
            gsonString = gson.toJson(object);
        return gsonString;
    }


    /**
     * json String 转 Object
     * @param gsonString
     * @param cls
     * @param <T>
     * @return
     */
    public  static <T> T gsonToBean(String gsonString, Class<T> cls){
        T t = null;
        if (gson != null && gsonString !=null && !gsonString.equals("")){
           try{
               t = gson.fromJson(gsonString,cls);
           }catch (Exception e){
               System.out.println(cls.getSimpleName()+" "+ e.toString());
           }
        }
        return t;
    }


    /**
     * 解析成 List<T>等泛型数组
     * @param <T>  泛型:String、Object等
     * @param gsonString json串
     * @param typeToken 泛型类型
     * @return
     */
    public static <T> T gsonToBean(String gsonString, TypeToken<T> typeToken){
        T t = null;
        if (gson != null && !TextUtils.isEmpty(gsonString)){
            try{
                t = gson.fromJson(gsonString,typeToken.getType());
            }catch (Exception e){
                System.out.println(typeToken.getType()+" "+ e.toString());
            }
        }
        return t;
    }


}
