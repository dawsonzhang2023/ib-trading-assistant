package com.ib;

import com.ib.client.*;
import samples.testbed.EWrapperImpl;

public class OpenOrdersExample extends EWrapperImpl {
    public OpenOrdersExample() {
        //clientSocket = new EClientSocket(this.clientSocket.wrapper(),  this.getSignal());
    }

    public void getOpenOrders() {
        this.getClient().reqOpenOrders(); // Request information about all open orders
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        // This method is called when an open order information is received
        System.out.println("Open Order ID: " + orderId);
        System.out.println("Contract: " + contract);
        System.out.println("Order: " + order);
        System.out.println("Order State: " + orderState);
        System.out.println("-----------------------------");
        // Handle the order information as needed
    }


    public static void main(String[] args) {
        OpenOrdersExample example = new OpenOrdersExample();

        example.getClient().eConnect("127.0.0.1", 7497, 0);
        example.getOpenOrders(); // Request information about open orders
        // It will trigger openOrder method to handle the received open order information
    }
}
