package com.domain;

import java.util.Date;

/**
 * Created by seed on 2018/5/3.
 */
public class Header {

    protected String version;    //版本号，从配置文件中读取
    protected int type;           //类型 0：命令 1：回复
    protected String trans_id;    //传输ID（包括请求ID和回复ID）
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

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public Date getCurr_time() {
        return curr_time;
    }

    public void setCurr_time(Date curr_time) {
        this.curr_time = curr_time;
    }
}
