package com.wenyunet.domain;

import java.util.Date;

/**
 * Created by seed on 2018/5/3.
 */
public class Header {

    protected String version;    //版本号，从配置文件中读取
    protected int type;           //类型 0：命令 1：回复 2:主动发送的数据 如余额
    protected TransId trans_id;    //传输ID(服务端处理回复时使用)
    protected Date curr_time;     //时间戳

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    

    public TransId getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(TransId trans_id) {
		this.trans_id = trans_id;
	}

	public Date getCurr_time() {
        return curr_time;
    }

    public void setCurr_time(Date curr_time) {
        this.curr_time = curr_time;
    }

	public Header() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Header(String version, int type, TransId trans_id, Date curr_time) {
		this.version = version;
		this.type = type;
		this.trans_id = trans_id;
		this.curr_time = curr_time;
	}
    
}
