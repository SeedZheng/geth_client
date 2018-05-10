

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

/**
 * Created by ls263 on 2018/4/25.
 */
public class Test2 {

    public static void main(String[] args) throws Exception {

        String ip="127.0.0.1";
        String port="6394";

        if(args.length>0){
            ip=args[0];
            port=args[1];
        }
        Admin web3=Admin.build(new HttpService("http://"+ip+":"+port)) ;
        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        System.out.println(clientVersion);
    }


}
