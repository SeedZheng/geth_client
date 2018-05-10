

import com.contract.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ls263 on 2018/4/28.
 */
public class TestCoin {

    private static final Logger log = LoggerFactory.getLogger(Test.class);
    private static Admin web3=null;
    private static final String walletPath="D:\\Program Files\\Geth\\data1\\keystore\\UTC--2018-04-28T03-32-25.198733800Z--6714eb1c2911bc5f6bf36bdee01979592617aa19";


    public TestCoin(){
        web3=Admin.build(new HttpService("http://127.0.0.1:6394")) ;
    }


    public static void main(String[] args) throws Exception {
       //new TestCoin().callContract("0x1E320249527bCCd697Ea6d140ad05121760e7527");
      // new TestCoin().balance("123","0x6714eb1c2911bc5f6bf36bdee01979592617aa19");

    }


    private void getVersion() throws IOException {


        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        log.info(clientVersion);

    }
    /*private void buildContract2() throws Exception {

        String account=web3.ethAccounts().send().getAccounts().get(0);
        log.info("当前账户是："+account);

        Credentials credentials= WalletUtils.loadCredentials("123",walletPath);
        Map<String,Object> map=BuildCoin.deployCont(web3,credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
        Set<String> keySet=map.keySet();
        Iterator<String> ite=keySet.iterator();
        while(ite.hasNext()){
            String str=ite.next();
            Object obj=map.get(str);
            System.out.println(str+" : "+obj);
        }

    }*/


    private void buildContract() throws Exception {

        String account=web3.ethAccounts().send().getAccounts().get(0);
        log.info("当前账户是："+account);

        Credentials credentials= WalletUtils.loadCredentials("123",walletPath);
        //具体功能接口化 返回类型和参数类型都是接口 如  List getAll(List<String> list);
        Coin coin=Coin.deploy(web3,credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();

        log.info("合约地址是："+coin.getContractAddress());

        String result=coin.minter().send();
        log.info("调用合约构造函数："+result);


        BigInteger min_bal=coin.balances(account).send();
        log.info("当前账户的余额是:"+min_bal.intValue());

        TransactionReceipt transactionReceipt=coin.mint(account,new BigInteger("1000")).send();
        log.info("向账户 "+account+" 增加1000代币,交易HASH："+transactionReceipt.getTransactionHash());

        min_bal=coin.balances(account).send();
        log.info("当前账户的余额是:"+min_bal.intValue());

    }

    private void callContract(String toAddress) throws Exception {
        String contractAdd="0xe734c6572fb4732dab0007f9ce971bf995ab3332";

        String account=web3.ethAccounts().send().getAccounts().get(0);
        log.info("当前账户是："+account);

        Credentials credentials= WalletUtils.loadCredentials("123",walletPath);

        Coin coin=Coin.load(contractAdd,web3,credentials,ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
        log.info("合约地址是："+coin.getContractAddress());

        BigInteger min_bal=coin.balances(account).send();
        log.info("当前账户的余额是:"+min_bal.intValue());

        log.info("测试增加余额1000");

        TransactionReceipt transactionReceipt=coin.mint(account,new BigInteger("1000")).send();
        log.info("向账户 "+account+" 增加1000代币,交易HASH："+transactionReceipt.getTransactionHash());

        log.info("测试转账到账户 "+toAddress+"  1000代币");
        min_bal=coin.balances(toAddress).send();
        log.info("账户"+toAddress+"的余额是:"+min_bal.intValue());

        transactionReceipt= coin.send(toAddress,new BigInteger("1000")).send();
        log.info("向账户 "+toAddress+" 转账1000代币,交易HASH："+transactionReceipt.getTransactionHash());

        min_bal=coin.balances(toAddress).send();
        log.info("账户 "+toAddress+" 的余额是:"+min_bal.intValue());

    }

    private void balance(String password,String account) throws Exception {

        String contractAdd="0xe734c6572fb4732dab0007f9ce971bf995ab3332";

        Credentials credentials= WalletUtils.loadCredentials(password,walletPath);

        Coin coin=Coin.load(contractAdd,web3,credentials,ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
        log.info("合约地址是："+coin.getContractAddress());

        BigInteger min_bal=coin.balances(account).send();
        log.info("当前账户的余额是:"+min_bal.intValue());

    }





}
