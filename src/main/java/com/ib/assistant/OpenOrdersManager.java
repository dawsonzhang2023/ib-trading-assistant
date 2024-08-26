package com.ib.assistant;

import com.ib.client.*;
import com.ib.controller.ApiController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;


public class OpenOrdersManager implements ApiController.ILiveOrderHandler {

    private ApiController controller;
    private final List<OpenOrderInfo> openOrderInfoList;
    private CompletableFuture<List<OpenOrderInfo>> openOrdersFuture;

    public OpenOrdersManager(ApiController controller) {
        this.controller = controller;
        openOrderInfoList = new ArrayList<>();
    }
    public List<OpenOrderInfo> getOpenOrderInfo() {
        // 如果当前没有正在进行的请求，开始一个新的请求
        if (openOrdersFuture == null || openOrdersFuture.isDone()) {
            openOrdersFuture = new CompletableFuture<>();
            openOrderInfoList.clear(); // 清除之前的结果
            controller.reqLiveOrders(this);
        }

        try {
            // 阻塞等待结果返回
            return openOrdersFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void openOrder(Contract contract, Order order, OrderState orderState) {
        // 这里确保线程安全地添加新订单信息
        openOrderInfoList.add(new OpenOrderInfo(contract, order));
    }

    @Override
    public void openOrderEnd() {
        // 在所有订单信息接收完成后，完成future
        openOrdersFuture.complete(new ArrayList<>(openOrderInfoList));
    }

    @Override
    public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining,
                            double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        // 如果订单被取消、完成或变为非活动状态，从列表中移除相关订单
        if (status == OrderStatus.Cancelled || status == OrderStatus.Filled
                || status == OrderStatus.Inactive || status == OrderStatus.ApiCancelled) {
            openOrderInfoList.removeIf(info -> info.getOpenOrder().orderId() == orderId);
        }
    }

    @Override
    public void handle(int orderId, int errorCode, String errorMsg) {
        // 处理订单错误的日志输出
        System.err.println("Order ID: " + orderId + ", Error Code: " + errorCode + ", Error Message: " + errorMsg);
    }

    public static class OpenOrderInfo {
        private final Contract openOrderContract;
        private final Order openOrder;

        public OpenOrderInfo(Contract openOrderContract, Order openOrder) {
            this.openOrderContract = openOrderContract;
            this.openOrder = openOrder;
        }

        public Contract getOpenOrderContract() {
            return openOrderContract;
        }

        public Order getOpenOrder() {
            return openOrder;
        }
    }
}
