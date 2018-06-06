package dragonlive.cwl.com.dragonlive.updateapk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mysdk.okhttp.download.DownloadTask;
import com.mysdk.okhttp.listener.DisposeDownloadListener;

import java.io.File;

import dragonlive.cwl.com.dragonlive.R;


/**
 * Created by cwl on 2017/5/16.
 */

public class DownloadService extends Service {

    private DownloadTask downloadTask;
    private String downloadUrl;// 下载地址
    private String filePath; //保存地址

    public DownloadService(){

    }


    private DisposeDownloadListener listener=new DisposeDownloadListener() {
        @Override
        public void onSuccess() {
          downloadTask=null;

            //下载成功将前台服务通知关闭，并创建一个下载成功的通知
              stopForeground(true);
          getNotificationManager().notify(1,getNotification(getString(R.string.update_download_finish),-1));
            startActivity(getInstallApkIntent());
        }

        @Override
        public void onFailed() {
         downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification(getString(R.string.update_download_failed),-1));
        }

        @Override
        public void onPaused() {
        downloadTask=null;

        }

        @Override
        public void onCanceld() {
            downloadTask=null;
            stopForeground(true);
            deleteApkFile();
        }

        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification(getString(R.string.update_download_processing),progress));
        }
    };
    private Notification getNotification(String title, int progress) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        if (progress > 0) {
            //当进度大于或等于0时才需要显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        if (progress>=100){
            builder.setContentIntent(getContentIntent());//安装
        }
        return builder.build();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

  private DownloadBinder mBinder=new DownloadBinder();

    public class DownloadBinder extends Binder {
        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadTask = new DownloadTask(listener);
                downloadUrl = url;
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                filePath=directory+fileName;
                deleteApkFile();
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification(getString(R.string.update_download_processing), 0));
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    //取消下载时需要将文件删除，并将通知关闭
                    deleteApkFile();
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                }
            }
        }
    }


    private PendingIntent getContentIntent() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, getInstallApkIntent(),
                PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }

        /**
         * 下载完成，安装
         *
         * @return
         */
        private Intent getInstallApkIntent() {
            File apkfile = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            return intent;
        }

        /**
         * 删除无用apk文件
         */
        private boolean deleteApkFile() {
            if (filePath!=null) {
                File apkFile = new File(filePath);
                if (apkFile.exists() && apkFile.isFile()) {
                    return apkFile.delete();
                }
            }
            return false;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return mBinder;
        }
}
