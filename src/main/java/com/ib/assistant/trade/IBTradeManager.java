package com.ib.assistant.trade;

import com.ib.assistant.IBAccountManager;
import com.ib.assistant.OpenOrdersManager;
import com.ib.assistant.PositionManager;
import com.ib.assistant.configuration.TQQQConfiguration;
import com.ib.assistant.market.MarketDataManger;
import com.ib.assistant.message.AccountSummaryMessage;
import com.ib.assistant.message.IBAccount;
import com.ib.assistant.message.IbPosition;
import com.ib.assistant.report.AuditModel;
import com.ib.assistant.report.IBAuditManager;
import com.ib.client.OrderType;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IBTradeManager {


    private List<AuditModel> AuditLogList;

    private ApiController m_controller;

    private IBAccountManager ibAccountManager;

    private PositionManager positionManager;

    private OpenOrdersManager openOrdersManager;

    private MarketDataManger marketDataManger;

    private IBAuditManager ibAuditManager;
    private TQQQConfiguration tqqqConfiguration;

    private ApiController.IConnectionHandler connectionHandler;

    private List<String> tradeMessages;

    private String accountId;

    private final Object executionLock = new Object();

    private boolean isExecuting = false;

    private void show(String message) {
        connectionHandler.show(message);
        if (tradeMessages != null) {
            tradeMessages.add(message);
        }
    }

    private boolean verifyTQQQConfiguration(TQQQConfiguration currentConfig) {
        Boolean isConfigured = true;
        if (currentConfig.getReferencePrice() == 0) {
            show("配置文件:参考价格错误");
            isConfigured = false;
        }
        ;
        if (currentConfig.getTriggerRate() == 0) {
            show("配置文件:触发比例错误");
            isConfigured = false;
        }
        return isConfigured;
    }

    public void adjustPositionTQQQ(OrderActionType orderType) {


        String symbol = "TQQQ";
        AuditModel auditModel = new AuditModel(accountId, symbol, orderType.name(), new Date());
        ibAuditManager.createAuditEntity(auditModel);
        Map<String, Double> params = auditModel.getParams();

        params.put("PlanAmount", tqqqConfiguration.getPlanAmount());
        params.put("ReferencePrice", tqqqConfiguration.getReferencePrice());
        params.put("TriggerRate", tqqqConfiguration.getTriggerRate());
        params.put("FundRate", tqqqConfiguration.getFundRate());
        params.put("AddRate",  tqqqConfiguration.getAddRate());
        params.put("UpTriggerPrice" , tqqqConfiguration.getUpTriggerPrice());
        params.put("DownTriggerPrice" , tqqqConfiguration.getDownTriggerPrice());

        tradeMessages = new ArrayList<>();
        show("*************开始仓位调整*************");

        synchronized (executionLock) {
            if (isExecuting) {
                show("重复的操作");
                return;
            }
            isExecuting = false;
        }

        if (!verifyTQQQConfiguration(tqqqConfiguration)) {
            ibAuditManager.saveIbAuditLog(auditModel);
            return;
        }


        try {
            if (marketDataManger.getLastPrice() == 0) {
                show("无效价格，不执行任何操作！");
                auditModel.setMarketLastPrice(marketDataManger.getLastPrice());
                ibAuditManager.saveIbAuditLog(auditModel);
                return;
            }

            IBAccount account = getAccountInfo();
            if (account == null || this.accountId.equalsIgnoreCase(account.getAccountID())) {
                show("用户编号与TWS用户不一致，不执行任何操作！ TWS用户编号:");
                ibAuditManager.saveIbAuditLog(auditModel);
                return;
            }


            List<Integer> openOrders = getPendingOrderId(symbol);
            auditModel.setOpenOrderId(openOrders);
            int hasPendingOrder = openOrders.size();
            if (hasPendingOrder > 0) {
                show("已经存在待执行订单，暂时退出等待...");
                show("仍有待执行订单，暂不操作!");
                ibAuditManager.saveIbAuditLog(auditModel);
                return;
            }


            // 仓位验证
            List<IbPosition> positions = positionManager.getIbPositions();
            // Get ETF  position
            Long currentTQQQETFPosition = positions.stream().filter(p -> {
                return (symbol.equals(p.getContract().symbol()) && "STK".equals(p.getContract().getSecType()));
            }).mapToLong(p -> p.getPosition().longValue()).sum();

            auditModel.setPosition(currentTQQQETFPosition.intValue());


            //  (净资产总市值 x 基金占比)，大于现有规划金额，则将其重置为规划金额
            double currentTQQQIdealAmount = account.getNetLiquidation() * tqqqConfiguration.getFundRate();


            show(String.format("TQQQ规划市值(净资产总市值 x 基金占比)%s = 净资产总市值%s * 基金占比 %s ",
                    String.valueOf(currentTQQQIdealAmount),
                    String.valueOf(account.getNetLiquidation()),
                    String.valueOf(tqqqConfiguration.getFundRate())
            ));


            params.put("CurrentTQQQIdealAmount", currentTQQQIdealAmount);
            params.put("NetLiquidation", account.getNetLiquidation());



        } catch (Exception ex) {

        }


    }

    //"已经存在待执行订单，暂时退出等待..."
    private List<Integer> getPendingOrderId(String symbol) {
        List<OpenOrdersManager.OpenOrderInfo> orderInfoList = openOrdersManager.getOpenOrderInfo();
        return orderInfoList.stream().filter(new Predicate<OpenOrdersManager.OpenOrderInfo>() {
            @Override
            public boolean test(OpenOrdersManager.OpenOrderInfo openOrderInfo) {
                return symbol.equals(openOrderInfo.OpenOrderContract.symbol())
                        && "STK".equals(openOrderInfo.OpenOrderContract.getSecType())
                        ;
            }
        }).map(new Function<OpenOrdersManager.OpenOrderInfo, Integer>() {
            @Override
            public Integer apply(OpenOrdersManager.OpenOrderInfo openOrderInfo) {
                return openOrderInfo.OpenOrder.permId();
            }
        }).collect(Collectors.toList());
    }

    private IBAccount getAccountInfo() {
        IBAccount account = new IBAccount();
        Map<String, AccountSummaryMessage> acctMassages = ibAccountManager.getAccountSummary();
        if (acctMassages == null) return null;
        for (String key : acctMassages.keySet()) {
            if (key.equalsIgnoreCase(
                    accountId.concat("_").concat(AccountSummaryTag.NetLiquidation.toString())
            )) {
                AccountSummaryMessage accountSummaryMessage = acctMassages.get(key);
                account.setAccountID(accountSummaryMessage.getAccountName());
                account.setNetLiquidation(Double.parseDouble(accountSummaryMessage.getAccountValue()));
            }
            if (key.equalsIgnoreCase(
                    accountId.concat("_").concat(AccountSummaryTag.TotalCashValue.toString())
            )) {
                AccountSummaryMessage accountSummaryMessage = acctMassages.get(key);
                account.setTotalCash(Double.parseDouble(accountSummaryMessage.getAccountValue()));
            }
        }
        show(String.format("账户:%s, 账户净值:%s, 账户总现金:%s", account.getAccountID(), String.valueOf(account.getNetLiquidation()), String.valueOf(account.getTotalCash())));
        return account;
    }


}
