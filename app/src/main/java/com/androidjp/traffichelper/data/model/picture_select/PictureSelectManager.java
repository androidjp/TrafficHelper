package com.androidjp.traffichelper.data.model.picture_select;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.androidjp.traffichelper.R;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.model.PictureConfig;
import com.orhanobut.logger.Logger;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择管理者
 * （封装PictureSelector库相关设置与操作）
 * Created by androidjp on 2017/3/27.
 */

public class PictureSelectManager {

    /**
     * type --> 1图片 or 2视频
     * copyMode -->裁剪比例，默认、1:1、3:4、3:2、16:9
     * maxSelectNum --> 可选择图片的数量
     * selectMode         --> 单选 or 多选
     * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
     * isPreview    --> 是否打开预览选项
     * isCrop       --> 是否打开剪切选项
     * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
     * ThemeStyle -->主题颜色
     * CheckedBoxDrawable -->图片勾选样式
     * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
     * cropH-->裁剪高度 值不能小于100
     * isCompress -->是否压缩图片
     * setEnablePixelCompress 是否启用像素压缩
     * setEnableQualityCompress 是否启用质量压缩
     * setRecordVideoSecond 录视频的秒数，默认不限制
     * setRecordVideoDefinition 视频清晰度  Constants.HIGH 清晰  Constants.ORDINARY 低质量
     * setImageSpanCount -->每行显示个数
     * setCheckNumMode 是否显示QQ选择风格(带数字效果)
     * setPreviewColor 预览文字颜色
     * setCompleteColor 完成文字颜色
     * setPreviewBottomBgColor 预览界面底部背景色
     * setBottomBgColor 选择图片页面底部背景色
     * setCompressQuality 设置裁剪质量，默认无损裁剪
     * setSelectMedia 已选择的图片
     * setCompressFlag 1为系统自带压缩  2为第三方luban压缩
     * 注意-->type为2时 设置isPreview or isCrop 无效
     * 注意：Options可以为空，默认标准模式
     */
    private int selectMode = FunctionConfig.MODE_MULTIPLE;
    private int maxSelectNum = 9;// 图片最大可选数量
    private boolean isShow = true;///是否显示拍照选项（默认自动根据type 启动拍照或者录视频）
    private int selectType = LocalMediaLoader.TYPE_IMAGE;///1图片 or 2视频
    private int copyMode = FunctionConfig.CROP_MODEL_DEFAULT;///裁剪比例：默认、1:1、3:4、3:2、16:9
    private boolean enablePreview = true;//是否允许预览
    private boolean isPreviewVideo = true;//是否预览视频
    private boolean enableCrop = true;///是否允许裁剪
    private boolean theme = false;//是否 启动 自定义主题颜色
    private boolean selectImageType = false;
    private int cropW = 0;
    private int cropH = 0;
    private int compressW = 0;
    private int compressH = 0;
    private boolean isCompress = false;
    private boolean isCheckNumMode = false;
    private int compressFlag = 1;// 1 系统自带压缩 2 luban压缩
    private List<LocalMedia> selectMedia = new ArrayList<>();


    private PictureSelectManager(){

    }

    public static PictureSelectManager getInstance(){
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder{
        private static final PictureSelectManager sInstance = new PictureSelectManager();
    }

    //================//================//================//================


    private void init(){

    }

    /**
     * 选择一张图片
     */
    public void choiceOnePicture(Context context , PictureConfig.OnSelectResultCallback selectResultCallback){

        ///TODO:设置单个用户头像获取的配置

        selectMode = FunctionConfig.MODE_SINGLE;
        maxSelectNum = 1;// 图片最大可选数量
        isShow = true;///是否显示拍照选项（默认自动根据type 启动拍照或者录视频）
        selectType = LocalMediaLoader.TYPE_IMAGE;///1图片 or 2视频
        copyMode = FunctionConfig.CROP_MODEL_1_1;///裁剪比例：默认、1:1、3:4、3:2、16:9
        enablePreview = true;//是否允许预览
        isPreviewVideo = true;//是否预览视频
        enableCrop = true;///是否允许裁剪
        theme = true;//是否 启动 自定义主题颜色
        selectImageType = false;
        cropW = 0;
        cropH = 0;
        compressW = 0;
        compressH = 0;
        isCompress = true;
        isCheckNumMode = true;
        compressFlag = 1;// 1 系统自带压缩 2 luban压缩

       startChoice(context,selectResultCallback);
    }

    private void startChoice(Context context, PictureConfig.OnSelectResultCallback selectResultCallback) {
        //默认裁剪宽度
        int selector = R.drawable.select_cb;
        FunctionConfig config = new FunctionConfig();
        config.setType(selectType);
        config.setCopyMode(copyMode);
        config.setCompress(isCompress);
        config.setEnablePixelCompress(true);
        config.setEnableQualityCompress(true);
        config.setMaxSelectNum(maxSelectNum);
        config.setSelectMode(selectMode);
        config.setShowCamera(isShow);
        config.setEnablePreview(enablePreview);
        config.setEnableCrop(enableCrop);
        config.setPreviewVideo(isPreviewVideo);
        config.setRecordVideoDefinition(FunctionConfig.HIGH);// 视频清晰度
        config.setRecordVideoSecond(60);// 视频秒数
        //裁剪尺寸
        config.setCropW(cropW);
        config.setCropH(cropH);
        config.setCheckNumMode(isCheckNumMode);
        config.setCompressQuality(100);//压缩质量
        config.setImageSpanCount(4);
        config.setSelectMedia(selectMedia);
        ///压缩设置
        config.setCompressFlag(compressFlag);
        config.setCompressW(compressW);
        config.setCompressH(compressH);
        if (theme){
            config.setThemeStyle(ContextCompat.getColor(context, R.color.blue));
            // 可以自定义底部 预览 完成 文字的颜色和背景色
            if (!isCheckNumMode) {
                // QQ 风格模式下 这里自己搭配颜色，使用蓝色可能会不好看
                config.setPreviewColor(ContextCompat.getColor(context, R.color.white));
                config.setCompleteColor(ContextCompat.getColor(context, R.color.white));
                config.setPreviewBottomBgColor(ContextCompat.getColor(context, R.color.blue));
                config.setBottomBgColor(ContextCompat.getColor(context, R.color.blue));
            }
        }
        if (selectImageType) {
            config.setCheckedBoxDrawable(selector);
        }

        // 先初始化参数配置，在启动相册
        PictureConfig.init(config);
        PictureConfig.getPictureConfig().openPhoto(context, selectResultCallback);

    }
    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            if (selectMedia != null) {
//                adapter.setList(selectMedia);
//                adapter.notifyDataSetChanged();
                //TODO: 获取第一个item，拿到用户头像，即可
                LocalMedia item = selectMedia.get(0);
                Logger.i("LocalMedia.getPath() : "+item.getPath());
            }
        }
    };

    /**
     * 预览指定的一张图片
     */
    public void previewOnePicture(){

    }

}
