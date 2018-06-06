package com.mysdk.okhttp.download;

import android.os.AsyncTask;
import android.os.Environment;

import com.mysdk.okhttp.listener.DisposeDownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cwl on 2018/5/31.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private boolean isCanceled = false;
    private boolean isPauesd = false;
    private int lastProgress;

    private DisposeDownloadListener downloadListener;

    public DownloadTask(DisposeDownloadListener listener){
        downloadListener=listener;
    }




    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try{
            long downloadedLength = 0; //记录已下载的文件长度
            String downloadUrl = params[0];
            String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file=new File(directory+fileName);
            if (file.exists()){
                downloadedLength=file.length(); //如果文件存在 已下载的文件长度就等于文件的长度
            }
            long contentLength=getContent(downloadUrl);
            if (contentLength==0){
                return TYPE_FAILED;
            }else if (contentLength==downloadedLength){
                return TYPE_SUCCESS;
            }
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().addHeader("RANGE","bytes="+downloadedLength+"-")
                    .url(downloadUrl)
                    .build();
             Response response=client.newCall(request).execute();
            if (response!=null){
                is=response.body().byteStream();
                savedFile=new RandomAccessFile(file,"rw");
                savedFile.seek(downloadedLength);//跳过已下载的字节
                byte[] b=new byte[1024];
                int total=0;
                int len;
                while ((len=is.read(b))!=-1){
                    if (isCanceled){
                        return TYPE_CANCELED;
                    }else if (isPauesd){
                        return TYPE_PAUSED;
                    }else {
                        total+=len;
                        savedFile.write(b,0,len);
                        int progress=(int)((total+downloadedLength)*100/contentLength); //计算下载百分百
                        publishProgress(progress);//更新进度条
                    }
                }
                response.close();
                return TYPE_SUCCESS;
            }
    } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is!=null){
                    is.close();
                }
                if (savedFile!=null){
                    savedFile.close();
                }
                if (isCancelled()&&file!=null){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }
    //文件总长度
    private long getContent(String downloadUrl) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(downloadUrl)
                .build();
          Response response= client.newCall(request).execute();
        if (response!=null&&response.isSuccessful()){
            long contentLength=response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
     int progress=values[0];
        if (progress>lastProgress){
            downloadListener.onProgress(progress);
            lastProgress=progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
      switch (status){
          case TYPE_CANCELED:
              downloadListener.onCanceld();
              break;
          case TYPE_FAILED:
              downloadListener.onFailed();
              break;
          case TYPE_PAUSED:
              downloadListener.onPaused();
              break;
          case TYPE_SUCCESS:
              downloadListener.onSuccess();
              break;
          default:break;
      }
    }

    public void pauseDownload(){
        isPauesd=true;
    }
    public void cancelDownload(){
        isCanceled=true;
    }
}
