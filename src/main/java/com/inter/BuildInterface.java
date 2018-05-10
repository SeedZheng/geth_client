package com.inter;

public interface BuildInterface {
	
	 public  String build_factory(String className, String method_name,String payPass, Object[] params) throws Exception;
	 
	 public  String load_factory(String className, String contractAdd,String method_name, Object[] params) throws Exception;

}
