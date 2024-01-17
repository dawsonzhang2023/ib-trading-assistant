package com.ib.assistant.market;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketDataModel extends AbstractTableModel {

    private List<MarketDataStruct> dataRows;

    private Map<String, MarketDataStruct> rowMap;

    private static final int DESCRIPTION_INDEX = 0;
    private static final int MARKET_DATA_TYPE_INDEX = 1;
    private static final int LAST_PRICE_INDEX = 2;
    private static final int CURRENT_POSITION = 3;
    private static final int CURRENT_MARKET_VALUE = 4;
    private static final int CURRENT_ALLOCATION = 5;
    private static final int CURRENT_NET_LIQ = 6;
    private static final int HIGH_TRIGGER_PRICE = 7;
    private static final int lOW_TRIGGER_PRICE = 8;
    private static final int PLAN_AMOUNT = 9;

    private String[] columnNames = new String[]{
            "证券代码 ETF Code",
            "数据类型 Data Type",
            "当前价格 Market Price",
            "当前持仓 Position",
            "当前市值 Market Value",
            "基金占比 ETF Ratio",
            "账户净值 Total Net Value",
            "向上触发价 Upper Trigger Price",
            "向下触发价 Lower Trigger Price",
            "规划金额 Planned Amount"
    };

    public List<MarketDataStruct> getDataRows() {
        return dataRows;
    }

    public void insertRow(MarketDataStruct  rowData){
        this.rowMap.put(rowData.getSecurityCode() , rowData );
        this.dataRows.add(rowData);
    }

    public static class MarketDataStruct {
        private String securityCode;
        private String dataType;

        private double lastPrice;

        private double position;

        private double marketValue;

        private double marketRatio;

        private double netLiquidation;

        private double upTriggerPrice;

        private double downTriggerPrice;

        private double planAmount;

        public String getSecurityCode() {
            return securityCode;
        }

        public void setSecurityCode(String securityCode) {
            this.securityCode = securityCode;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public double getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(double lastPrice) {
            this.lastPrice = lastPrice;
        }

        public double getPosition() {
            return position;
        }

        public void setPosition(double position) {
            this.position = position;
        }

        public double getMarketValue() {
            return marketValue;
        }

        public void setMarketValue(double marketValue) {
            this.marketValue = marketValue;
        }

        public double getMarketRatio() {
            return marketRatio;
        }

        public void setMarketRatio(double marketRatio) {
            this.marketRatio = marketRatio;
        }

        public double getNetLiquidation() {
            return netLiquidation;
        }

        public void setNetLiquidation(double netLiquidation) {
            this.netLiquidation = netLiquidation;
        }

        public double getUpTriggerPrice() {
            return upTriggerPrice;
        }

        public void setUpTriggerPrice(double upTriggerPrice) {
            this.upTriggerPrice = upTriggerPrice;
        }

        public double getDownTriggerPrice() {
            return downTriggerPrice;
        }

        public void setDownTriggerPrice(double downTriggerPrice) {
            this.downTriggerPrice = downTriggerPrice;
        }

        public double getPlanAmount() {
            return planAmount;
        }

        public void setPlanAmount(double planAmount) {
            this.planAmount = planAmount;
        }

        public MarketDataStruct(String securityCode, String dataType, double lastPrice, double position, double marketValue, double marketRatio, double netLiquidation, double upTriggerPrice, double downTriggerPrice, double planAmount) {
            this.securityCode = securityCode;
            this.dataType = dataType;
            this.lastPrice = lastPrice;
            this.position = position;
            this.marketValue = marketValue;
            this.marketRatio = marketRatio;
            this.netLiquidation = netLiquidation;
            this.upTriggerPrice = upTriggerPrice;
            this.downTriggerPrice = downTriggerPrice;
            this.planAmount = planAmount;
        }


        public MarketDataStruct() {
        }

        public void calculateMarketData(){

            if( position > 0  &&  lastPrice > 0 ){
                    marketValue = position*lastPrice;
                    BigDecimal  mv = new BigDecimal(marketValue);
                    marketValue = mv.setScale(2 , BigDecimal.ROUND_UP ).doubleValue();
            }
            if( netLiquidation !=0 ){
                marketRatio =  marketValue/netLiquidation;
                BigDecimal  mr = new BigDecimal( marketRatio );
                marketRatio=mr.setScale(2 , BigDecimal.ROUND_UP).doubleValue();
            }
        }

        public Object[] getDataRow() {
            return new Object[]{
                    securityCode,
                    dataType,
                    lastPrice,
                    position,
                    marketValue,
                    marketRatio,
                    netLiquidation,
                    upTriggerPrice,
                    downTriggerPrice,
                    planAmount
            };
        }
    }

    public MarketDataModel() {
        this.dataRows = new ArrayList<>();
        this.rowMap = new HashMap<>();
    }

    public MarketDataModel(List<MarketDataStruct> dataRows) {
        this.dataRows = dataRows;
        this.rowMap = new HashMap<>();
        // row 2 map
        if (dataRows != null && dataRows.size() > 0) {
            for (MarketDataStruct data : dataRows) {
                rowMap.put(data.securityCode, data);
            }
        }
    }

    public MarketDataStruct findDataRowBySecurityCode(String securityCode) {
        return this.rowMap.getOrDefault(securityCode, null);
    }


    @Override
    public int getRowCount() {
        return dataRows.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        MarketDataStruct row = dataRows.get(rowIndex);
        switch (columnIndex) {
            case DESCRIPTION_INDEX:
                value = row.securityCode;
                break;
            case MARKET_DATA_TYPE_INDEX:
                value = row.dataType;
                break;
            case LAST_PRICE_INDEX:
                value = row.lastPrice;
                break;
            case CURRENT_POSITION:
                value = row.position;
                break;
            case CURRENT_MARKET_VALUE:
                value = row.marketValue;
                break;
            case CURRENT_ALLOCATION:
                value = row.marketRatio;
                break;
            case CURRENT_NET_LIQ:
                value = row.netLiquidation;
                break;
            case HIGH_TRIGGER_PRICE:
                value = row.upTriggerPrice;
                break;
            case lOW_TRIGGER_PRICE:
                value = row.downTriggerPrice;
                break;
            case PLAN_AMOUNT:
                value = row.planAmount;
                break;
            default:
                value = "";
        }
        return value;
    }
}
