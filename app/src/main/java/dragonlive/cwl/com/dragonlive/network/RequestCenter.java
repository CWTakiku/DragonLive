package dragonlive.cwl.com.dragonlive.network;


import com.mysdk.okhttp.CommonOkHttpClient;
import com.mysdk.okhttp.listener.DisposeDataHandle;
import com.mysdk.okhttp.listener.DisposeDataListener;
import com.mysdk.okhttp.request.CommonRequest;
import com.mysdk.okhttp.request.RequestParams;



/**
 * Created by cwl on 2017/5/8.
 */

public class RequestCenter {

    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }


    /**
     * 应用版本号请求
     *
     * @param listener
     */
//    public static void checkVersion(DisposeDataListener listener) {
//        RequestCenter.postRequest(AppNetConfig.UPDATE, null, listener, UpdateInfo.class);
//    }

//    public static void requestHomeData(DisposeDataListener listener) {
//        RequestCenter.postRequest(AppNetConfig.INDEX, null, listener, Index.class);
//    }
//    public static void requestProductData(DisposeDataListener listener) {
//        RequestCenter.postRequest(AppNetConfig.PRODUCT, null, listener, Product.class);
//    }
//    public static void requestLogin(RequestParams params, DisposeDataListener listener){
//        CommonOkHttpClient.post(CommonRequest.createPostRequest(AppNetConfig.LOGIN,params),new DisposeDataHandle(listener, User.class));
//    }
//    public static void requestfankui(RequestParams params, DisposeDataListener listener){
//        CommonOkHttpClient.post(CommonRequest.createPostRequest(AppNetConfig.FEEDBACK,params),new DisposeDataHandle(listener, Fankui.class));
//    }
//    public static void requestUserRegister(RequestParams params, DisposeDataListener listener){
//        CommonOkHttpClient.post(CommonRequest.createPostRequest(AppNetConfig.REGISTER,params),new DisposeDataHandle(listener, IsExist.class));
//    }

    /**
     * 请求下载
     * @param listener
     * @param url APK下载地址
     */

//    public static void requestDownload(DisposeDownloadListener listener, String url) {
//        CommonOkHttpClient.downloadFile(CommonRequest.
//                        createDownloadRequest(url),
//                new DisposeDataHandle(listener, Environment.getExternalStorageDirectory() + "/P2P金融/P2P金融.apk"));
//    }


    /**
     * 请求登录
     * @param name
     * @param pwd
     * @param listener
     */
//    public static void requestLogin(String name,String pwd,DisposeDataListener listener){
//        RequestParams params=new RequestParams();
//        params.put("mb",name);
//        params.put("pwd",pwd);
//        RequestCenter.postRequest(HttpConstants.LOGIN,params,listener,User.class);
//    }

    /**
     * 请求课程详情
     */
//  public static void requestCourseDetail(String courseID,DisposeDataListener listener){
//      RequestParams params=new RequestParams();
//      params.put("courseId",courseID);
//      RequestCenter.postRequest(HttpConstants.COURSE_DETAIL,params,listener, BaseCourseModel.class);
//  }
}
