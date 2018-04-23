package com.mysdk.okhttp.listener;

/**
 * Created by cwl on 2017/5/6.
 */

public class DisposeDataHandle {

    public DisposeDataListener mListener=null;
    public Class<?> mClass;
    public String mSource=null;
    public DisposeDownloadListener downloadListener=null;

    public DisposeDataHandle(DisposeDataListener disposeDatalistener){
        this.mListener=disposeDatalistener;
    }

    public DisposeDataHandle(DisposeDataListener disposeDatalistener,Class<?> clazz){
        this.mListener=disposeDatalistener;
        this.mClass=clazz;
    }

    public DisposeDataHandle(DisposeDataListener disposeDatalistener,String source){
        this.mListener=disposeDatalistener;
       this.mSource=source;
    }
    public DisposeDataHandle(DisposeDownloadListener listener,String source){
        this.downloadListener=listener;
        this.mSource=source;
    }


}
