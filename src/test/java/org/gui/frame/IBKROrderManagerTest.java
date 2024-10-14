package org.gui.frame;

import com.ib.client.*;
import com.ib.controller.ApiController;
import samples.testbed.request.ApiControllerFactory;

public class IBKROrderManagerTest {
    public static void main(String[] args) {

        String hostname = "localhost";
        String symbol = "UPRO";

        ApiController controller = ApiControllerFactory.getInstance().getController();
        controller.connect(hostname, 7497, 0, null);

        controller.reqLiveOrders(new ApiController.ILiveOrderHandler() {
            @Override
            public void openOrder(Contract contract, Order order, OrderState orderState) {
                System.out.println( "open order :::" + contract.symbol()  +  " open order :: " +  order.orderId() + " orderState ::" + orderState.status() );
            }

            @Override
            public void openOrderEnd() {
                System.out.println("openOrderEnd");
            }

            @Override
            public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
                System.out.println( " open order ID:: " + orderId+ " orderState ::" +status);
            }

            @Override
            public void handle(int orderId, int errorCode, String errorMsg) {

            }
        });


    }
}
