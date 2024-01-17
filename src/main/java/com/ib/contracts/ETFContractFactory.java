package com.ib.contracts;

import com.ib.client.Contract;

public class ETFContractFactory {

    public  static  ETFContractFactory   etfContractFactory ;

    public  static  ETFContractFactory getInstance(){
        if ( etfContractFactory == null ){
            etfContractFactory = new ETFContractFactory();
        }
        return  etfContractFactory;
    }

    public Contract getContractBySymbol( String symbol) {
        switch (symbol){
            case "TQQQ":
                Contract  contract = new Contract() ;
                contract.symbol( symbol );
                contract.secType("STK");
                contract.exchange( "SMART");
                contract.currency( "USD");
                return  contract;
            default:
                Contract  contractDefault = new Contract() ;
                contractDefault.symbol( symbol );
                contractDefault.secType("STK");
                contractDefault.exchange( "SMART");
                contractDefault.currency( "USD");
                return contractDefault;
        }
    }
}
