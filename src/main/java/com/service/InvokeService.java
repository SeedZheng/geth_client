package com.service;

import com.inter.ContractInterface;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by seed on 2018/5/7.
 */
public class InvokeService {
	
	private static final Logger log=LoggerFactory.getLogger(InvokeService.class);


    public static ContractInterface deploy_factory(String className,Web3j web3,Credentials credentials) throws Exception {

    	log.info("deploy_factory now");

        Class<?>  mainClass = Class.forName(className);
        Method deployMethod = mainClass.getMethod("deploy", new Class[]{Web3j.class,Credentials.class,BigInteger.class,BigInteger.class});
        RemoteCall c = (RemoteCall)deployMethod.invoke(null,new Object[]{web3, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT});
        return (ContractInterface)c.send();
    }

    public static ContractInterface load_factory(String className, String contractAdd,Web3j web3,Credentials credentials) throws Exception {

        System.out.println("load_factory now");

        Class<?> mainClass = Class.forName(className);
        Method deployMethod = mainClass.getMethod("load", new Class[]{String.class,Web3j.class,Credentials.class,BigInteger.class,BigInteger.class});
        return (ContractInterface)deployMethod.invoke(null,new Object[]{contractAdd,web3, credentials,ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT});
        //return (ContractInterface)c.send();
    }


}
