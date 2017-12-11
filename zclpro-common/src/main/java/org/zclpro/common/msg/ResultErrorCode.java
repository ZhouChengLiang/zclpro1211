package org.zclpro.common.msg;

import lombok.Data;
@Data
public class ResultErrorCode {
    public static ResultErrorCode USER_SIGNED = new ResultErrorCode(10036,"您已经完成本日签到了！");
    public static ResultErrorCode SIGN_SWITCH_CLOSED = new ResultErrorCode(10037,"签到开关已被关闭！");

    private int resultCode;
    private String resultMsg;

    public ResultErrorCode(int resultCode, String resultMsg){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
