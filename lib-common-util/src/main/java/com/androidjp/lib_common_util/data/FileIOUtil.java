package com.androidjp.lib_common_util.data;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件IO操作
 * Created by androidjp on 16-7-21.
 */
public class FileIOUtil {
    private static final String TAG = "FileUtil";


    public static final int TYPE_BYTE = 0x11;///字节读写（InputStream，OutputStream）
    public static final int TYPE_CHAR = 0x12;///字符读写（FileReader、FileWriter）
    public static final int TYPE_BUFFERED = 0x13;//行读写（BufferedReader 和  BufferedWriter）
    public static final int TYPE_OBJ = 0x14;///对象读写（ObjectInputStream）


    /**
     * 获取文件内容（字节形式）
     * @param fileName 文件绝对路径
     * @return 内容
     */
    public static String readFileByByte(String fileName) {
        File file = new File(fileName);
        InputStream inputStream = null;
        StringBuilder sb = null;
        try {
            sb = new StringBuilder(256);
            inputStream = new FileInputStream(file);
            int temp;
            byte[] bytes = new byte[1024];///表示1B为单位的数据,每次最多获取1KB 的数据
            while ((temp = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, temp));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 字符为单位来读取文件内容
     * @param fileName 文件绝对路径
     * @return 内容
     */
    public static String readFileByChar(String fileName){
        File file = new File(fileName);
        FileReader reader = null;
        StringBuilder sb = null;
        try{
            sb = new StringBuilder(256);
            reader = new FileReader(file);
            int temp ;
            char[] chs = new char[40];
            while((temp = reader.read(chs)) != -1){
//                if (((char) temp) != '\r') {
//
//                }
                sb.append(new String(chs,0,temp));
            }
            return sb.toString();
        }catch(IOException e){
            e.getStackTrace();
        }finally{
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /**
     * 以行为单位读取文件内容
     * @param filePath
     */
    public static void readFileByLine(String filePath){
        File file = new File(filePath);
        // BufferedReader:从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。
        BufferedReader buf = null;
        try{
            // FileReader:用来读取字符文件的便捷类。
            buf = new BufferedReader(new FileReader(file));
            // buf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String temp = null ;
            while ((temp = buf.readLine()) != null ){
                System.out.println(temp);
            }
        }catch(Exception e){
            e.getStackTrace();
        }finally{
            if(buf != null){
                try{
                    buf.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
    }

    /**
     * 以字节为单位读写文件内容
     *
     * @param filePath
     * @param pastePath
     *            ：需要读取的文件路径
     */
    public static void readFileByByte(String filePath,String pastePath) {
        File file = new File(filePath);
        // InputStream:此抽象类是表示字节输入流的所有类的超类。
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // FileInputStream:从文件系统中的某个文件中获得输入字节。
            ins = new FileInputStream(file);
            outs = new FileOutputStream(pastePath);
            int temp;
            // read():从输入流中读取数据的下一个字节。
            while ((temp = ins.read()) != -1) {
                outs.write(temp);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (ins != null && outs != null) {
                try {
                    outs.close();
                    ins.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
    }

    /**
     * 以字符为单位读写文件内容
     *
     * @param filePath
     * @param pastePath
     */
    public static void readFileByCharacter(String filePath,String pastePath) {
        File file = new File(filePath);
        // FileReader:用来读取字符文件的便捷类。
        FileReader reader = null;
        FileWriter writer = null;
        try {
            reader = new FileReader(file);
            writer = new FileWriter(pastePath);
            int temp;
            while ((temp = reader.read()) != -1) {
                writer.write((char)temp);
            }
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            if (reader != null && writer != null) {
                try {
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以行为单位读写文件内容
     *
     * @param filePath
     */
    public static void readFileByLine(String filePath,String pastePath) {
        File file = new File(filePath);
        // BufferedReader:从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。
        BufferedReader bufReader = null;
        BufferedWriter bufWriter = null;
        try {
            // FileReader:用来读取字符文件的便捷类。
            bufReader = new BufferedReader(new FileReader(file));
            bufWriter = new BufferedWriter(new FileWriter(pastePath));
            // buf = new BufferedReader(new InputStreamReader(new
            // FileInputStream(file)));
            String temp = null;
            while ((temp = bufReader.readLine()) != null) {
                bufWriter.write(temp+"\n");
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (bufReader != null && bufWriter != null) {
                try {
                    bufReader.close();
                    bufWriter.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
    }

    /**
     * 使用Java.nio ByteBuffer字节将一个文件输出至另一文件
     *
     * @param filePath
     */
    public static void readFileByBybeBuffer(String filePath,String pastePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            // 获取源文件和目标文件的输入输出流
            in = new FileInputStream(filePath);
            out = new FileOutputStream(pastePath);
            // 获取输入输出通道
            FileChannel fcIn = in.getChannel();
            FileChannel fcOut = out.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                // clear方法重设缓冲区，使它可以接受读入的数据
                buffer.clear();
                // 从输入通道中将数据读到缓冲区
                int r = fcIn.read(buffer);
                if (r == -1) {
                    break;
                }
                // flip方法让缓冲区可以将新读入的数据写入另一个通道
                buffer.flip();
                fcOut.write(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null && out != null) {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }





    /**
     * * 读取dat文件信息
     *
     * @param fileName dat文件路径
     * @return 对象列表数据
     */
    public static List<? extends Object> readDatFile(String fileName) {

        if (!FileUtil.isExist(fileName))
            return null;

        List<?> list = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(fileName)));
            list = (List<?>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            list = null;
            e.printStackTrace();
        } finally {
            return list;
        }
    }





    /**
     * 写入普通文件
     *
     * @param fileName 文件路径
     * @param content  内容
     * @param type     写入方式
     * @return true：成功 ，false：失败
     */
    public static boolean writeFile(String fileName, String content, int type) {

        if (TextUtils.isEmpty(content))
            return false;

        if (!FileUtil.isExist(fileName))
            return false;


        switch (type) {
            case TYPE_BUFFERED:
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));
                    bw.write(content);
                    bw.close();
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }

                break;
            case TYPE_BYTE:
                try {
                    FileOutputStream fis = new FileOutputStream(new File(fileName));
                    fis.write(content.getBytes());
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }
                break;
            default:
                return false;
        }

        return false;
    }


    /**
     * @param fileName dat文件路径
     * @param list     对象列表数据
     * @param <T>      泛型
     * @return
     */
    public static <T> boolean writeDatFile(String fileName, List<T> list) {

        if (list == null || list.size() == 0)
            return false;

        if (!FileUtil.isExist(fileName))
            return false;

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            oos.writeObject(list);
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }


}
