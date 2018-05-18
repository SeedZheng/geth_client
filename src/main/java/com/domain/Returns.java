package com.domain;

import java.util.Date;
import java.util.Map;

import com.tools.CommUtil;

/**
 * Created by seed on 2018/5/3.
 */
public class Returns extends  Header{
	
	
	
    private ReturnCode retCode; //返回值
    private String content;	//返回内容
    
    
    public ReturnCode getRetCode() {
		return retCode;
	}

	public void setRetCode(ReturnCode retCode) {
		this.retCode = retCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Returns() {}
	

	public Returns(String version, int type, String trans_id,  ReturnCode retCode, String content) {
		super(version, type, trans_id, new Date());
		this.retCode = retCode;
		this.content = content;
	}
	

	public static Returns initReturns(Params param,String ret,ReturnCode code){
    	Returns returns=new Returns();
    	returns.setCurr_time(new Date());
    	returns.setTrans_id(param.getTrans_id());
    	returns.setType(1);
    	returns.setVersion(param.getVersion());
    	returns.setContent(ret);
    	returns.setRetCode(null==code?ReturnCode.SUCCESS:code);
    	
    	return returns;
    }
	

	@Override
	public String toString() {
		return "Returns [retCode=" + retCode + ", content=" + content + ", version=" + version + ", type=" + type
				+ ", trans_id=" + trans_id + ", curr_time=" + curr_time + "]";
	}

	public static void main(String[] args) {
		Params param=new Params();
		param.setCommand("command");
		param.setCurr_time(new Date());
		param.setParams(new Object[]{"123"});
		param.setTrans_id("123456");
		param.setType(1);
		param.setVersion("1.0");
		
		System.out.println(param.toString());
		
		Returns returns=initReturns(param, "rets",null);
		System.out.println(returns.toString());
		
	}
}
