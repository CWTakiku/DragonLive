package com.mysdk.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

/**
 * Created by cwl on 2018/3/28.
 */

public class GlideUtil {

    /**
     * 默认加载
     * @param path
     * @param mImageView
     */
    public static void  loadImageView(Context mContext, String path, ImageView mImageView){
        Glide.with(mContext).load(path).into(mImageView);
    }
    public static void loadLocalImage(Context mContext,int res,  ImageView  mImageView){
        Glide.with(mContext).load(res).into(mImageView);
    }

    /**
     * 加载指定大小
     * @param path
     * @param width
     * @param height
     * @param mImageView
     */
    public static void loadImageViewSize(Context mContext,String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).into(mImageView);
    }

    /**
     * 加载圆形
     * @param mContext
     * @param path
     * @param mImageView
     * @param radius
     */
    public static void loadImageCircle(Context mContext,String path, ImageView mImageView,int radius){
        Glide.with(mContext).load(path).transform(new CornersTransform(mContext,radius)).into(mImageView);
    }

    public static void loadBitmapCircle(Context mContext,Bitmap bitmap, ImageView mImageView, int radius){
        Glide.with(mContext).load(bitmap).transform(new CornersTransform(mContext,radius)).into(mImageView);
    }

    /**
     * 设置加载中以及加载失败图片
     * @param path
     * @param mImageView
     * @param lodingImage
     * @param errorImageView
     */
    public static void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    /**
     * 设置下载优先级
     * @param path
     * @param mImageView
     */
    public static void loadImageViewPriority( Context mContext,String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 设置加载动画
     * @param path
     * @param anim
     * @param mImageView
     */
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /**
     * 设置监听请求接口
     * @param path
     * @param mImageView
     * @param requstlistener
     */
    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }
    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }
    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }
}
