package samples.testbed;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.TagValue;
import com.ib.contracts.ETFContractFactory;

import java.util.ArrayList;

public class TestTickPriceHandler {


    public static void main(String[] args) {
        EWrapperImpl wrapper = new EWrapperImpl();

        final EClientSocket m_client = wrapper.getClient();
        final EReaderSignal m_signal = wrapper.getSignal();
        //! [connect]
        m_client.eConnect("127.0.0.1", 7497, 2);
        //! [connect]
        //! [ereader]
        final EReader reader = new EReader(m_client, m_signal);

        reader.start();
        //An additional thread is created in this program design to empty the messaging queue
      new Thread(() -> {
            while (m_client.isConnected()) {
                m_signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    System.out.println("Exception: "+e.getMessage());
                }
            }
        }).start();

        m_client.reqMktData( 100000 ,  ETFContractFactory.getInstance().getContractBySymbol( "VTV"), "" , false , false  , new ArrayList<TagValue>());

    }
}
