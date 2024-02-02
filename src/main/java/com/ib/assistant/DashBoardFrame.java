package com.ib.assistant;

import apidemo.util.IConnectionConfiguration;
import com.ib.assistant.configuration.TQQQConfiguration;
import com.ib.assistant.content.TQQQConfigurationPanel;
import com.ib.assistant.market.IBMarketDataPanel;
import com.ib.assistant.market.MarketDataManger;
import com.ib.assistant.market.MarketDataUtil;
import com.ib.assistant.message.AccountSummaryMessage;
import com.ib.assistant.message.IBAccount;
import com.ib.assistant.message.IbPosition;
import com.ib.assistant.panels.*;
import com.ib.assistant.ui.OrderAction;
import com.ib.client.*;
import com.ib.contracts.ETFContractFactory;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiConnection;
import com.ib.controller.ApiController;
import com.ib.controller.Formats;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Predicate;


public class DashBoardFrame extends JFrame implements ApiController.IConnectionHandler {


    private ApiController m_controller;

    private static final String symbol = "TQQQ";

    private MarketDataManger marketDataManger;

    private OpenOrdersManager openOrdersManager;

    private IBAccountManager accountManager;

    private PositionManager positionManager;

    private final List<String> m_acctList = new ArrayList<>();

    ApiConnection.ILogger getInLogger() {
        return m_inLogger;
    }

    ApiConnection.ILogger getOutLogger() {
        return m_outLogger;
    }

    public ApiController controller() {
        if (m_controller == null) {
            m_controller = new ApiController(this, getInLogger(), getOutLogger());
        }
        return m_controller;
    }

    @Override
    public void connected() {
        show("connected");
        //m_connectionPanel.m_status.setText( "connected");
        headerPanel.getConnectionPanel().getStatusLabel().setText("连接 Connected");
        TQQQConfigurationPanel configurationPanel = contentPanel.getConfigPanel();
        TQQQConfiguration configuration = configurationPanel.getConfiguration();

        String clientId = headerPanel.getConnectionPanel().m_clientId.getText();
        try {
            int cid = Integer.parseInt(clientId);
            configuration.setClientId(cid);
            configurationPanel.updateConfig(configuration);
        } catch (Exception ex) {
            show(ex.getMessage());
        }

        //configuration.setClientId(  );

        controller().reqCurrentTime(time -> show("Server date/time is " + Formats.fmtDate(time * 1000)));

        controller().reqBulletins(true, (msgId, newsType, message, exchange) -> {
            String str = String.format("Received bulletin:  type=%s  exchange=%s", newsType, exchange);
            show(str);
            show(message);
        });

        isConnected = true;
        headerPanel.getConnectionPanel().connectButton.setText("断开连接 Disconnect");
    }

    @Override
    public void disconnected() {
        show("disconnected");
        headerPanel.getConnectionPanel().getStatusLabel().setText("中断 Disconnected");
        headerPanel.getConnectionPanel().connectButton.setText("连接 Connect");
        isConnected = false;
    }

    @Override
    public void accountList(List<String> list) {
        show("Received account list");
        m_acctList.clear();
        m_acctList.addAll(list);
    }

    @Override
    public void error(Exception e) {
        show(e.toString());
    }

    @Override
    public void message(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        String error = id + " " + errorCode + " " + errorMsg;
        if (advancedOrderRejectJson != null) {
            error += (" " + advancedOrderRejectJson);
        }
        show(error);
    }

    @Override
    public void show(String message) {
        JTextArea m_msg = footerPanel.getM_msg();
        SwingUtilities.invokeLater(() -> {
            footerPanel.getM_msg().append(message);
            m_msg.append("\n\n");

            Dimension d = m_msg.getSize();
            m_msg.scrollRectToVisible(new Rectangle(0, d.height, 1, 1));
        });
    }

    private static class Logger implements ApiConnection.ILogger {
        final private JTextArea m_area;

        Logger(JTextArea area) {
            m_area = area;
        }

        @Override
        public void log(final String str) {
            SwingUtilities.invokeLater(() -> {
//					m_area.append(str);
//
//					Dimension d = m_area.getSize();
//					m_area.scrollRectToVisible( new Rectangle( 0, d.height, 1, 1) );
            });
        }
    }


    private Logger m_inLogger = null;
    private Logger m_outLogger = null;

    private HeaderPanel headerPanel = null;

    private FooterPanel footerPanel = null;

    private ContentPanel contentPanel = null;

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    private Boolean isConnected = false;

    public DashBoardFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1640, 1000);
        setLayout(new BorderLayout());


        headerPanel = new HeaderPanel(this);
        footerPanel = new FooterPanel();
        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);


        m_outLogger = new Logger(footerPanel.getM_outLog());
        m_inLogger = new Logger(footerPanel.getM_inLog());

        IbConnectionPanel connectionPanel = headerPanel.getConnectionPanel();


        IConnectionConfiguration m_connectionConfiguration = connectionPanel.getConnectionConfiguration();
        //controller().connect("127.0.0.1", 7496, ClientId, m_connectionConfiguration.getDefaultConnectOptions() != null ? "" : null);

        connectionPanel.connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnected) {
                    int port = Integer.parseInt(connectionPanel.m_port.getText());
                    int clientId = Integer.parseInt(connectionPanel.m_clientId.getText());
                    controller().connect(connectionPanel.m_host.getText(), port, clientId, connectionPanel.m_connectOptionsTF.getText());
                } else {
                    controller().disconnect();
                }
            }
        });

    }

    public void initComponent() {
        contentPanel = new ContentPanel();
        contentPanel.setPreferredSize(new Dimension(1610, 500));
        Border margin = new EmptyBorder(5, 5, 5, 5);
        contentPanel.setBorder(margin);
        IbConnectionPanel connectionPanel = headerPanel.getConnectionPanel();

        contentPanel.getConfigPanel().setClientIdField(connectionPanel.m_clientId);
        try {
            contentPanel.getConfigPanel().loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IBMarketDataPanel dataView = contentPanel.getMarketDataPanel();
        openOrdersManager = new OpenOrdersManager(controller());
        accountManager = new IBAccountManager(controller(), dataView);
        positionManager = new PositionManager(controller(), dataView);
        marketDataManger = new MarketDataManger(dataView);
        add(contentPanel, BorderLayout.CENTER);
        ControlsPanel controlsPanel = this.headerPanel.getControlsPanel();

        Contract contract = ETFContractFactory.getInstance().getContractBySymbol(symbol);

        // init  tick price handler
        ApiController.ITopMktDataHandler tickPriceHandler = new ApiController.ITopMktDataHandler() {
            @Override
            public void tickPrice(TickType tickType, double price, TickAttrib attribs) {
                TQQQConfiguration currentConfig = contentPanel.getConfigPanel().getConfiguration();
                if (TickType.LAST.equals(tickType) || TickType.DELAYED_LAST.equals(tickType)) {
                    // show("tickPrice ::" + price);
                    marketDataManger.setLastPrice(price);
                    marketDataManger.updateDataView(contract, tickType, price, attribs, currentConfig);
                    if (price >= currentConfig.getUpTriggerPrice() || price <= currentConfig.getDownTriggerPrice()) {
                        // 大于上限 或者 小于下限 调整仓位
                        adjustPosition();
                    }
                }
            }

            @Override
            public void tickSize(TickType tickType, Decimal size) {

            }

            @Override
            public void tickString(TickType tickType, String value) {

            }

            @Override
            public void tickSnapshotEnd() {

            }

            @Override
            public void marketDataType(int marketDataType) {
                marketDataManger.updateMarketDataType(marketDataType);
            }

            @Override
            public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {

            }
        };


        controlsPanel.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TQQQConfigurationPanel configurationPanel = contentPanel.getConfigPanel();
                TQQQConfiguration currentConfig = null;
                try {
                    currentConfig = configurationPanel.getConfiguration();
                } catch (Exception ex) {
                    show("配置文件读取错误:" + ex.getMessage());
                    return;
                }
                if (!verifyTQQQConfiguration(currentConfig)) return;
                if (isConnected && !marketDataManger.isActive()) {
                    marketDataManger.setActive(true);
                    Contract contract = ETFContractFactory.getInstance().getContractBySymbol(symbol);
                    controller().reqMktDataType(3);
                    controller().reqTopMktData(contract, "", false, false, tickPriceHandler);
                    positionManager.getIbPositions();
                    accountManager.getAccountSummary();
                    openOrdersManager.getOpenOrderInfo();
                }
            }
        });


        controlsPanel.getPauseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    controller().cancelTopMktData(tickPriceHandler);
                    marketDataManger.setActive(false);
                }
            }
        });

        controlsPanel.getManualButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    adjustPosition();
                } else {
                    show("尚未连接IB, 请先连接本地IB");
                }
            }
        });


    }

    private final Object executionLock = new Object();

    private boolean isExecuting = false;

    private void adjustPosition() {
        show("*************开始仓位调整*************");
        TQQQConfigurationPanel configurationPanel = contentPanel.getConfigPanel();

        TQQQConfiguration currentConfig = null;
        try {
            currentConfig = configurationPanel.getConfiguration();
        } catch (Exception e) {
            show("配置文件读取错误:" + e.getMessage());
            return;
        }

        if (!verifyTQQQConfiguration(currentConfig)) return;

        synchronized (executionLock) {
            if (isExecuting) {
                return;
            }
            isExecuting = true;
        }
        try {
            if (marketDataManger.getLastPrice() == 0) {
                show("无效价格，不执行任何操作！");
                return;
            }
            IBAccount account = getAccountInfo();
            if (account == null || !m_acctList.get(0).endsWith(String.valueOf(currentConfig.getClientId()))  || currentConfig.getClientId()==0) {
                show("用户编号与TWS用户不一致，不执行任何操作！ TWS用户编号:");
                return;
            }
            // "已经存在待执行订单，暂时退出等待..."
            boolean hasPendingOrder = isPendingOrder(symbol);
            if (hasPendingOrder) {
                show("已经存在待执行订单，暂时退出等待...");
                show("仍有待执行订单，暂不操作!");
                return;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            // 仓位验证
            List<IbPosition> positions = positionManager.getIbPositions();
            // Get ETF  position
            Long currentTQQQETFPosition = positions.stream().filter(p -> {
                return (symbol.equals(p.getContract().symbol()) && "STK".equals(p.getContract().getSecType()));
            }).mapToLong(p -> p.getPosition().longValue()).sum();

            //  (净资产总市值 x 基金占比)，大于现有规划金额，则将其重置为规划金额
            double currentTQQQIdealAmount = account.getNetLiquidation() * currentConfig.getFundRate();
            show(String.format("TQQQ规划市值(净资产总市值 x 基金占比)%s = 净资产总市值%s * 基金占比 %s ",
                    String.valueOf(df.format(currentTQQQIdealAmount)),
                    String.valueOf(df.format(account.getNetLiquidation())),
                    String.valueOf(df.format(currentConfig.getFundRate()))
            ));
            if (currentTQQQIdealAmount > currentConfig.getPlanAmount()) {
                show(String.format("重置最新规划金额为 {%s} : 原有规划金额 {%s}",
                        String.valueOf(df.format(currentTQQQIdealAmount)),
                        String.valueOf(df.format(currentConfig.getPlanAmount()))
                ));
                currentConfig.setPlanAmount(currentTQQQIdealAmount);
                configurationPanel.planAmtField.setText(df.format(currentTQQQIdealAmount));
                configurationPanel.updateConfig(currentConfig);

            }

            // 初始化订单
            Types.Action action = null;
            double diff = 0.0d;

            // 基金市值高于最新规划金额的部分，为本次减仓金额。
            double currentMarketValue = currentTQQQETFPosition * marketDataManger.getLastPrice();
            show(String.format("当前TQQQ市场金额: {%s}. 当前持仓： {%s} 当前市场价格： {%s}",
                    String.valueOf(df.format(currentMarketValue)),
                    String.valueOf(df.format(currentTQQQETFPosition)),
                    String.valueOf(df.format(marketDataManger.getLastPrice()))
            ));

            if (currentMarketValue > currentConfig.getPlanAmount()) {
                show(String.format("执行减仓:当前TQQQ市场价格{%s}大于{%s}最新规划金额",
                        String.valueOf(df.format(currentMarketValue)),
                        String.valueOf(df.format(currentConfig.getPlanAmount()))
                ));
                diff = currentMarketValue - currentConfig.getPlanAmount();
                show(String.format("需要减仓金额为: 最新市值-最新规划金额{%s}", String.valueOf(df.format(diff))));
                if (diff < 100) {
                    show("金额差异小于100，不执行任何操作");
                    return;
                }
                action = Types.Action.SELL;
            }

            // currentMarketValue < 规划金额
            if (currentMarketValue < currentConfig.getPlanAmount()) {
                show(String.format("执行加仓:当前TQQQ市场价格 {%s} 小于 {%s}(规划金额)",
                        String.valueOf(df.format(currentMarketValue)),
                        String.valueOf(df.format(currentTQQQIdealAmount))
                ));
                // ((规划金额 - 基金市值) x 加仓比例)
                double compareValue1 = (currentConfig.getPlanAmount() - currentMarketValue) * currentConfig.getAddRate();
                show(String.format("市值1((规划金额 - 基金市值) x 加仓比例):{%s} ((规划金额{%s}  - 基金市值{%s}) x 加仓比例{%s}) ",
                        String.valueOf(df.format(compareValue1)),
                        String.valueOf(df.format(currentConfig.getPlanAmount())),
                        String.valueOf(df.format(currentMarketValue)),
                        String.valueOf(df.format(currentConfig.getAddRate()))
                ));

                double compareValue2 = account.getTotalCash() + currentConfig.getDebtLimit();
                show(String.format("市值2(现金总市值 + 负债上限)：{%s} 现金总市值{%s} + 负债上限{%s}",
                        String.valueOf(df.format(compareValue2)),
                        String.valueOf(df.format(account.getTotalCash())),
                        String.valueOf(df.format(currentConfig.getDebtLimit())))
                );

                if (compareValue2 < 0) {
                    show("(现金总市值 + 负债上限)为负数，不执行任何操作");
                    return;
                }

                if (compareValue1 < compareValue2) {
                    show("加仓金额为市值1: " + df.format(compareValue1));
                    diff = compareValue1;
                } else {
                    show("加仓金额为市值2: " + df.format(compareValue2));
                    diff = compareValue2;
                }

                if (diff < 100) {
                    show("金额差异小于100，不执行任何操作");
                    return;
                }

                action = Types.Action.BUY;

            }


            // 开仓下单
            Integer positionAmt = Math.toIntExact(Math.round(diff / marketDataManger.getLastPrice()));
            if (action == Types.Action.BUY) {
                show("买入TQQQ:" + positionAmt);
                PlaceOrder(action, positionAmt);
            } else if (action == Types.Action.SELL) {
                show("卖出TQQQ:" + positionAmt);
                PlaceOrder(action, positionAmt);
            } else {
                show("不执行任何操作");
            }
        } catch (Exception e) {
            show(e.getMessage());
        } finally {
            if (marketDataManger.getLastPrice() > 0) {
                currentConfig.setReferencePrice(marketDataManger.getLastPrice());
            }
            isExecuting = false;
            show("*************结束仓位调整*************");
            try {
                configurationPanel.updateConfig(currentConfig);
            } catch (IOException e) {
                show("配置文件保存错误:" + e.getMessage());
            }
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

    private ApiController.IOrderHandler orderHandler = new ApiController.IOrderHandler() {
        @Override
        public void orderState(OrderState orderState) {

        }

        @Override
        public void orderStatus(OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {

        }

        @Override
        public void handle(int errorCode, String errorMsg) {
            show("下单状态: " + errorCode + " 信息: " + errorMsg);
        }
    };

    private void PlaceOrder(Types.Action action, Integer positionAmt) {

        Contract contract = ETFContractFactory.getInstance().getContractBySymbol(symbol);
        Order order = new Order();
        order.action(action);
        order.orderType(OrderType.MKT);
        order.totalQuantity(Decimal.get(positionAmt));
        order.tif("GTC");
        controller().placeOrModifyOrder(contract, order, orderHandler);
    }

    //"已经存在待执行订单，暂时退出等待..."
    private boolean isPendingOrder(String symbol) {

        List<OpenOrdersManager.OpenOrderInfo> orderInfoList = openOrdersManager.getOpenOrderInfo();
        return orderInfoList.stream().filter(new Predicate<OpenOrdersManager.OpenOrderInfo>() {
            @Override
            public boolean test(OpenOrdersManager.OpenOrderInfo openOrderInfo) {
                return symbol.equals(openOrderInfo.OpenOrderContract.symbol())
                        && "STK".equals(openOrderInfo.OpenOrderContract.getSecType())
                        ;
            }
        }).count() > 0;
    }


    private IBAccount getAccountInfo() {
        IBAccount account = new IBAccount();
        String accountId = m_acctList.get(0);
        Map<String, AccountSummaryMessage> acctMassages = accountManager.getAccountSummary();
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
