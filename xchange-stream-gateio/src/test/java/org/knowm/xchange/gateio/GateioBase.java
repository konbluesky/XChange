package org.knowm.xchange.gateio;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.junit.BeforeClass;
import org.knowm.xchange.ExchangeSpecification;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioBase {

  protected static GateioStreamingExchange streamingExchange;
  protected static String apikey="";
  protected static String apiSecret="";
  @BeforeClass
  public static void initExchange() {
    ExchangeSpecification spec =
        StreamingExchangeFactory.INSTANCE
            .createExchangeWithoutSpecification(GateioStreamingExchange.class)
            .getDefaultExchangeSpecification();
    spec.setApiKey(apikey);
    spec.setSecretKey(apiSecret);
    spec.setShouldLoadRemoteMetaData(false);
    streamingExchange = (GateioStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(spec);
    streamingExchange.connect().blockingAwait();
  }


}
