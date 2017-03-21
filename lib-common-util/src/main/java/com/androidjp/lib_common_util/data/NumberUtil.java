package com.androidjp.lib_common_util.data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Int ，double等类型数据的适配工具类
 * Created by androidjp on 16-7-21.
 */
public class NumberUtil {


    /**
     * @param num 目的数
     * @return 输出保留2位小数的double
     */
    public static double doubleRest(double num){
        return doubleRest(num , 2);
    }

    /**
     *
     * @param num 目的数
     * @return 输出保留2位小数的String
     */
    public static String doubleRestStr(double num){
        //方式二
//        DecimalFormat df = new DecimalFormat("#.00");
//        return df.format(num);

        //方式三
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return  decimalFormat.format(num);//format 返回的是字符串
    }



    /**
     * 输出保留2位小数的String
     * @param num 目的数
     * @param tiny 保留小数位
     * @return
     */
    public static double doubleRest(double num ,int tiny){
        //方式一（四舍五入）
        BigDecimal b = new BigDecimal(num);
        double num1  = b.setScale(tiny, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num1;
    }




}
