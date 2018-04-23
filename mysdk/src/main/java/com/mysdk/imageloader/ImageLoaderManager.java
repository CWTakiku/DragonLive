package com.mysdk.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.mysdk.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by cwl on 2017/5/7.
 */

public class ImageLoaderManager {

    private static final  int THREAD_COUNT=4;//标明我们的UIL最多可以有多少线程
    private static final  int PRIOPRITY=2;//标明我们图片加载的优选级
    private static final  int READ_TIME_OUT=30*1000;//读取超时时间
    private static final  int DISK_CACHE_SIZE=50*1024;//标明我们URL最大可以缓存的图片数量
    private static final  int CONNECTION_TIME_OUT=5*1000;//连接超时时间

    private  static ImageLoader mImageLoader=null;

    private static  ImageLoaderManager mInstance=null;

    public static ImageLoaderManager getInstance(Context context){
        if(mInstance==null){
            synchronized (ImageLoaderManager.class){
                if(mInstance==null){
                    mInstance=new ImageLoaderManager(context);
                }
            }
        }
        return mInstance;
    }
    private ImageLoaderManager(Context context){
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.
                Builder(context)
                .threadPoolSize(THREAD_COUNT)
                .threadPriority(Thread.NORM_PRIORITY-PRIOPRITY)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(DISK_CACHE_SIZE)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(getDefaultOptions())
                .imageDownloader(new BaseImageDownloader(context,CONNECTION_TIME_OUT,READ_TIME_OUT))
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(configuration);
        mImageLoader=ImageLoader.getInstance();
    }


    private DisplayImageOptions getDefaultOptions() {
         DisplayImageOptions options=new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.xadsdk_img_error)
        .showImageOnFail(R.drawable.xadsdk_img_error)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .decodingOptions(new BitmapFactory.Options())
        .build();
        return options;
    }
    public void displayImage(ImageView imageview, String path,DisplayImageOptions options, ImageLoadingListener listener){

        if(mImageLoader!=null){

            mImageLoader.displayImage(path,imageview,options,listener);

        }
    }
    public void  displayImage(ImageView imageview, String url, ImageLoadingListener listener){
        displayImage(imageview,url,null,listener);
    }
    public void  displayImage(ImageView imageview, String path){
        displayImage(imageview,path,null,null);
    }

}
