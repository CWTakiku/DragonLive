package com.mysdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mysdk.okhttp.listener.DisposeDataHandle;
import com.mysdk.okhttp.listener.DisposeDownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by cwl on 2017/5/17.
 */

public class CommonFileCallback implements Callback {

    private static final int TYPE_SUCCESS=0;
    private static final int TYPE_FAILED=1;
    private static final int TYPE_PAUSED=2;
    private static final int TYPE_CANCELED=3;


    /**
     * 发送到UI线程的
     */
    private DisposeDownloadListener mListener;
    private String mfilePath;
   // private int progress;
    private Handler mUIhandler;
    private static final int PROGRESS_MESSAGE = 0x01;
    private static final int SUCCESS_MESSAGE=0X02;



    public CommonFileCallback(DisposeDataHandle handle){
        this.mfilePath=handle.mSource;
        this.mListener=handle.downloadListener;
        this.mUIhandler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
              switch (msg.what){
                  case PROGRESS_MESSAGE:
                      mListener.onProgress((int)msg.obj);
                      break;
                  case SUCCESS_MESSAGE:
                      mListener.onSuccess();
                      break;

              }
            }
        };
    }

    @Override
    public void onFailure(Call call, IOException e) {
mUIhandler.post(new Runnable() {
    @Override
    public void run() {
        mListener.onFailed();
    }
});
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.i("info1", "onResponse: "+response.toString());
       long contentLength=response.body().contentLength();
        Log.i("info1", "onResponse: do"+contentLength);
        File file;
        InputStream is=null;
        RandomAccessFile savedFile=null;
        try {
            long downloadedLength=0;//已经下载的文件长度

            checkLocalFilePath(mfilePath);
            file=new File(mfilePath);
            if (file.exists()){
                downloadedLength=file.length();
            }

            if (contentLength==0){
                mUIhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onFailed();
                    }
                });
            }else if(contentLength==downloadedLength){
                mUIhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onSuccess();
                    }
                });
                if (is!=null){
                    is.close();
                   // Log.i("info", "onResponse:close ");
                }
                if (savedFile!=null){
                    savedFile.close();
                  //  Log.i("info", "onResponse:close ");
                }
                return;
            }

            if (response!=null){
                is=response.body().byteStream();
                savedFile=new RandomAccessFile(file,"rw");
               savedFile.seek(downloadedLength);//跳过已经下载的字节
                byte[] b=new byte[1024];
                int total=0;
                int len=0;
               // Log.i("info", "onResponse: "+downloadedLength);
                while ((len=is.read(b))!=-1){
                   total+=len;
                    savedFile.write(b,0,len);
                    int progress= (int) ((total+downloadedLength)*100/contentLength);
                    mUIhandler.obtainMessage(PROGRESS_MESSAGE, progress).sendToTarget();
                    }
                    savedFile.close();
                mUIhandler.obtainMessage(SUCCESS_MESSAGE,0).sendToTarget();
                }
            }catch (Exception e){
          file=null;
        }finally {
            try {
                if (is!=null){
                    is.close();
                }
                if (savedFile!=null){
                    savedFile.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //Log.i("info", "onResponse:2 ");
      // mListener.onFailed();
    }
    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
