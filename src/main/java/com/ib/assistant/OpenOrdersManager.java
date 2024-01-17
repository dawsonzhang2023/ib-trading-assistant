package com.ib.assistant;

import com.ib.client.*;
import com.ib.controller.ApiController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OpenOrdersManager implements ApiController.ILiveOrderHandler {

    private ApiController controller;

    private List<OpenOrderInfo> openOrderInfoList;

    public OpenOrdersManager(ApiController controller) {
        this.controller = controller;
        openOrderInfoList = new ArrayList<>();
    }

    public List<OpenOrderInfo> getOpenOrderInfo() {
        controller.reqLiveOrders(this);
        return openOrderInfoList;
    }

    @Override
    public void openOrder(Contract contract, Order order, OrderState orderState) {
            openOrderInfoList.add(new OpenOrderInfo(contract, order));
    }

    @Override
    public void openOrderEnd() {

    }

    @Override
    public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        if (
                status==OrderStatus.Cancelled ||  status==OrderStatus.Filled
                ||  status == OrderStatus.Inactive || status == OrderStatus.ApiCancelled
        ) {
            List<OpenOrderInfo> openOrders = openOrderInfoList.stream().filter(new Predicate<OpenOrderInfo>() {
                @Override
                public boolean test(OpenOrderInfo openOrderInfo) {
                    return openOrderInfo.OpenOrder.orderId() == orderId;
                }
            }).collect(Collectors.toList());
            for (OpenOrderInfo info : openOrders
            ) {
                openOrderInfoList.remove(info);
            }
        }
    }

    @Override
    public void handle(int orderId, int errorCode, String errorMsg) {
        System.out.println("order ID :: " + orderId + " errorCode :: " + errorCode + " errorMsg ::" + errorMsg);
    }

    public  class OpenOrderInfo {
        public Contract OpenOrderContract;
        public Order OpenOrder;

        public OpenOrderInfo(Contract openOrderContract, Order openOrder) {
            OpenOrderContract = openOrderContract;
            OpenOrder = openOrder;
        }
    }


}
