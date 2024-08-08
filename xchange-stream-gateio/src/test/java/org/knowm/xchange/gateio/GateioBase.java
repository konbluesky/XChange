package org.knowm.xchange.gateio;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.junit.BeforeClass;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.AuthUtils;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioBase {

  protected static GateioStreamingExchange streamingExchange;
  @BeforeClass
  public static void initExchange() {
    ExchangeSpecification spec =
        StreamingExchangeFactory.INSTANCE
            .createExchangeWithoutSpecification(GateioStreamingExchange.class)
            .getDefaultExchangeSpecification();
    AuthUtils.setApiAndSecretKey(spec);
    spec.setShouldLoadRemoteMetaData(false);
    streamingExchange = (GateioStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(spec);
    streamingExchange.connect().blockingAwait();
  }


}
