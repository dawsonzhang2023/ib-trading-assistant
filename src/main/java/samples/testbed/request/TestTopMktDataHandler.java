package samples.testbed.request;

import com.ib.assistant.IBAccountManager;
import com.ib.assistant.message.AccountSummaryMessage;
import com.ib.client.Decimal;
import com.ib.client.TickAttrib;
import com.ib.client.TickType;
import com.ib.contracts.ETFContractFactory;
import com.ib.controller.ApiController;

import java.util.List;

public class TestTopMktDataHandler {

    public static void main(String[] args) throws InterruptedException {
        String hostname = "localhost";
        String symbol = "UPRO";

        ApiController controller = ApiControllerFactory.getInstance().getController();
        controller.connect(hostname, 7497, 0, null);

      /*  IBAccountManager acctManager = new IBAccountManager(controller);
        List<AccountSummaryMessage> messages = acctManager.getAccountSummary();*/
        ApiController.ITopMktDataHandler   tickHandler = new ApiController.ITopMktDataHandler() {
            @Override
            public void tickPrice(TickType tickType, double price, TickAttrib attribs) {
                if( TickType.LAST == tickType ||  TickType.DELAYED_LAST == tickType ) {
                    System.out.println("tick type :: " + tickType.toString());
                    System.out.println("Symbol :: " + symbol + " :: TickPrice :: " + price);
                }else {
                    System.out.println(tickType);
                }
            }

            @Override
            public void tickSize(TickType tickType, Decimal size) {

            }

            @Override
            public void tickString(TickType tickType, String value) {

            }

            @Override
            public void tickSnapshotEnd() {

            }

            @Override
            public void marketDataType(int marketDataType) {

            }

            @Override
            public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {

            }
        };




      controller.reqTopMktData(ETFContractFactory.getInstance().getContractBySymbol(symbol), "", false, false,   tickHandler);



      Thread.sleep( 5000);

      controller.cancelTopMktData( tickHandler );


      controller.disconnect();
    }
}
