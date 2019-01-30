package com.ipaynow.dc.common.cfg.pojo;

/**
 * @Date: 2019/1/30 15:15
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.pojo
 * @ClassName: CfgException
 * @Description:
 **/
public class CfgException extends RuntimeException {
    private String code;
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CfgException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
