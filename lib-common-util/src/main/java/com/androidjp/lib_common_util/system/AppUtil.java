package com.androidjp.lib_common_util.system;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by androidjp on 16-7-1.
 */
public class AppUtil {

    //------------------------------------------------

    /**
     * 获取当前的App的任务栈情况
     *
     * @param context 被检测的app上下文环境
     * @return 返回 当前app的任务栈信息
     */
    public static String getCurrentStack(Context context) {

        StringBuilder sb = new StringBuilder();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(Integer.MAX_VALUE);
        for (int i = 0; i < infos.size(); i++) {
            ActivityManager.RunningTaskInfo info = infos.get(i);
            sb.append("<font color=\"#ff0000\">Stack" + i + "</font>").append("<br/>");
            sb.append("\t <b><i>ID:</i></b>" + info.id).append("<br/>");
            sb.append("\t <b><i>Num Running:</i></b>" + info.numRunning).append("<br/>");
            sb.append("\t <b><i>Num Activities:</i></b>" + info.numActivities).append("<br/>");
            sb.append("\t <b><i>Description:</i></b>" + info.description).append("<br/>");
            sb.append("\t <b><i>Top Activity:</i></b>" + toComponentName(info.topActivity)).append("<br/>");
            sb.append("\t <b><i>Base Activity:</i></b>" + toComponentName(info.baseActivity)).append("<br/>");
        }
        return sb.toString();
    }

    /**
     * 获取 ComponentName 对应的报名+类名
     *
     * @param cn componentName
     * @return 对应的 包名+类名
     */
    private static String toComponentName(ComponentName cn) {
        if (cn == null)
            return null;
        else
            return cn.getPackageName() + "/" + cn.getShortClassName();
    }


    //----------------------------------------------------------
    ///关于 判断本module是否是debug版本
    /**
     * 由于：多Module模式下， Library Module 会以release版本方式编译并给予 依赖它的module，所以，及时 app module使用debug方式编译运行，也不会输出log
     * （因为：BuildConfig 对象依赖的是本module的包名）
     * <p>
     * 以下是三种解决方式
     */
    private static Boolean isDebug = null;

    public static boolean isDebug() {
        return (isDebug == null) ? false : isDebug;
    }

    /**
     * 同步本module是否是debug模式
     *
     * @param context 上下文（ApplicationContext）
     */
    public static void syncIsDebug(Context context) {
        ///方式一：通过反射得到真正执行的Module的BuildConfig，在自己的Application内调用：syncIsDebug()
        /// 但是，有个问题：
        ///   因为 BuildConfig.java 的 packageName 是 Module的PackageName 也就是 AndroidManifest.xml 中的 package 属性
        ///   而 context.getPackage() = 应用的 applicationId （这个applicationId是可以通过build.gradle 修改的）
        ///  当：build.gradle的applicationId 不等于  AndroidManifest.xml 的 package 属性 时， 就会有"找不到类"的问题
//        if (isDebug == null){
//            try {
//                String packageName = context.getApplicationContext().getPackageName();
//                Class buildConfig = Class.forName(packageName+".BuildConfig");
//                Field DEBUG = buildConfig.getField("DEBUG");
//                DEBUG.setAccessible(true);
//                isDebug = DEBUG.getBoolean(null);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
        /// 方式一变形：省略传入context 参数: android.app.ActivityThread.currentPackageName 获取包名

        ///方式二：Lib publishNonDefault（让所有的Lib module都提供除了release方式以外的版本）
        ///  （1）这方式需要所有依赖的库module都加代码：
//        android{
//            publishNonDefault true
//        }
        ///  （2）另外需要在App Module 中将其依赖的Library逐个添加代码：
//        dependencies{
//            releaseCompile project(path: ':library', configuration: 'release');
//            debugCompile project(path: ':library2', configuration: 'debug');
//        }

        // 方式三： 使用 ApplicationInfo.FLAG_DEBUGGABLE
        //   (反编译Bebug包和Release包， 发现，他们的AndroidManifest.xml中application节点的 android:debuggable值不同)
        //   (Debug包值：false  ， Release包值：true)
        //  so，解决方案就是，不使用 BuildConfig，通过ApplicationInfo的flags属性去判断是否是Debug版本
        if (isDebug == null){
            isDebug = context.getApplicationContext()!=null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        }
    }
    ///end-----关于 判断本module是否是debug版本
}
