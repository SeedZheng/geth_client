package com.wenyunet.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by seed on 2018/5/3.
 */
public class Params extends Header {


    private String command;  //命令
    private String data;    //携带参数(需要客户端返回)
    private Object[] params;  //参数

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
    

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

    
}
