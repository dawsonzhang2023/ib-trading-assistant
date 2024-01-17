package samples.testbed.request;

import com.ib.controller.ApiConnection;

public class ConsoleLogger implements ApiConnection.ILogger {
    @Override
    public void log(String valueOf) {
        System.out.println( valueOf );
    }
}
