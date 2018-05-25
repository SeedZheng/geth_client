package com.wenyunet.domain;

/**
 * Created by seed on 2018/5/25.
 */
public enum TransId {
	SENDBALANCE(1,"sendBalance"),   //客户端主动向服务端发送余额变动提醒
    GETBALANCE(0,"getBalance"),     //服务端向客户端请求余额
    TRANSFER(0,"transfer"),         //服务端向客户端请求转账
    LISTACCOUNTS(0,"listAccounts"), //服务端向客户端请求获取账户列表
    GETVERSION(0,"getVersion"),     //服务端向客户端请求获取客户端版本
    BUILDFACTORY(0,"build_factory"),//部署合约
    LOADFACTORY(0,"load_factory");  //调用合约


    private int code;       //命令类型 0:一请求一回复 1:客户端自动发送的命令
    private String method;

    TransId(int code, String method) {
        this.code = code;
        this.method = method;
    }



}
