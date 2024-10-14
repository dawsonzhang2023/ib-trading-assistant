package org.gui.frame;

import com.ib.client.*;
import samples.testbed.EWrapperImpl;

public class IBKRTickDataExample extends EWrapperImpl {
    EClientSocket clientSocket;

    public IBKRTickDataExample() {
        clientSocket =this.getClient();
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        System.out.println("Tick Price. Ticker Id: " + tickerId + ", Field: " + field + ", Price: " + price);
    }

    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        System.out.println("Tick Size. Ticker Id: " + tickerId + ", Field: " + field + ", Size: " + size);
    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        System.out.println("Tick String. Ticker Id: " + tickerId + ", Type: " + tickType + ", Value: " + value);
    }

    public void requestData() {
        Contract contract = new Contract();
        contract.symbol("UPRO");
        contract.secType("STK");
        contract.exchange("SMART");
        contract.currency("USD");
        // Ensure you have the correct primaryExchange for TQQQ (it might differ)
        contract.primaryExch("NASDAQ");

        clientSocket.reqMktData(1, contract,  "", false , false , null);
    }

    public void connect() {
        clientSocket.eConnect("127.0.0.1", 7497, 0); // Replace with your IB Gateway/TWS IP and port
    }

    public void disconnect() {
        clientSocket.eDisconnect();
    }

    public static void main(String[] args) {
        IBKRTickDataExample example = new IBKRTickDataExample();
        example.connect();
        example.requestData();
    }
}
