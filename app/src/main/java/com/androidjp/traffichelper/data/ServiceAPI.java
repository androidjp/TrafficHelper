package com.androidjp.traffichelper.data;

import com.androidjp.lib_common_util.pojo.network.Result;
import com.androidjp.traffichelper.data.pojo.Location;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 请求API集合类
 * Created by androidjp on 2017/1/12.
 */

public class ServiceAPI {

    public static boolean IS_DEBUG = true;////如果是debug，则使用本地的服务端url，否则使用云端url

    /// debug 服务端地址
    public static final String SERVER_HOST = "http://10.0.2.2:8080/SpringDemo/";
    /// release 服务端地址
    public static final String REMOTE_SERVER_HOST = "http://139.199.6.201:8080/SpringDemo/";


    private static final String LOGIN_PATH = "servlets/LoginServlet/";
    private static final String REGISTER_PATH = "servlets/RegisterServlet/";
    private static final String USER_MANAGER_PATH = "servlets/UserManager/";
    private static final String RECORD_PATH = "servlets/RecordServlet/";
    private static final String LOCATION_PATH = "servlets/LocationServlet/";

    private static final String TEST_PATH = "servlets/test/TestConnection/";

    ///----- 各种key -----------

    ///User信息
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_pwd";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String SEX = "sex";
    public static final String KIND = "kind";
    public static final String USER_PIC = "user_pic";
    public static final String AGE = "age";

    /// Record 记录


    public interface LoginAPI {

        ///登录
        @POST(TEST_PATH)
        @FormUrlEncoded
        Call<Result<User>> login(@Field("user_id") String user_id, @Field("user_pwd") String password);

        ///注册
        @POST(TEST_PATH)
        Call<Result<User>> register(@Body User user);
    }

    public interface UserAPI{
        ///更改昵称
        @POST(USER_MANAGER_PATH)
        Call<User> updateUserName(@Field("user_id") String user_id, @Field("user_name") String userName);

        @POST(USER_MANAGER_PATH)
        Call<User> updateUserPic(@Field("user_id") String user_id, @Field("user_pic") String userPic);

        @POST(USER_MANAGER_PATH)
        Call<User> updateAge(@Field("user_id") String user_id, @Field("age") int age);

        @POST(USER_MANAGER_PATH)
        Call<User> updateUserPwd(@Field("user_id") String user_id, @Field("user_pwd") String user_pwd);

        @POST(USER_MANAGER_PATH)
        Call<User> updateEmail(@Field("user_id") String user_id, @Field("email") String EMAIL);

        @POST(USER_MANAGER_PATH)
        Call<User> updatePhone(@Field("user_id") String user_id, @Field("phone") String phone);

        @POST(USER_MANAGER_PATH)
        Call<User> updateSex(@Field("user_id") String user_id, @Field("sex") int sex);

    }


    public interface RecordAPI {
        ///上传一个计算记录，并返回得到一个计算结果
        @POST(RECORD_PATH)
        Call<RecordRes> addRecord(@Body Record record);
    }

    public interface LocationAPI {
        /**
         * 上传一个定位：
         * 后端判断是否存在一模一样内容的Location：
         * 如果存在：那么返回这个一模一样内容的Location对象（只返回一个location_id）
         * 如果不存在：后端存储这个新的location对象，返回新的location对象的location_id
         */
        @POST(LOCATION_PATH)
        Call<String> uploadLocation(@Body Location location);
    }

}
