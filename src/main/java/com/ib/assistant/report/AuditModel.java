package com.ib.assistant.report;

import com.ib.client.Order;
import com.ib.client.OrderStatus;

import java.util.*;

public class AuditModel {

    private String  accountId ;

    private int orderId;

    private String orderStatus;

    private String symbol;

    private  int position;

    private String orderType;
    private double marketLastPrice;

    private List<Integer> openOrderId;

    private Map<String,  Double> params;

    private Date startTime;

    private Date placeOrderTime;

    public AuditModel(String accountId, String symbol, String orderType, Date startTime) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.orderType = orderType;
        this.startTime = startTime;
        params=new HashMap<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getMarketLastPrice() {
        return marketLastPrice;
    }

    public void setMarketLastPrice(double marketLastPrice) {
        this.marketLastPrice = marketLastPrice;
    }

    public List<Integer> getOpenOrderId() {
        return openOrderId;
    }

    public void setOpenOrderId(List<Integer> openOrderId) {
        this.openOrderId = openOrderId;
    }

    public Map<String,  Double> getParams() {
        return params;
    }

    public void setParams(Map<String,  Double> params) {
        this.params = params;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getPlaceOrderTime() {
        return placeOrderTime;
    }

    public void setPlaceOrderTime(Date placeOrderTime) {
        this.placeOrderTime = placeOrderTime;
    }
}
