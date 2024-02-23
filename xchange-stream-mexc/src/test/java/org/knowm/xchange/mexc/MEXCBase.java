package org.knowm.xchange.mexc;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.junit.BeforeClass;
import org.knowm.xchange.ExchangeSpecification;

/**
 * <p> @Date : 2024/2/23 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCBase {

  protected static MEXCStreamingExchange streamingExchange;
  protected static String apikey="";
  protected static String apiSecret="";
  @BeforeClass
  public static void initExchange() {
    ExchangeSpecification spec =
        StreamingExchangeFactory.INSTANCE
            .createExchangeWithoutSpecification(MEXCStreamingExchange.class)
            .getDefaultExchangeSpecification();
    spec.setApiKey(apikey);
    spec.setSecretKey(apiSecret);
    spec.setShouldLoadRemoteMetaData(false);
    spec.getExchangeSpecificParameters().put(MEXCStreamingExchange.IS_PRIVATE, true);
    streamingExchange = (MEXCStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(spec);
    streamingExchange.connect().blockingAwait();
  }



}
