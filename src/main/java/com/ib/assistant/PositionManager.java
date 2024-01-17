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

public class PositionManager  implements ApiController.IPositionHandler {

    private ApiController controller;

    private List<IbPosition> positionList;

    private IBMarketDataPanel dataView;

    public PositionManager(ApiController controller, IBMarketDataPanel dataView) {
        this.dataView =dataView;
        this.controller = controller;
        positionList = new ArrayList<>();
        getIbPositions();
    }

    public List<IbPosition> getIbPositions() {
        controller.reqPositions( this );
        return positionList;
    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        if (pos.compareTo(Decimal.ZERO) > 0 &&  contract.symbol().equals("TQQQ")) {
            positionList.clear();
            IbPosition  ibPosition = new IbPosition(account, contract, pos, avgCost);
            positionList.add(ibPosition);
            updatePosition( ibPosition );
        }
    }

    private void updatePosition(IbPosition ibPosition) {
        MarketDataModel  dataModel = dataView.getDataModel();
        String symbol = ibPosition.getContract().symbol();
        Contract  contract = ETFContractFactory.getInstance().getContractBySymbol(symbol);
        String securityCode = MarketDataUtil.convertContractToSecurityCode(contract);
        MarketDataModel.MarketDataStruct  dataRow = dataModel.findDataRowBySecurityCode(securityCode);
        if ( dataRow!= null ){
             dataRow.setPosition(ibPosition.getPosition().longValue());
             dataRow.calculateMarketData();
        }else {
            dataRow=new MarketDataModel.MarketDataStruct();
            dataRow.setSecurityCode( securityCode );
            dataRow.setPosition(ibPosition.getPosition().longValue());
            dataModel.insertRow(dataRow);
        }
        dataModel.fireTableDataChanged();
    }

    @Override
    public void positionEnd() {

    }
}
