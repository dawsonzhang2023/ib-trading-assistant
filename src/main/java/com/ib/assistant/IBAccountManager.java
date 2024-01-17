package com.ib.assistant;

import com.ib.assistant.market.IBMarketDataPanel;
import com.ib.assistant.market.MarketDataModel;
import com.ib.assistant.market.MarketDataUtil;
import com.ib.assistant.message.AccountSummaryMessage;
import com.ib.client.Contract;
import com.ib.contracts.ETFContractFactory;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiController;

import java.util.*;

public class IBAccountManager implements ApiController.IAccountSummaryHandler {

    private final ApiController controller;

    private boolean isSubscribe = false;

    private IBMarketDataPanel dataView;
    private Map< String  , AccountSummaryMessage> accountSummaryMessages;

    private final AccountSummaryTag[] tags = new AccountSummaryTag[]{
            AccountSummaryTag.AccountType,
            AccountSummaryTag.NetLiquidation,
            AccountSummaryTag.TotalCashValue
    };

    public IBAccountManager(ApiController controller, IBMarketDataPanel dataView) {
        this.controller = controller;
        this.dataView = dataView;
        accountSummaryMessages = new HashMap<>();
    }


    public  Map<String , AccountSummaryMessage> getAccountSummary() {
        if( !isSubscribe) {
            controller.reqAccountSummary("All", tags, this);
            isSubscribe=true;
        }
        return accountSummaryMessages;
    }

    @Override
    public void accountSummary(String account, AccountSummaryTag tag, String value, String currency) {
        AccountSummaryMessage accountSummaryMessage = new AccountSummaryMessage(0, account, tag, value, currency);
        String key = account.concat("_").concat(tag.toString());
        if ( AccountSummaryTag.NetLiquidation.equals( tag )) {
            // System.out.println( "acct ::" + account + "  tag :: " + tag + " value ::" + value);
            updateAccountTQQQ(accountSummaryMessage);
        }
        // System.out.println( "accountSummary event list size ::" +  accountSummaryMessages.size());
        accountSummaryMessages.put(key,accountSummaryMessage);
    }

    private void updateAccountTQQQ(AccountSummaryMessage accountSummaryMessage) {
        MarketDataModel dataModel = dataView.getDataModel();
        String symbol = "TQQQ";
        Contract contract = ETFContractFactory.getInstance().getContractBySymbol(symbol);
        String securityCode = MarketDataUtil.convertContractToSecurityCode(contract);
        MarketDataModel.MarketDataStruct  dataRow = dataModel.findDataRowBySecurityCode(securityCode);
        if ( dataRow!= null ){
            dataRow.setNetLiquidation( Double.parseDouble( accountSummaryMessage.getAccountValue() ));
        }else {
            dataRow = new MarketDataModel.MarketDataStruct();
            dataRow.setNetLiquidation( Double.parseDouble( accountSummaryMessage.getAccountValue() ));
            dataRow.setSecurityCode(securityCode);
            dataModel.insertRow( dataRow );
        }
        dataModel.fireTableDataChanged();
    }

    @Override
    public void accountSummaryEnd() {
        //accountSummaryMessages.clear();
    }
}
