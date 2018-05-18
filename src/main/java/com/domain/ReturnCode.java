package com.domain;

/**
 * Created by seed on 2018/5/3.
 */
public enum ReturnCode {

    CONNECT_FAIL(-1,"连接客户端失败"),
    SUCCESS(0,"成功"),
    WAITING(5,"等待确认"),
    BALANCE_INSUFF(10,"余额不足"),
	ERROR(-2,"失败");


	private  int code;
	private String msg;
	
    ReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
 
}
