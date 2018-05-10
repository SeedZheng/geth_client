package com.domain;

import java.util.Date;
import java.util.Map;

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

	public static Returns initReturns(Params param,String ret){
    	Returns returns=new Returns();
    	returns.setCurr_time(new Date());
    	returns.setTrans_id(param.getTrans_id());
    	returns.setType(1);
    	returns.setVersion(param.getVersion());
    	returns.setRetCode(ReturnCode.SUCCESS);
    	returns.setContent(ret);
    	
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
		
		Returns returns=initReturns(param, "rets");
		System.out.println(returns.toString());
		
	}
}
