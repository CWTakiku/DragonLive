package com.mysdk.okhttp;


import com.mysdk.okhttp.https.HttpsUtils;
import com.mysdk.okhttp.listener.DisposeDataHandle;
import com.mysdk.okhttp.response.CommonFileCallback;
import com.mysdk.okhttp.response.CommonJsonCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cwl on 2017/5/6.
 */

public class CommonOkHttpClient {
    private static final int TIME_OUT=30;
    private static OkHttpClient mOkHttpClient;

    static {
       OkHttpClient.Builder okHttpClientBuilder=new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        /**
         *  为所有请求添加请求头，看个人需求
         */
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", "Imooc-Mobile") // 标明发送本次请求的客户端
                        .build();
                return chain.proceed(request);
            }
        });
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true);

        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    public  static  OkHttpClient getmOkHttpClient(){
        return  mOkHttpClient;
    }

    public static Call get(Request request, DisposeDataHandle handle){

        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return  call;
    }
    public static  Call post(Request request, DisposeDataHandle handle){
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return  call;
    }
    public static  Call downloadFile(Request request, DisposeDataHandle handle){
        OkHttpClient client=new OkHttpClient();
        Call call=client.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return  call;
    }

}
