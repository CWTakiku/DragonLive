package dragonlive.cwl.com.dragonlive.util;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import dragonlive.cwl.com.dragonlive.application.MyApplication;

/**
 * Created by cwl on 2018/4/26.
 */

public class AliYunOssHelper {
    private String bucket;
    private String key;
     public AliYunOssHelper(){
     }
    public  void uploadToOSS(final String bucket, final String key, String path){
       // Log.i("info1", "uoloadToOSS: "+path);
         this.bucket=bucket;
        this.key=key;
        PutObjectRequest put = new PutObjectRequest(bucket, key, path);
// 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task =  MyApplication.getApplication().getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
               // Log.d("PutObject", "UploadSuccess");
               // Log.d("ETag", result.getETag());
                //Log.d("RequestId", result.getRequestId());

                //Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
                // 生成URL
                String url = MyApplication.getApplication().getOss().presignPublicObjectURL(bucket,key);
               // Log.i("info1", "onSuccessurl: "+url);
                 monResultListener.onSuccess(url);
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
               // Log.i("info1", "onFailure:upload ");
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.i("info1", "onFailure: toUpload");
                    Log.i("info1", serviceException.getErrorCode());
                    Log.i("info1", serviceException.getRequestId());
                    Log.i("info1", serviceException.getHostId());
                    Log.i("info1", serviceException.getRawMessage());
                }
                if (serviceException==null)
                    Log.i("info1", "onFailure: null");
                monResultListener.onFailure("上传失败");
            }
        });
        task.waitUntilFinished();

    }
    private OnResultListener monResultListener;
    public void setOnResultListener(OnResultListener onResultListenr){
        monResultListener= onResultListenr;
    }
    public interface OnResultListener{
     void onSuccess(String url);
    void onFailure(String msg);

    }
}
