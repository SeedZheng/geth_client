package com.inter;

import com.service.InvokeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

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


    public Generated() {
        getInstance();
        connectGeth();
    }

    public static void main(String[] args) throws IOException {

    }


    public String newAccount(String payPass) {
    	
        checkAll(payPass);
        String account="";
        try {
           NewAccountIdentifier identifier= web3.personalNewAccount(payPass).send();
           account=identifier.getAccountId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return account;
    }


    public String listAccounts() {
        return null;
    }

    public String getDevInfo() {
        return null;
    }


    public String getBlockChain() {
        return null;
    }

    public String getDevStatus() {
        return null;
    }

    public String getVersion(String testParam){
    	log.info(testParam);
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        return clientVersion;
    }

    @Override
    public  String build_factory(String className, String method_name,String payPass, Object[] params) throws Exception {

    	log.info("build_factory now");

        //checkWithoutCre();
    	checkAll(payPass);

        ContractInterface cInter = InvokeService.deploy_factory(className,web3, credentials);

        String object=cInter.otherFunction(method_name,params,cInter);
        return object;
    }
    @Override
    public  String load_factory(String className, String contractAdd,String method_name, Object[] params) throws Exception {
    	
    	log.info("load_factory now");

        checkWithoutCre();

        ContractInterface cInter = InvokeService.load_factory(className,contractAdd,web3, credentials);

        String object=cInter.otherFunction(method_name,params,cInter);
        return object;
    }



    private void loadCred(String payPass){
        try {
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
            System.out.println(file.exists());
            credentials= WalletUtils.loadCredentials(payPass,file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    private  void connectGeth(){
        if(config==null)
            getInstance();
        String rpcPort=config.getProperty("port");
        String addr="http://127.0.0.1:"+rpcPort;
        if(web3==null)
            web3= Admin.build(new HttpService(addr));
        log.info("connect geth success");
    }

    public void getInstance(){
        InputStream ins=Generated.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            config.load(ins);
            log.info("loading properties success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Generated(String payPass) {
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

    private void checkAll(String payPass){
        if(config==null)
            getInstance();
        if(web3==null)
            connectGeth();
        if(credentials==null)
            loadCred(payPass);
    }

    private void checkWithoutCre(){
        if(config==null)
            getInstance();
        if(web3==null)
            connectGeth();
    }

}
