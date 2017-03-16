package com.androidjp.lib_common_util.net;

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 基础网络请求工具类
 * Created by androidjp on 2017/1/11.
 */

public class HttpURLConnectionUtil {
    public static final String TAG = "HttpURLConnectionUtil";

    /**
     * 使用HttpURLConnection 来进行post请求【注意子线程中使用】
     *
     * @param url     URL
     * @param content 请求参数
     * @return 响应结果
     */
    public static String post(String url, String content) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);///设置此方法允许向服务器输出内容

            ///post请求参数
            String data = content;
            OutputStream out = conn.getOutputStream();///获取一个输出流向服务端写数据（默认是不允许的）
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();///此前不必再次调用connect()方法

            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            } else {
                throw new NetworkErrorException("response status is " + responseCode);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "创建URL对象过程出错");
            e.printStackTrace();
        } catch (IOException | NetworkErrorException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String get(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            } else {
                throw new NetworkErrorException("response status is " + responseCode);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "创建URL对象过程出错");
            e.printStackTrace();
        } catch (IOException | NetworkErrorException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }


    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];///基本单位是1B，每次最多读1KB
        int len = -1;
        while ((len = is.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }
        is.close();
        String res = os.toString();///把流中的数据转换为字符串，采用的编码是utf-8（模拟器默认编码）
        os.close();
        return res;
    }
}
