package com.wenyunet.domain;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wenyunet.tools.CommUtil;

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
	

	public Returns(String version, int type, TransId trans_id,  ReturnCode retCode, Object content) {
		super(version, type, trans_id, new Date());
		this.retCode = retCode;
		this.content = JSONObject.toJSONString(content);
	}
	

	public static Returns initReturns(Params param,Object ret,ReturnCode code){
    	Returns returns=new Returns();
    	returns.setCurr_time(new Date());
    	returns.setTrans_id(param.getTrans_id());
    	returns.setType(param.getType());
    	returns.setVersion(param.getVersion());
    	returns.setRetCode(null==code?ReturnCode.SUCCESS:code);
    	
    	if(ret instanceof Map){
    		Map<String,String> map=(Map)ret;
    		Map<String,String> data=JSONObject.parseObject(param.getData(),Map.class);
    		map.putAll(data);
        	String json=JSON.toJSONString(map);
        	returns.setContent(json);
    	}
    	if(ret instanceof String){
    		returns.setContent((String)ret);
    	}
    	
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
		//param.setTrans_id("123456");
		param.setType(1);
		param.setVersion("1.0");
		
		System.out.println(param.toString());
		
		//Returns returns=initReturns(param, "rets",null);
		//System.out.println(returns.toString());
		
	}
}
