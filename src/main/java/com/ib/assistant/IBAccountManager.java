package com.ib.assistant;

import com.ib.assistant.market.IBMarketDataPanel;
import com.ib.assistant.market.MarketDataModel;
import com.ib.assistant.market.MarketDataUtil;
import com.ib.assistant.message.AccountSummaryMessage;
import com.ib.client.Contract;
import com.ib.contracts.ETFContractFactory;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiController;
import java.util.concurrent.CompletableFuture;

import java.util.*;
import java.util.function.Consumer;

public class IBAccountManager implements ApiController.IAccountSummaryHandler {

    private final ApiController controller;

    private final Map<String, AccountSummaryMessage> accountSummaryMessages;
    private CompletableFuture<Map<String, AccountSummaryMessage>> summaryFuture;

    private IBMarketDataPanel dataView;

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

    public void getAccountSummaryAsync(Consumer<Map<String, AccountSummaryMessage>> callback) {
        // 如果当前没有正在进行的请求，开始一个新的请求
        if (summaryFuture == null || summaryFuture.isDone()) {
            // 取消之前的请求（如果有）
            controller.cancelAccountSummary(this);

            summaryFuture = new CompletableFuture<>();
            accountSummaryMessages.clear(); // 清除之前的结果
            controller.reqAccountSummary("All", tags, this);

            // 在另一个线程中等待结果返回，避免阻塞主线程
            summaryFuture.thenAccept(result -> {
                callback.accept(result);
            }).exceptionally(ex -> {
                ex.printStackTrace();
                callback.accept(new HashMap<>()); // 返回一个空的Map作为错误处理
                return null;
            });
        }
    }

    public Map<String, AccountSummaryMessage> getAccountSummary() {
        // 如果当前没有正在进行的请求，开始一个新的请求
        if (summaryFuture == null || summaryFuture.isDone()) {
            // 取消之前的请求（如果有）
            controller.cancelAccountSummary(this);

            summaryFuture = new CompletableFuture<>();
            accountSummaryMessages.clear(); // 清除之前的结果
            controller.reqAccountSummary("All", tags, this);
        }

        try {
            // 阻塞等待结果返回
            return summaryFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void accountSummary(String account, AccountSummaryTag tag, String value, String currency) {
        System.out.println("Received account summary: " + account + ", " + tag + ", " + value);
        AccountSummaryMessage accountSummaryMessage = new AccountSummaryMessage(0, account, tag, value, currency);
        String key = account.concat("_").concat(tag.toString());

        if (AccountSummaryTag.NetLiquidation.equals(tag)) {
            updateAccountTQQQ(accountSummaryMessage);
        }

        accountSummaryMessages.put(key, accountSummaryMessage);
    }

    @Override
    public void accountSummaryEnd() {
        System.out.println("Account summary completed.");
        // 在账户摘要完成时，完成 future
        summaryFuture.complete(accountSummaryMessages);
    }

    private void updateAccountTQQQ(AccountSummaryMessage accountSummaryMessage) {
        MarketDataModel dataModel = dataView.getDataModel();
        String symbol = "UPRO";
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
}
