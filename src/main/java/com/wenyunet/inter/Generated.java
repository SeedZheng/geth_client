package com.wenyunet.inter;

import com.wenyunet.service.InvokeService;
import com.wenyunet.tools.CommUtil;

import rx.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.utils.Convert.Unit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by seed on 2018/5/3.
 */
public class Generated  implements  BuildInterface{

    //类加载需要做的事情
    //1.读取配置文件信息
    //2.连接geth客户端
    //3.读取凭证
    public static  Properties config=new Properties();
    private static  Credentials credentials=null;
    private static  Admin web3=null;
    private static  Generated gene=null;
    private static final Logger log = LoggerFactory.getLogger(Generated.class);
    private static final BigInteger GAS_PRICE=BigInteger.valueOf(22_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);


    public Generated() throws Exception{
        getInstance();
        connectGeth();
    }
    

    private void loadCred(String payPass) throws Exception{
       
            if(config==null)
                getInstance();
            if(web3==null)
                connectGeth();
            String os=System.getProperty("os.name");
            String path="";
            if("linux".equalsIgnoreCase(os))
                path=config.getProperty("fp_linux");
            else
                path=config.getProperty("fp_windows");
            File file=new File(path);
            if(CommUtil.isNull(credentials)){
            	credentials= WalletUtils.loadCredentials(payPass,file);
            	log.info("credentials loading success");
            }
    }

    private  void connectGeth() throws Exception{
        if(config==null)
            getInstance();
        String rpcPort=config.getProperty("port");
        String addr="http://127.0.0.1:"+rpcPort;
        if(web3==null){
            web3= Admin.build(new HttpService(addr));
            if(!CommUtil.isNull(web3))
            	log.info("connect geth success");
            else
            	log.info("connect geth unsuccess");
        }
    }

    public void getInstance() throws Exception{
        	if(CommUtil.isNull(config) || config.size()==0){
        		InputStream ins=Generated.class.getClassLoader().getResourceAsStream("config.properties");
                config.load(ins);
                log.info("loading properties success");
        	}
    }

    public Generated(String payPass) throws Exception{
        if(gene==null){
            synchronized (Generated.class){
                if(gene==null){
                    gene=new Generated();
                    getInstance();
                    connectGeth();
                    loadCred(payPass);
                }
            }
        }
    }

    private void checkAll(String payPass) throws Exception{
        if(config==null)
            getInstance();
        if(web3==null)
            connectGeth();
        if(credentials==null)
            loadCred(payPass);
    }

    private void checkWithoutCre() throws Exception{
        if(config==null)
            getInstance();
        if(web3==null)
            connectGeth();
    }
    
    public String getBlockNumber() throws Exception{
    	EthBlockNumber number = web3.ethBlockNumber().send();
    	return number.getBlockNumber().toString();
    }
    
   
    public String getBLockChainNews()throws Exception{
    	 //latestBlock:$|time:$
    	String latestBlock=getBlockNumber();
    	EthBlock block = web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf("latest"), true).send();
    	String parentHash=block.getResult().getParentHash();
    	EthBlock parent=web3.ethGetBlockByHash(parentHash, true).send();
    	BigInteger t1=parent.getResult().getTimestamp();
    	BigInteger t2=block.getResult().getTimestamp();
    	return "latestBlock:"+latestBlock+"|time:"+(t2.intValue()-t1.intValue());
    }


    public static void main(String[] args) throws Exception {
    	new Generated().getBLockChainNews();
    	
    }

    @Override
    public Map<String,String> newAccount(String payPass) throws Exception{
    	//walletId:$|chainId:$|account:$
    	Map<String,String> map=new HashMap<>();
        //checkAll(payPass);
        String account="";
        try {
           NewAccountIdentifier identifier= web3.personalNewAccount(payPass).send();
           account=identifier.getAccountId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        map.put("account", account);
        
        return map;
    }

    @Override
    public Map<String,String> listAccounts() throws Exception{
    	//walletId:$
    	
    	Map<String,String> map=new HashMap<>();
    	StringBuilder sb=new StringBuilder();
    	
    	EthAccounts accounts=web3.ethAccounts().send();
        Iterator<String> iterator=accounts.getAccounts().iterator();
        while(iterator.hasNext()){
        	sb.append(iterator.next());
        }
        
        map.put("account", sb.toString());
        
        return map;
    }
    
    @Override
    public Map<String,String> getDevInfo() throws Exception{
        return null;
    }
    @Override
    public Map<String,String> getBlockChain(String blockAdd) throws Exception{
        return null;
    }
    @Override
    public Map<String,String> getDevStatus() throws Exception{
        return null;
    }
    @Override
    public Map<String,String> transfer(String _to,String amount,Convert.Unit unit,String payPass,String address)throws Exception{
    	//transferId:$|blockHash:$|blockNumber:$
    	Map<String,String> map=new HashMap<>();
    	checkAll(payPass);
    	
    	TransactionReceipt transactionReceipt=Transfer.sendFunds(web3, credentials, _to, new BigDecimal(amount),unit).send();
    	String blockHash=transactionReceipt.getBlockHash();
    	BigInteger blockNumber = transactionReceipt.getBlockNumber();
    	
    	map.put("blockHash", blockHash);
    	map.put("blockNumber", blockNumber.longValue()+"");
    	
    	return map;
    }
    @Override
    public Map<String,String> getVersion()throws Exception{
    	//log.info(testParam);
    	Map<String,String> map=new HashMap<>();
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        
        map.put("clientVersion", clientVersion);
        
        return map;
    }

    @Override
    public  Map<String,String> build_factory(String className, String method_name,String payPass, Object[] params) throws Exception {

    	log.info("build_factory now");
    	Map<String,String> map=new HashMap<>();

        //checkWithoutCre();
    	checkAll(payPass);

        ContractInterface cInter = InvokeService.deploy_factory(className,web3, credentials);

        String object=cInter.otherFunction(method_name,params,cInter);
        
        map.put("result", object);
        
        return map;
    }
    @Override
    public  Map<String,String> load_factory(String className, String contractAdd,String method_name, Object[] params) throws Exception {
    	
    	log.info("load_factory now");
    	Map<String,String> map=new HashMap<>();

        checkWithoutCre();

        ContractInterface cInter = InvokeService.load_factory(className,contractAdd,web3, credentials);

        String object=cInter.otherFunction(method_name,params,cInter);
        map.put("result", object);
        
        return map;
    }

    //实时获取节点信息
    private static void getEvent(){
    	//阻塞事件
        Subscription subscription=web3.blockObservable(true).subscribe(block->{
            log.info(block.getBlock().getNumber()+": "+block.getBlock().getMiner());
        });

    }

	@Override
	public Map<String,String> getBalance(String address) throws Exception {
		
		Map<String,String> map=new HashMap<>();
		 EthGetBalance balance=web3.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
	        BigInteger ba=balance.getBalance();
	        BigDecimal b=Convert.fromWei(new BigDecimal(ba), Convert.Unit.ETHER);
	        
	        map.put("balance", b.toString());
	        
		return map;
	}
	
	public  Map<String,String> getBalance()throws Exception{
		
		Map<String,String> map=new HashMap<>();
		EthAccounts accounts=web3.ethAccounts().send();
		String defaultAccount=accounts.getAccounts().get(0);
		
		map.put("account", defaultAccount);
		map.put("balance", getBalance(defaultAccount).get("balance"));
		
		
		return map;
	}


	@Override
	public Map<String,String> transfer(String _from, String _to, String payPass, String amount, BigInteger gas_price,
			BigInteger gas_limit, Unit unit,String address) throws Exception {
		
		checkAll(payPass);
		Map<String,String> map=new HashMap<>();
		
		if(CommUtil.isNull(gas_price))
			gas_limit=GAS_LIMIT;
		if(CommUtil.isNull(gas_price))
			gas_price=GAS_PRICE;
		
		//获取nonce
		EthGetTransactionCount count=web3.ethGetTransactionCount(_from, DefaultBlockParameterName.LATEST).send();
		BigInteger nonce=count.getTransactionCount();
		
		//创建交易
		BigInteger val=Convert.toWei(new BigDecimal(amount), Convert.Unit.ETHER).toBigInteger();
		RawTransaction rawTransaction=RawTransaction.createEtherTransaction(nonce, gas_price, gas_limit, _to, val);
		
		//验证签名
		byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue=Numeric.toHexString(signMessage);
		
		//发送交易
		EthSendTransaction transaction=web3.ethSendRawTransaction(hexValue).send();
		String transactionHash=transaction.getTransactionHash();
		
		map.put("transactionHash", transactionHash);
		
		return map;
	}


	@Override
	public Map<String,String> build_factory(String className, String payPass) throws Exception {
		log.info("build_factory now");

        //checkWithoutCre();
    	checkAll(payPass);
    	Map<String,String> map=new HashMap<>();

        ContractInterface cInter = InvokeService.deploy_factory(className,web3, credentials);

        String object=cInter.getContractAddress(cInter);
        
        map.put("result", object);
        
        return map;
	}

}
