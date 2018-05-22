package com.inter;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by wwenbo on 2018/5/7.
 */
public interface ContractInterface {

    public String otherFunction(String functionName, Object[] params,ContractInterface obj) throws Exception;
    
    public String getContractAddress(ContractInterface obj);


}
