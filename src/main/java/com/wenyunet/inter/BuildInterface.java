package com.wenyunet.inter;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.utils.Convert;

public interface BuildInterface {
	
	
	public Map<String,String> newAccount(String payPass) throws Exception;
	
	/**
	 * 列举出当前所有账户
	 * @return
	 * @throws Exception
	 */
     public Map<String,String> listAccounts() throws Exception;
    
     public Map<String,String> getDevInfo() throws Exception;

     public Map<String,String> getBlockChain(String blockAdd) throws Exception;
     
     public Map<String,String> getDevStatus() throws Exception;
    
     /**
      * 自己转账给其他人
      * @param _to			接收者
      * @param amount		金额
      * @param unit			单位
      * @param payPass		支付密码
      * @return
      * @throws Exception
      */
     public Map<String,String> transfer(String _to,String amount,Convert.Unit unit,String payPass)throws Exception;
     
     /**
      * 账户之间转账
      * @param _from		来源
      * @param _to			接收者
      * @param payPass		支付密码
      * @param amount		金额
      * @param gas_price	GAS单价
      * @param gas_limit	GAS限制
      * @param unit			单位
      * @return
      * @throws Exception
      */
     public Map<String,String> transfer(String _from,String _to,String payPass,String amount,BigInteger gas_price,BigInteger gas_limit,Convert.Unit unit)throws Exception;

     /**
      * 获取GETH版本
      * @param testParam
      * @return
      * @throws Exception
      */
     public Map<String,String> getVersion()throws Exception;
     
     /**
      * 获取我的账户余额
      * @return
      * @throws Exception
      */
     public Map<String,String> getBalance(String address)throws Exception;
     
	/**
	 * 部署合约
	 * @param className		合约类名
	 * @param method_name	方法名
	 * @param payPass		支付密码
	 * @param params		部署后需执行的合约方法参数
	 * @return
	 * @throws Exception
	 */
	 public  Map<String,String> build_factory(String className, String method_name,String payPass, Object[] params) throws Exception;
	 
	 /**
	  * 合约部署
	  * @param className	合约名
	  * @param payPass		支付密码
	  * @return				合约地址
	  * @throws Exception
	  */
	 public  Map<String,String> build_factory(String className, String payPass) throws Exception;
	 
	 /**
	  * 调用合约
	  * @param className	合约类名
	  * @param contractAdd	合约地址
	  * @param method_name	方法名
	  * @param params		方法参数
	  * @return				方法调用结果
	  * @throws Exception
	  */
	 public  Map<String,String> load_factory(String className, String contractAdd,String method_name, Object[] params) throws Exception;
	 
	 /**
	     * 获取我的账户余额
	     * @return
	     * @throws Exception
	     */
	 public  Map<String,String> getBalance()throws Exception;

}
