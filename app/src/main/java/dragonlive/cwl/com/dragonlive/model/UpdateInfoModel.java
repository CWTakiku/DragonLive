package dragonlive.cwl.com.dragonlive.model;

/**
 * Created by cwl on 2018/5/30.
 */

public class UpdateInfoModel {


    /**
     * code : 1
     * errCode :
     * errMsg :
     * data : {"version":"1.1","apkUrl":"https://dragonlive.oss-cn-shenzhen.aliyuncs.com/apk/app-release.apk","desc":"修复了闪退等BUG，优化用户体验"}
     */

    private String code;
    private String errCode;
    private String errMsg;
    private UpdateInfo data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public UpdateInfo getData() {
        return data;
    }

    public void setData(UpdateInfo data) {
        this.data = data;
    }



}
