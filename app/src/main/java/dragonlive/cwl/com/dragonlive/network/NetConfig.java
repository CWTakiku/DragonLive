package dragonlive.cwl.com.dragonlive.network;

/**
 * Created by cwl on 2018/5/3.
 */

public class NetConfig {
    //主机地址
    public static final    String HOST="47.106.155.189";
    //调试地址
    public  static final   String LOCAL_SERVER="http://172.19.219.6:8080/ImoocBearLive";//暂时放在本地
    //web应用地址
    public  static final   String BASE_URL="http://"+HOST+"/ImoocBearLive";//正式服务器
    //获取临时钥匙地址
    public  static final   String STS_SERVER="http://47.106.155.189:7085";  //获取临时钥匙 用于上传到OSS
    //OSS华南地域的固定地址
    public  static final   String ALiEndpoint= "http://oss-cn-shenzhen.aliyuncs.com";
    //有关直播房间的地址
    public  static final   String ROOM=BASE_URL+"/roomServlet";
    //版本检查,是否是最新
    public static final String CHECK_VERSION=BASE_URL+"/versionServlet";



}
