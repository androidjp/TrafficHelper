package com.androidjp.traffichelper.user;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.androidjp.lib_common_util.pojo.network.Result;
import com.androidjp.traffichelper.THApplication;
import com.androidjp.traffichelper.data.ServiceAPI;
import com.androidjp.traffichelper.data.model.UserManager;
import com.androidjp.traffichelper.data.model.picture_select.PictureSelectManager;
import com.androidjp.traffichelper.data.model.retrofit.ServiceGenerator;
import com.androidjp.traffichelper.data.pojo.User;
import com.luck.picture.lib.model.PictureConfig;
import com.orhanobut.logger.Logger;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * UserPresenter
 * Created by androidjp on 2017/1/12.
 */

public class UserPresenter implements UserContract.Presenter {
    private SoftReference<UserContract.View> mView;
    private Context mContext;

    private List<LocalMedia> pictureMediaList;


    public UserPresenter(Context context, UserContract.View view) {
        this.mContext = context;
        this.mView = new SoftReference<UserContract.View>(view);
    }


    @Override
    public void selectUserPic() {
        PictureSelectManager.getInstance().choiceOnePicture(mContext, new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {
//                        Log.i("callBack_result", list.size() + "");
                if (list != null && list.size() > 0) {
                    pictureMediaList = list;
//                adapter.setList(selectMedia);
//                adapter.notifyDataSetChanged();
                    //TODO: 获取第一个item，拿到用户头像，即可
                    LocalMedia item = list.get(0);
                    Logger.i("LocalMedia.getPath() : " + item.getCompressPath());
                    ///TODO: 让mPresenter去上传用户头像，最后，让Glide去加载图片
                    if(mView!=null)
                        mView.get().loading();
                    uploadUserPic(item.getCompressPath());
                }
            }
        });
    }


    private void uploadUserPic(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Logger.e("刚刚选择的头像文件不存在");
            return;
        }
        ServiceAPI.UserAPI userAPI = ServiceGenerator.createService(ServiceAPI.UserAPI.class);
        //user_pic
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("user_pic", file.getName(), requestFile);
        //user_id
        // MediaType.parse("multipart/form-data")
        RequestBody requestUserId = RequestBody.create(MediaType.parse("multipart/form-data"),UserManager.getInstance(THApplication.getContext()).getUserId());
        Flowable<Result<User>> call = userAPI.updateUserPic(requestUserId,body);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResult -> {
                    if (userResult == null) {
                        if (mView != null)
                            mView.get().finishLoad(null);
                    } else {
                        User user = userResult.data;
                        if (user != null)
                            UserManager.getInstance(THApplication.getContext()).refreshUser(user);
                        if (mView != null)
                            mView.get().finishLoad(user);
                    }
                }, throwable -> {
                    if (mView!=null)
                        mView.get().finishLoad(null);
                });
    }


    @Override
    public void modifyName(String newName) {
        if (TextUtils.isEmpty(newName))
            return;
        if (mView!=null)
            mView.get().loading();

        try {
            Logger.i("原来的user_name是"+newName+", 长度是"+newName.getBytes().length);
            Logger.i("utf-8后, 长度是"+new String(newName.getBytes(),"utf-8").getBytes().length);
            newName = new String(newName.getBytes(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            Logger.e("UserPresenter","昵称修改出问题了！！！编码转换异常");
            e.printStackTrace();
        }

        ServiceAPI.UserAPI userAPI = ServiceGenerator.createService(ServiceAPI.UserAPI.class);
        Flowable<Result<User>> call = userAPI.updateUserName(UserManager.getInstance(THApplication.getContext()).getUserId(),newName);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResult -> {
                    if (userResult == null) {
                        if (mView != null)
                            mView.get().finishLoad(null);
                    } else {
                        User user = userResult.data;
                        if (user != null)
                            UserManager.getInstance(THApplication.getContext()).refreshUser(user);
                        if (mView != null)
                            mView.get().finishLoad(user);
                    }
                }, throwable -> {
                    if (mView!=null)
                        mView.get().finishLoad(null);
                });
    }

    @Override
    public void modifyPhone(String newPhone) {
        if (TextUtils.isEmpty(newPhone))
            return;
        if (mView != null)
            mView.get().loading();
        ServiceAPI.UserAPI userAPI = ServiceGenerator.createService(ServiceAPI.UserAPI.class);
        Flowable<Result<User>> call = userAPI.updatePhone(UserManager.getInstance(THApplication.getContext()).getUserId(), newPhone);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResult -> {
                    if (userResult == null) {
                        if (mView != null)
                            mView.get().finishLoad(null);
                    } else {
                        User user = userResult.data;
                        if (user != null)
                            UserManager.getInstance(THApplication.getContext()).refreshUser(user);
                        if (mView != null)
                            mView.get().finishLoad(user);
                    }
                }, throwable -> {
                    if (mView != null)
                        mView.get().finishLoad(null);
                });
    }
    @Override
    public void modifyEmail(String newEmail) {
        if (TextUtils.isEmpty(newEmail))
            return;
        if (mView!=null)
            mView.get().loading();
        ServiceAPI.UserAPI userAPI = ServiceGenerator.createService(ServiceAPI.UserAPI.class);
        Flowable<Result<User>> call = userAPI.updateEmail(UserManager.getInstance(THApplication.getContext()).getUserId(),newEmail);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResult -> {
                    if (userResult == null) {
                        if (mView != null)
                            mView.get().finishLoad(null);
                    } else {
                        User user = userResult.data;
                        if (user != null)
                            UserManager.getInstance(THApplication.getContext()).refreshUser(user);
                        if (mView != null)
                            mView.get().finishLoad(user);
                    }
                }, throwable -> {
                    if (mView!=null)
                        mView.get().finishLoad(null);
                });
    }

    @Override
    public void modifyAge(int age) {
        if (mView!=null)
            mView.get().loading();
        ServiceAPI.UserAPI userAPI = ServiceGenerator.createService(ServiceAPI.UserAPI.class);
        Flowable<Result<User>> call = userAPI.updateAge(UserManager.getInstance(THApplication.getContext()).getUserId(),age);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResult -> {
                    if (userResult == null) {
                        if (mView != null)
                            mView.get().finishLoad(null);
                    } else {
                        User user = userResult.data;
                        if (user != null)
                            UserManager.getInstance(THApplication.getContext()).refreshUser(user);
                        if (mView != null)
                            mView.get().finishLoad(user);
                    }
                }, throwable -> {
                        if (mView!=null)
                            mView.get().finishLoad(null);
                });
    }

    @Override
    public void modifySex(int sex) {
        if (mView!=null)
            mView.get().loading();
        ServiceAPI.UserAPI userAPI = ServiceGenerator.createService(ServiceAPI.UserAPI.class);
        Flowable<Result<User>> call = userAPI.updateSex(UserManager.getInstance(THApplication.getContext()).getUserId(),Integer.valueOf(sex));
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResult -> {
                    if (userResult == null) {
                        if (mView != null)
                            mView.get().finishLoad(null);
                    } else {
                        User user = userResult.data;
                        if (user != null)
                            UserManager.getInstance(THApplication.getContext()).refreshUser(user);
                        if (mView != null)
                            mView.get().finishLoad(user);
                    }
                }, throwable -> {
                    if (mView!=null)
                        mView.get().finishLoad(null);
                });
    }

    @Override
    public void modifyPwd(String pwd) {
        if (TextUtils.isEmpty(pwd))
            return;
        if (mView!=null)
            mView.get().loading();
        ServiceAPI.UserAPI userAPI = ServiceGenerator.createService(ServiceAPI.UserAPI.class);
        Flowable<Result<User>> call = userAPI.updateUserPwd(UserManager.getInstance(THApplication.getContext()).getUserId(),pwd);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResult -> {
                    if (userResult == null) {
                        if (mView != null)
                            mView.get().finishLoad(null);
                    } else {
                        User user = userResult.data;
                        if (user != null)
                            UserManager.getInstance(THApplication.getContext()).refreshUser(user);
                        if (mView != null)
                            mView.get().finishLoad(user);
                    }
                }, throwable -> {
                    if (mView!=null)
                        mView.get().finishLoad(null);
                });
    }

    @Override
    public void logout() {
        UserManager.getInstance(THApplication.getContext()).removeUser();
        if (mView!=null)
            mView.get().finishLoad(null);
        ((Activity)mContext).finish();
    }

    @Override
    public void previewUserPic() {
        if (this.pictureMediaList!=null && this.pictureMediaList.size()>0)
        PictureConfig.getPictureConfig().externalPicturePreview(mContext, 0, this.pictureMediaList);
    }

    @Override
    public void start() {
        ///获取User，并让Fragment刷新起来
        User user = UserManager.getInstance(THApplication.getContext()).getUser();
        if (user != null && this.mView != null)
            this.mView.get().finishLoad(user);
    }
}
