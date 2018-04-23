package com.mysdk.okhttp.listener;

/**
 * Created by cwl on 2017/5/17.
 */

public interface DisposeDownloadListener {

    public void onSuccess();
    public void onFailed();
    public void onPaused();
    public void onCanceld();
    public void onProgress(int progress);
}
