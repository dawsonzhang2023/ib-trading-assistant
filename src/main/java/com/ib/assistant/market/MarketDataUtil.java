package com.ib.assistant.market;

import com.ib.client.Contract;

public class MarketDataUtil {

    public static String convertContractToSecurityCode(Contract contract) {
        return  contract.symbol() + " " + contract.getSecType() + " " + contract.currency() + " @" + contract.exchange();
    }
}
