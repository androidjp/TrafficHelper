package com.androidjp.traffichelper.data;

import com.androidjp.lib_common_util.pojo.network.Result;
import com.androidjp.traffichelper.data.pojo.Record;
import com.androidjp.traffichelper.data.pojo.RecordRes;
import com.androidjp.traffichelper.data.pojo.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 请求API集合类
 * Created by androidjp on 2017/1/12.
 */

public class ServiceAPI {

    public static boolean IS_DEBUG = false;////如果是debug，则使用本地的服务端url，否则使用云端url

    /// debug 服务端地址
    public static final String SERVER_HOST = "http://10.0.2.2:8080/SpringDemo/";
    /// release 服务端地址
    public static final String REMOTE_SERVER_HOST = "http://139.199.6.201:8080/SpringDemo/";

    public static  final int PAGE_COUNT = 8;///设定每页加载item数量最多为8个



    private static final String LOGIN_PATH = "servlets/login/LoginServlet";
    private static final String REGISTER_PATH = "servlets/login/RegisterServlet";
    private static final String USER_MANAGER_PATH = "servlets/user/UserServlet";
//    private static final String USER_PIC_UPLOAD_PATH = "servlets/upload/UploadServlet";
    private static final String USER_PIC_UPLOAD_PATH_TEST = "servlets/upload/MyUploadServlet";
    private static final String RECORD_QUERY_PATH = "servlets/record/RecordQueryServlet";
    private static final String RECORD_ADD_PATH = "servlets/record/RecordAddServlet";
    private static final String RECORD_DETAIL_PATH = "servlets/record/RecordDetailServlet";

    ///TODO：测试相关，后期可删
    private static final String TEST_LOCATION_PATH = "servlets/test/TestLocation";

    ///----- 各种key -----------


    /// Record 记录


    public interface LoginAPI {

        ///登录
        @POST(LOGIN_PATH)
        @FormUrlEncoded
        Call<Result<User>> login(@Field("user_id") String user_id, @Field("user_pwd") String password);

        ///注册
        @POST(REGISTER_PATH)
        @FormUrlEncoded
        Call<Result<String>> register(@FieldMap Map<String,String> userMsgMap);
    }

    public interface UserAPI{
        ///更改昵称

        @POST(USER_MANAGER_PATH)
        @FormUrlEncoded
        Call<Result<User>> updateUserName(@Field("user_id") String user_id, @Field(value="user_name",encoded=true) String userName);

//        @POST(USER_MANAGER_PATH)
//        Call<Result<User>>> updateUserPic(@Field("user_id") String user_id, @Field("user_pic") String userPic);



        //原来 鸿洋的做法
        @Multipart
        @POST(USER_PIC_UPLOAD_PATH_TEST)
        Call<Result<User>> updateUserPic(@Part("user_id") RequestBody user_id, @Part MultipartBody.Part file);

        @POST(USER_MANAGER_PATH)
        @FormUrlEncoded
        Call<Result<User>> updateUserPwd(@Field("user_id") String user_id, @Field("user_pwd") String user_pwd);

        @POST(USER_MANAGER_PATH)
        @FormUrlEncoded
        Call<Result<User>> updateEmail(@Field("user_id") String user_id, @Field("email") String EMAIL);

        @POST(USER_MANAGER_PATH)
        @FormUrlEncoded
        Call<Result<User>> updateAge(@Field("user_id") String user_id, @Field("age") int age);

        @POST(USER_MANAGER_PATH)
        @FormUrlEncoded
        Call<Result<User>> updatePhone(@Field("user_id") String user_id, @Field("phone") String phone);

        @POST(USER_MANAGER_PATH)
        @FormUrlEncoded
        Call<Result<User>> updateSex(@Field("user_id") String user_id, @Field("sex") int sex);

    }


    public interface RecordAPI {
        ///上传一个计算记录，并返回得到一个计算结果
        @POST(RECORD_ADD_PATH)
        Call<Result<RecordRes>> addRecord(@Body Record record);
        // 参数： user_id + 当前请求页 + （如果是下拉刷新，则获取新增的record 列表，此时需要本地的第一个pos的record_id ，以确定是否有新的record产生）
        @POST(RECORD_QUERY_PATH)
        @FormUrlEncoded
        Call<Result<List<Record>>> queryRecordList(@Field(Constants.USER_ID)String user_id, @Field(Constants.PAGE)int page ,@Field(Constants.RECORD_TIME) long record_time);
        @POST(RECORD_DETAIL_PATH)
        @FormUrlEncoded
        Call<Result<RecordRes>> getRecordDetail(@Field(Constants.RESULT_ID) String result_id);
    }


    public interface TestAPI{

        @POST(TEST_LOCATION_PATH)
        @FormUrlEncoded
        Call<String> testLocation(@Field(Constants.CITY) String city);
    }

}
