package com.androidjp.lib_common_util.data;

import java.util.regex.Pattern;

/**
 * 正则表达式 工具类
 * Created by androidjp on 2017/2/6.
 */

public class ExpressionUtil {
    private static final String INT_OR_FLOAT = "^[0-9]+\\.{0,1}[0-9]{0,2}$";
    private static final String JUST_NUMBER = "^[0-9]*$";
    private static final String JUST_N_NUMBER = "^\\d{11}$";
    private static final String MORE_THAN_N_NUMBER = "^\\d{n,}$";
    private static final String BETWEEN_M_N_NUMBER = "^\\d{m,n}$";
    ///十进制数
    private static final String NUM_TEN = "^(0|[1-9]|[0-9]*)$";
    private static final String NUM_POSITIVE_TWO_SMALL = "^[0-9]+(.[0-9]{2})?$";
    ///一位到三位小数的正整数
    private static final String NUM_POSITIVE_BETWEEN_1_TO_3_SMALL = "^[0-9]+(.[0-9]{1,3})?$";
    private static final String NUM_POSITIVE_NOT_ZERO = "^\\+?[1-9][0-9]*$";
    private static final String NUM_NEGATIVE_NOT_ZERO = "^\\-[1-9][]0-9\"*$";
    private static final String STRING_LEN_3 = "^.{3}$";
    private static final String JUST_ENGLISH = "^[A-Za-z]+$";
    private static final String JUST_UPPERCASE = "^[A-Z]+$";
    private static final String JUST_LOWERCASE = "^[a-z]+$";
    private static final String NUMBER_AND_ENGLISH = "^[A-Za-z0-9]+$";
    private static final String NUMBER_OR_ENGLISH_OR_UNDERLINE = "^\\w+$";
    //密码格式：以字母开头，长度在6~18之间，只能包含字符、数字和下划线
    private static final String PASSWORD = "^[a-zA-Z]\\w{5,17}$";
    ///检查不寻常的字符
    private static final String CHECK_UNUSUAL_CHAR = "[^%&',;=?$\\x22]+";
    ///只有汉字
    private static final String CHINESE = "^[\\u4e00-\\u9fa5]{0,}$";
    ///邮箱格式
    private static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    ///验证InternetURL
    private static final String INTERNET_URL = "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
    ///电话号码
//    private static final String PHONE = "^(\\(\\d{3,4}"+"-)|\\d{3.4}"+"-)?\\d"+"{7,8}$";
    private static final String PHONE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    //身份证
    private static final String ID_CARD = "^\\d{15}|\\d{18}$";
    ///一年的12个月(01~09 和 1~12)
    private static final String MONTH_OF_YEAR = "^(0?[1-9]|1[0-2])$";
    ///一月的31天（01~09 ， 1~31）
    private static final String DATE_OF_MONEH = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
    ///中文字符
    private static final String CHINESE_CHAR = "[\\u4e00-\\u9fa5]";
    ///双字节字符（包括汉字）
    private static final String DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";


    public static boolean isPhoneNumber(String str){
        return Pattern.matches(PHONE, str);
    }

    public static boolean isPassword(String str){
        return Pattern.matches(PASSWORD,str);
    }

    public static boolean isEmail(String str){
        return Pattern.matches(EMAIL,str);
    }



}
