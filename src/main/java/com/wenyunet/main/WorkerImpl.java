package com.wenyunet.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wenyunet.domain.Params;
import com.wenyunet.domain.ReturnCode;
import com.wenyunet.domain.Returns;
import com.wenyunet.domain.TransId;
import com.wenyunet.tools.CommUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 具体逻辑实现类
 * 1.解析命令
 * 2.执行命令
 * 3.拿到结果并包装
 * 4.返回
 * Created by seed on 2018/5/9.
 */
public class WorkerImpl extends Start implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(WorkerImpl.class);
    private static AtomicInteger tName=new AtomicInteger(0);

    @Override
    public void run() {
    	Thread.currentThread().setName("Worker-Thread-"+tName.getAndIncrement());
    	for(;;){
    		try {
	   			 String message = reqQueue.take();//拿到命令
	   	    	if(!CommUtil.isEmpty(message))
	   	    		invoke(message);
	   		} catch (InterruptedException e) {
	   			e.printStackTrace();
	   		}
    	}
    }
    
    
    public void invoke(String message){
    	
    	log.info("receive message "+message);
        
        Params param= JSONObject.parseObject(message,Params.class);
        String command=param.getCommand();
        
        Map<String, String> ret=new HashMap<>();
        String json="";
        
        //拿到参数
        Object[] params=new Object[param.getParams().length];
        for(int i=0;i<param.getParams().length;i++){
        	Object obj=param.getParams()[i];
        	if(obj instanceof JSONArray){
        		JSONArray ja=(JSONArray)obj;
        		Object[] o=new Object[ja.size()];
        		for(int j=0;j<ja.size();j++){
        			o[j]=ja.get(j);
        		}
        		obj=o;
        	}
        	for(int n=0;n<units.length;n++){
        		if(units[n].name().equals(obj)){
        			obj=units[n];
        			break;
        		}
        	}
        	params[i]=obj;
        }
        Class<?>[] classs=new Class[params.length];
        for(int i=0;i<params.length;i++){
            Class<?> c=params[i].getClass();
            classs[i]=c;
        }
        try {
            Class<?> clazz=Class.forName(CommUtil.REFLECT_CLASS);
            Method method=clazz.getMethod(command,classs);
            ret=(Map)method.invoke(clazz.newInstance(),params);
            json=JSONObject.toJSONString(Returns.initReturns(param, ret,null));
        } catch (Exception e) {
            e.printStackTrace();
            json=JSONObject.toJSONString(Returns.initReturns(param,  e.getMessage(),ReturnCode.ERROR));
        }finally {
        	try {
				respQueue.put(json);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
    }
    
    
    public static void sendBalance() throws Exception{
    	//sendBalance:account|balance
    	
    	Map<String,String> map=new HashMap<>();
    	String result="";
    	
    	Class<?> clazz=Class.forName(CommUtil.REFLECT_CLASS);
    	Method method=clazz.getMethod("getBalance");
    	map=(Map) method.invoke(clazz.newInstance());
    	//result="sendBalance:"+result;
    	
    	Returns returns=new Returns("1.0", 2, TransId.SENDBALANCE, ReturnCode.SUCCESS, map);
    	
    	result=JSONObject.toJSONString(returns);
    	respQueue.put(result);
    	
    }

    public static void main(String[] args) {
        String s="123";
        System.out.println(s.getClass().getName());
    }
}
