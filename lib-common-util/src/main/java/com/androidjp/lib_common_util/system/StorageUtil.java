package com.androidjp.lib_common_util.system;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;


import java.io.File;

/**
 * 外部SD卡和内部存储相关
 * SD卡相关权限：
 * 1.在SDCard中创建与删除文件权限
 <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 * 2.往SDCard写入数据权限
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * Created by JP on 2016/4/4.
 */
public class StorageUtil {





    /**
     * SD卡存在并可用？
     * @return
     */
    public static boolean isSDWorks(){
        String state = Environment.getExternalStorageState();
        if(state!=null &&( state.equals(Environment.MEDIA_MOUNTED) ||!Environment.isExternalStorageRemovable())){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 存储的用量情况
     */
    public static long getUsableSpace(File path){
        if (path == null)
            return -1;
        /*API >=9,直接用File的方法可以计算可用空间*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }else {/*API8和以前*/
            if (!path.exists()){
                return 0;
            }else{
                final StatFs statFs = new StatFs(path.getPath());
                return statFs.getBlockSizeLong()*statFs.getAvailableBlocksLong();
            }
        }
    }



    /**
     * 选择获取路径
     * @param choice
     * @return
     */
    public static String getDir(DirEnum choice){
        String res = null;
       switch (choice){
           case DataDir:
               res = Environment.getDataDirectory().getAbsolutePath();
               break;
           case ExternalStorageDir:
               res = Environment.getExternalStorageDirectory().getAbsolutePath();
               res = Environment.getExternalStoragePublicDirectory("").getAbsolutePath();
               break;
           case AppRootDir:

               break;
           case DownloadCacheDir:
               res = Environment.getDownloadCacheDirectory().getAbsolutePath();
               break;
           case SystemDir:
               res = Environment.getRootDirectory().getAbsolutePath();
               break;
           case FileDir:
//               res = Environment.getDataDirectory().getAbsolutePath();
//               ToastUtil.showShort(res);
//               res = MyAppl.getContext().getFilesDir().getAbsolutePath();
//               ToastUtil.showShort(res);
               break;
           case ExternalFileDir:
//               res = Environment.getExternalStoragePublicDirectory("files").getAbsolutePath();
//               if (StorageUtil.isSDWorks()){
//                   res = MyAppl.getContext().getExternalFilesDir("").getAbsolutePath();
//                   ToastUtil.showShort(res);
//               }
               break;

           case CacheDir:
//               res = MyAppl.getContext().getCacheDir().getAbsolutePath();
//               ToastUtil.showShort(res);
               break;
           case ExternalCacheDir:
//               res = Environment.getExternalStoragePublicDirectory("cache").getAbsolutePath();
//               ToastUtil.showShort(res);
//               if (StorageUtil.isSDWorks()){
//                   res = MyAppl.getContext().getExternalCacheDir().getAbsolutePath();
//                   ToastUtil.showShort(res);
//               }
               break;

           default:
               res = null;
               break;
       }
        if (TextUtils.isEmpty(res)){
            res = "";
        }
        return res;
    }

    public static enum DirEnum{
        /*$rootDir*/
        DataDir /*/data*/
        ,ExternalStorageDir/*/storage/sdcard0 或者 /storage/emulated/0*/

        /*appDataDir*/
        ,AppRootDir/*$rootDir/data/$packagename*/
        ,ExternalAppRootDir/*$rootDir/Android/data/$packagename*/

        ,DownloadCacheDir/*/cache*/
        ,SystemDir/*/system*/

        ,CacheDir/*$rootDir/data/#packagename/cache*/
        ,ExternalCacheDir/*$rootDir/Android/data/$packagename/cache*/

        ,FileDir/*$rootDir/data/$package/file*/
        ,ExternalFileDir/*$rootDir*/

    }

}
