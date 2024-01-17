package samples.testbed.request;

import com.ib.assistant.IBAccountManager;
import com.ib.assistant.message.AccountSummaryMessage;
import com.ib.controller.ApiController;

import java.util.List;

public class TestAccountSummaryHandler {



    public static void main(String[] args) {
        String hostname = "localhost";


        ApiController   controller = ApiControllerFactory.getInstance().getController();
        controller.connect( hostname , 7497 ,  0  , null );



        //controller.reqTopMktData(  );


    }


}
