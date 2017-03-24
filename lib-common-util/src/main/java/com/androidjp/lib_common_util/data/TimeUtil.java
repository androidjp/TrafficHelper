package com.androidjp.lib_common_util.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by androidjp on 2017/3/22.
 */

public class TimeUtil {

    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
        return dateFormat.format(date);
    }
}
