package samples.testbed.request;

import com.ib.controller.ApiConnection;
import com.ib.controller.ApiController;
import com.ib.controller.Formats;

import java.util.List;

public class ApiControllerFactory {

    private ApiConnection.ILogger   outLogger ;

    private ApiConnection.ILogger   inLogger;

    private ApiController controller;

    private  static ApiControllerFactory  instance;

    public static ApiControllerFactory  getInstance() {
            if ( instance == null ){
                instance=new ApiControllerFactory();
            }
            return  instance ;
    }

    public ApiController getController() {
        if( controller == null ){
            outLogger = null;
            inLogger = null ;
            controller = new ApiController(new ApiController.IConnectionHandler() {
                @Override
                public void connected() {
                    show("connected");

                    controller.reqCurrentTime(time -> show("Server date/time is " + Formats.fmtDate(time * 1000)));

                    controller.reqBulletins(true, (msgId, newsType, message, exchange) -> {
                        String str = String.format("Received bulletin:  type=%s  exchange=%s", newsType, exchange);
                        show(str);
                        show(message);
                    });
                }

                @Override
                public void disconnected() {
                    show("disconnected");
                }

                @Override
                public void accountList(List<String> list) {
                    list.forEach( acct -> show("connect acct ::" + acct));
                }

                @Override
                public void error(Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void message(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
                    String error = id + " " + errorCode + " " + errorMsg;
                    if (advancedOrderRejectJson != null) {
                        error += (" " + advancedOrderRejectJson);
                    }
                    show(error);
                }

                @Override
                public void show(String string) {
                    System.out.println( string );
                }
            } , inLogger , outLogger );
        }
        return controller;
    }
}
