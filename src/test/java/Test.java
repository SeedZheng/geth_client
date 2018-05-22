

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import com.wenyunet.contract.Greeter;
import com.wenyunet.contract.HelloWorld;

import rx.Subscription;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by ls263 on 2018/4/20.
 */
public class Test {

    private static final Logger log = LoggerFactory.getLogger(Test.class);
    private static Admin web3=null;
    private static final String walletPath="D:\\Program Files\\Geth\\data\\00\\keystore\\UTC--2018-04-09T03-31-27.536376500Z--ca9304dc781a89e0804147be30a9929d491cf698";

  //24682469
    public Test(){
        web3=Admin.build(new HttpService("http://127.0.0.1:6394")) ;
    }



    public static void main(String[] args) throws Exception {
        Test t=new Test();
        //t.run();
        //t.getEvent();
        //t.transEther();
        //t.generateNewWalletFile();
        t.BuildContract();
        //t.BuildContract2();
    }

    private void run() throws IOException {

      //Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:6394"));  // defaults to http://localhost:8545/
       // Web3j web3=Web3j.build(new WindowsIpcService("\\\\.\\pipe\\geth.ipc"));

       Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
       String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        log.info(clientVersion);
        //String result=web3.personalNewAccount("123").send().getResult();
        //log.info(result);

    }

    //实时获取节点信息
    private void getEvent(){

        Subscription subscription=web3.blockObservable(false).subscribe();
        log.info(subscription.toString());

    }

    //测试转账
    private  void transEther() throws Exception {
       List<String> accs= web3.personalListAccounts().send().getResult();
        for (String acc:accs) {
            System.out.println("acc:"+acc);
        }
        String toAddress="0x51501c7D4ac2530Ec1405C5678B60dfD6F5A6dA5";
        //获取转账凭证
        Credentials credentials= WalletUtils.loadCredentials("789",walletPath);
        //这里会在geth客户端中提交一个transaction，然后等待挖矿才能输出TransactionHash
        //同步方法
        //TransactionReceipt transactionReceipt=
        //        Transfer.sendFunds(web3,credentials,toAddress,new BigDecimal(100), Convert.Unit.ETHER).send();
        //System.out.println(transactionReceipt.getTransactionHash());

        //异步方法
        CompletableFuture future=
                Transfer.sendFunds(web3,credentials,toAddress,new BigDecimal(100), Convert.Unit.ETHER).sendAsync();
        boolean isDone=false;
        while(!isDone){
            if(future.isDone()){
                isDone=true;
                System.out.println("已完成");
            }else {
                Thread.sleep(2000);
                System.out.println("未完成");
            }

        }
    }


    //生成新的钱包文件
    private void generateNewWalletFile() throws Exception{
        String destination="d:/";
        File file=new File(destination);
        String fileName=WalletUtils.generateNewWalletFile("789",file,true);
        System.out.println(fileName);
    }

    //部署合约测试
    private void BuildContract() throws Exception {
        //1.get credentials
        Credentials credentials=WalletUtils.loadCredentials("123",walletPath);
       // Credentials credentials=Credentials.create("d371c208d307076027134f17edab894f5676036012a82e5c01952f0e713b29c6");
        //String account=web3.ethAccounts().send().getAccounts().get(0);
        //Credentials credentials=Credentials.create(account);
        //ObjectMapper objectMapper = new ObjectMapper();
        //WalletFile walletFile=objectMapper.readValue(new File(walletPath),WalletFile.class);
       // Credentials credentials=Credentials.create(Wallet.decrypt("789",walletFile));
        ECKeyPair keyPair=credentials.getEcKeyPair();
        BigInteger privateKey=keyPair.getPrivateKey();
        BigInteger publicKey=keyPair.getPublicKey();

        System.out.println(privateKey);
        System.out.println("---");
        System.out.println(publicKey);


        //2.build contract
        HelloWorld contract=HelloWorld.deploy(web3,credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,"ni hao").send();
        //3.get contract address
        String contractAddress=contract.getContractAddress();
        System.out.println("contract address is ："+contractAddress);

        System.out.println("contract is valid："+contract.isValid());

        String result=contract.greet().send();
        System.out.println("result ："+result);
        //5.call the contract
        TransactionReceipt transactionReceipt=contract.setGreeting("hello1").send();
        System.out.println("set："+transactionReceipt.getTransactionHash());
        System.out.println("contract is valid："+contract.isValid());

        result=contract.greet().send();
        System.out.println("result ："+result);
        //String ret=contract.greet().send();
        //System.out.println("合约调用结果为："+ret);
    }

    private void BuildContract2() throws Exception {
        Credentials credentials=WalletUtils.loadCredentials("123",walletPath);

        Greeter contract = Greeter.deploy(web3, credentials,ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                "Hello blockchain world!").send();
        String contractAddress = contract.getContractAddress();
        System.out.println(contractAddress);

        System.out.println(contract.greet().send());
    }



}
