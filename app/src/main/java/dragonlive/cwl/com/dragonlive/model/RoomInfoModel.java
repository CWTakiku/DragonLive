package dragonlive.cwl.com.dragonlive.model;

/**
 * Created by Administrator.
 */

public class RoomInfoModel {

    /**
     * code : 1
     * errCode :
     * errMsg :
     * data : {"roomId":37,"userId":"a12345678","userName":"是takitu啊","userAvatar":"","liveCover":"http://dragonlive.oss-cn-shenzhen.aliyuncs.com/a12345678_1525590117102_cover","liveTitle":"22","watcherNums":0}
     */

    private String code;
    private String errCode;
    private String errMsg;
    private RoomInfo data;

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

    public RoomInfo getData() {
        return data;
    }

    public void setData(RoomInfo data) {
        this.data = data;
    }


}
