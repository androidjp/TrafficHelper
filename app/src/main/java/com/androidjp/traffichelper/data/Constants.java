package com.androidjp.traffichelper.data;

/**
 * 常量列表
 * Created by androidjp on 2016/12/9.
 */

public class Constants {

    /**
     * 性别 --> USER_SEX
     */
    public static final int MALE = 0;
    public static final int FEMALE = 1;


    /**
     * 网络操作相关情况标志
     */
    public static final int FINISH_LOGIN = 0x11;
    public static final int FAIL_LOGIN = 0x12;
    public static final int FINISH_REGISTER = 0x13;
    public static final int FAIL_REGUSTER = 0x14;

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

    //上传文件存放目录
    public static final String UPLOAD_DIR = "upload";
    public static final String USER_IMG_DIR = "user_image_cache";
    //上传配置常数
    public static final int MEMORY_THRESHOLD = 1024*1024*3;//3MB
    public static final int MAX_FILE_SIZE = 1024*1024*40;//40MB
    public static final int MAX_REQUEST_SIZE = 1024*1024*50;//50MB

    //Record相关常量
    public static final String PAGE  = "page";
    public static final String FIRST_RECORD_ID  = "first_record_id";
    public static final String RECORD_ID = "record_id";
    public static final String RECORD_TIME = "record_time";
    public static final String HURT_LEVEL = "hurt_level";
    public static final String SALARY = "salary";
    public static final String RELATIVES_COUNT = "relatives_count";
    public static final String HAS_SPOUSE = "has_spouse";
    public static final String ID_TYPE = "id_type";
    public static final String RESPONSIBILITY = "responsibility";
    public static final String DRIVING_TOOLS = "driving_tools";
    public static final String MEDICAL_FREE = "medical_free";
    public static final String HOSPITAL_DAYS = "hospital_days";
    public static final String NUTRITION_DAYS = "nutrition_days";
    public static final String NURSING_DAYS = "nursing_days";
    public static final String PAY = "pay";



    ///需抚养人信息
    public static final String RELATIVE_ITEM_MSG_ID = "relative_item_msg_id";
    public static final String RELATION = "relation";


    //location 信息
    public static final String CITY = "city";
    public static final String PROVINCE = "province";
    public static final String STREET = "street";
    public static final String LOCATION_ID = "location_id";
    public static final String LATITUDE = "latitdue";
    public static final String LONGITUDE = "longitude";

    //RecordRes信息
    public static final String RESULT_ID = "result_id";
    public static final String MONEY_PAY = "money_pay";
    public static final String MONEY_HURT = "money_hurt";
    public static final String MONEY_HEART = "money_heart";
    public static final String MONEY_NURSING = "money_nursing";
    public static final String MONEY_TARDY = "money_tardy";
    public static final String MONEY_MEDICAL = "money_medical";
    public static final String MONEY_NUTRITION = "money_nutrition";
    public static final String MONEY_HOSPITAL_ALLOWANCE = "money_hospital_allowance";
    public static final String MONEY_RELATIVES = "money_relatives";
    public static final String MONEY_HURT_INFO = "money_hurt_info";
    public static final String MONEY_HEART_INFO = "money_heart_info";
    public static final String MONEY_NURSING_INFO = "money_nursing_info";
    public static final String MONEY_NUTRITION_INFO = "money_nutrition_info";
    public static final String MONEY_TARDY_INFO = "money_tardy_info";
    public static final String MONEY_MEDICAL_INFO = "money_medical_info";
    public static final String MONEY_HOSPITAL_ALLOWANCE_INFO = "money_hospital_allowance_info";
    public static final String MONEY_RELATIVES_INFO = "money_relatives_info";
    public static final String MONEY_BURY_INFO = "money_bury_info";


}
