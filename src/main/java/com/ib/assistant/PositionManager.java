package com.ib.assistant;

import com.ib.assistant.market.IBMarketDataPanel;
import com.ib.assistant.market.MarketDataModel;
import com.ib.assistant.market.MarketDataUtil;
import com.ib.assistant.message.IbPosition;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.contracts.ETFContractFactory;
import com.ib.controller.ApiController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class PositionManager  implements ApiController.IPositionHandler {

    private final ApiController controller;
    private final IBMarketDataPanel dataView;
    private final List<IbPosition> positionList;
    private CompletableFuture<List<IbPosition>> positionsFuture;

    public PositionManager(ApiController controller, IBMarketDataPanel dataView) {
        this.dataView =dataView;
        this.controller = controller;
        positionList = new ArrayList<>();
    }
    public void getIbPositionsAsync(Consumer<List<IbPosition>> callback) {
        // 如果当前没有正在进行的请求，开始一个新的请求
        if (positionsFuture == null || positionsFuture.isDone()) {
            positionsFuture = new CompletableFuture<>();
            positionList.clear(); // 清除之前的结果
            controller.reqPositions(this);

            // 异步等待结果返回，避免阻塞主线程
            positionsFuture.thenAccept(positions -> {
                callback.accept(positions);
            }).exceptionally(ex -> {
                ex.printStackTrace();
                callback.accept(new ArrayList<>()); // 返回一个空的List作为错误处理
                return null;
            });
        } else {
            // 如果已经有一个正在进行的请求，直接返回现有的future结果
            positionsFuture.thenAccept(callback);
        }
    }

    public List<IbPosition> getIbPositions() {
        // 如果当前没有正在进行的请求，开始一个新的请求
        if (positionsFuture == null || positionsFuture.isDone()) {
            positionsFuture = new CompletableFuture<>();
            positionList.clear(); // 清除之前的结果
            controller.reqPositions(this);
        }

        try {
            // 阻塞等待结果返回
            return positionsFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        if (pos.compareTo(Decimal.ZERO) > 0 && contract.symbol().equals("UPRO")) {
            // 清除之前的TQQQ仓位并更新
            positionList.clear();
            IbPosition ibPosition = new IbPosition(account, contract, pos, avgCost);
            positionList.add(ibPosition);
            updatePosition(ibPosition);
        }
    }

    private void updatePosition(IbPosition ibPosition) {
        MarketDataModel dataModel = dataView.getDataModel();
        String symbol = ibPosition.getContract().symbol();
        Contract contract = ETFContractFactory.getInstance().getContractBySymbol(symbol);
        String securityCode = MarketDataUtil.convertContractToSecurityCode(contract);
        MarketDataModel.MarketDataStruct dataRow = dataModel.findDataRowBySecurityCode(securityCode);

        if (dataRow != null) {
            dataRow.setPosition(ibPosition.getPosition().longValue());
            dataRow.calculateMarketData();
        } else {
            dataRow = new MarketDataModel.MarketDataStruct();
            dataRow.setSecurityCode(securityCode);
            dataRow.setPosition(ibPosition.getPosition().longValue());
            dataModel.insertRow(dataRow);
        }
        dataModel.fireTableDataChanged();
    }

    @Override
    public void positionEnd() {
        // 在仓位信息接收结束时，完成future
        positionsFuture.complete(new ArrayList<>(positionList));
    }
}
