package com.ib.assistant.market;

import com.ib.assistant.configuration.TQQQConfiguration;
import com.ib.client.Contract;
import com.ib.client.MarketDataType;
import com.ib.client.TickAttrib;
import com.ib.client.TickType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.HashMap;

public class MarketDataManger {

    public   static final  int  TICK_ID_BASE = 10000000;

    // private Map<String ,  >
    private String MarketDataType ;
    private  double  lastPrice = 0 ;

    private boolean active=false;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private IBMarketDataPanel  dataView ;

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }


    public MarketDataManger(IBMarketDataPanel dataView) {
        this.dataView = dataView;
    }


    public void updateMarketDataType( int  marketDateType ){
            this.MarketDataType = com.ib.client.MarketDataType.getField(marketDateType);
    }

    public void updateDataView(Contract  contract , TickType tickType, double price, TickAttrib attribs, TQQQConfiguration currentConfig) {
        MarketDataModel  dataModel = dataView.getDataModel();
        String securityCode = convertContractToSecurityCode( contract );
        if ( dataModel.findDataRowBySecurityCode( securityCode) != null ){
            MarketDataModel.MarketDataStruct  dataRow = dataModel.findDataRowBySecurityCode( securityCode );
            dataRow.setSecurityCode( securityCode );
            dataRow.setDataType( this.MarketDataType  );
            dataRow.setLastPrice( price );
            dataRow.setPlanAmount(currentConfig.getPlanAmount());
            dataRow.setUpTriggerPrice(currentConfig.getUpTriggerPrice());
            dataRow.setDownTriggerPrice(currentConfig.getDownTriggerPrice());
            dataRow.calculateMarketData();
        }else {
            MarketDataModel.MarketDataStruct  dataRow = new MarketDataModel.MarketDataStruct();
            dataRow.setSecurityCode( securityCode );
            dataRow.setDataType( this.MarketDataType  );
            dataRow.setLastPrice( price );
            dataRow.setPlanAmount(currentConfig.getPlanAmount());
            dataRow.setUpTriggerPrice(currentConfig.getUpTriggerPrice());
            dataRow.setDownTriggerPrice(currentConfig.getDownTriggerPrice());
            dataModel.insertRow( dataRow );
        }
        dataModel.fireTableDataChanged();
    }

    private String convertContractToSecurityCode(Contract contract) {
        return  contract.symbol() + " " + contract.getSecType() + " " + contract.currency() + " @" + contract.exchange();
    }
}
