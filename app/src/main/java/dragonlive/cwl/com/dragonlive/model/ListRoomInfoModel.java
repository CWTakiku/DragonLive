package dragonlive.cwl.com.dragonlive.model;

import java.util.List;

/**
 * Created by cwl on 2018/5/5.
 * 返回的是LIst 房间 包含code
 */

public class ListRoomInfoModel {


    /**
     * code : 1
     * errCode :
     * errMsg :
     * data : [{"roomId":31,"userId":"a916379012","userName":"Takiku","userAvatar":"http://dragonlive.oss-cn-shenzhen.aliyuncs.com/a916379012_1525582118579_avatar","liveCover":"http://dragonlive.oss-cn-shenzhen.aliyuncs.com/a916379012_1525589051608_cover","liveTitle":"1","watcherNums":0},{"roomId":35,"userId":"","userName":"","userAvatar":"","liveCover":"","liveTitle":"","watcherNums":0},{"roomId":37,"userId":"a12345678","userName":"?takitu?","userAvatar":"","liveCover":"http://dragonlive.oss-cn-shenzhen.aliyuncs.com/a12345678_1525590117102_cover","liveTitle":"22","watcherNums":0}]
     */

    private String code;
    private String errCode;
    private String errMsg;
    private List<RoomInfo> data;

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

    public List<RoomInfo> getData() {
        return data;
    }

    public void setData(List<RoomInfo> data) {
        this.data = data;
    }



}
