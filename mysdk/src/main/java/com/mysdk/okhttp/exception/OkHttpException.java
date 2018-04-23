package com.mysdk.okhttp.exception;

/**
 * Created by cwl on 2017/5/6.
 */

public class OkHttpException extends Exception {

    private static final long serialVersionUID = 1L;

    private int ecode;

    private Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }
}
