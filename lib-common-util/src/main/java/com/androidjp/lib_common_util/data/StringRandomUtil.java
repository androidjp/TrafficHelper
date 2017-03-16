package com.androidjp.lib_common_util.data;



import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 数字、字符串随机生成工具类
 * 方便测试数据
 */
public class StringRandomUtil {

    private Random widthRandom = new Random();
    private int length;
    private static char[] charsNumber = ("0123456789").toCharArray();
    private static char[] charsLetter = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    private static char[] charsRandom = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    private static Random random = new Random();
    private final static int[] areaCode = {1601, 1637, 1833, 2078, 2274,
            2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
            4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};
    private final static String[] letters = {"a", "b", "c", "d", "e",
            "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "w", "x", "y", "z"};

    //参数为生成的字符串的长度，根据给定的char集合生成字符串数字
    public static String randomNumber(int length) {

        char[] data = new char[length];

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charsNumber.length);
            data[i] = charsNumber[index];
        }
        String s = new String(data);
        return s;
    }

    //参数为生成的字符串的长度，根据给定的char集合生成字符串、字母
    public static String randomLetter(int length) {

        char[] data = new char[length];

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charsLetter.length);
            data[i] = charsLetter[index];
        }
        String s = new String(data);
        return s;
    }

    //参数为生成的字符串的长度，根据给定的char集合生成字符串
    public static String getStringRandom(int length) {

        char[] data = new char[length];

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charsRandom.length);
            data[i] = charsRandom[index];
        }
        String s = new String(data);
        return s;
    }

    //简体中文
    public static String getRandomJianHan(int length) {
        String ret = "";
        for (int i = 0; i < length; i++) {
            String str = null;
            int hightPos, lowPos;//定义高地位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK");//转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }

    /*封装成带下划线的蓝色文本*/
    public static String toBlueTextUnderLine(String text) {
        return "<u><font color=\"#15ABF9\">" + text + "</font></u>";
    }


    /**
     * 获取汉字的首字母
     * @param x
     * @return
     */
    public static String getFirstAlp(String x) {

        String chinese = x.substring(0,1);

        if (chinese == null || chinese.trim().length() == 0) {
            return "";
        }
        chinese = conversionStr(chinese, "GB2312", "ISO8859-1");

        if (chinese.length() > 1) // 判断是不是汉字
        {
            int li_SectorCode = (int) chinese.charAt(0); // 汉字区码
            int li_PositionCode = (int) chinese.charAt(1); // 汉字位码
            li_SectorCode = li_SectorCode - 160;
            li_PositionCode = li_PositionCode - 160;
            int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
            if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
//                ToastUtils.showShort(MyApplication.getContext(),"chinese 是汉字字符");
                for (int i = 0; i < 23; i++) {
                    if (li_SecPosCode >= areaCode[i]
                            && li_SecPosCode < areaCode[i + 1]) {
                        chinese = letters[i];
                        break;
                    }
                }
            } else // 非汉字字符,如图形符号或ASCII码
            {
//                ToastUtils.showShort(MyApplication.getContext(),"chinese 不是汉字！！");
                chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
                chinese = chinese.substring(0, 1);
            }
        }
        return chinese;
    }


    /**
     * 字符串编码转换
     *
     * @param str           要转换编码的字符串
     * @param charsetName   原来的编码
     * @param toCharsetName 转换后的编码
     * @return 经过编码转换后的字符串
     */
    private static String conversionStr(String str, String charsetName, String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("字符串编码转换异常：" + ex.getMessage());
        }
        return str;
    }

}
