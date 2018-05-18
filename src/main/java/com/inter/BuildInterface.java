package com.inter;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.Web3ClientVersion;

public interface BuildInterface {
	
     public String listAccounts() throws Exception;
    
     public String getDevInfo() throws Exception;

     public String getBlockChain(String blockAdd) throws Exception;
 
     public String getDevStatus() throws Exception;
    
     public String transfer(String _from ,String _to,BigInteger amount)throws Exception;

     public String getVersion(String testParam)throws Exception;
     
     /**
      * 获取我的账户余额
      * @return
      * @throws Exception
      */
     public String getBalance(String address)throws Exception;
     
	
	 public  String build_factory(String className, String method_name,String payPass, Object[] params) throws Exception;
	 
	 public  String load_factory(String className, String contractAdd,String method_name, Object[] params) throws Exception;

}
