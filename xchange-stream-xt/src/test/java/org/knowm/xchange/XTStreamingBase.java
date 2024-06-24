package org.knowm.xchange;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.knowm.xchange.utils.AuthUtils;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTStreamingExchange;
import org.knowm.xchange.xt.dto.ws.WebSocketToken;

/**
 * <p> @Date : 2024/6/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTStreamingBase {

  protected static XTStreamingExchange streamingExchange;

  @Before
  public void setUp() throws IOException {
    ExchangeSpecification spec = new XTExchange().getDefaultExchangeSpecification();
    AuthUtils.setApiAndSecretKey(spec);
    XTExchange exchange = (XTExchange) ExchangeFactory.INSTANCE.createExchange(spec);
    WebSocketToken wsToken = exchange.getWsTokenService().getWsToken();

    ExchangeSpecification defaultExchangeSpecification = new XTStreamingExchange().getDefaultExchangeSpecification();
    AuthUtils.setApiAndSecretKey(defaultExchangeSpecification);
    defaultExchangeSpecification.setExchangeSpecificParametersItem(XTStreamingExchange.LISTEN_KEY,
        wsToken.getAccessToken());

    streamingExchange = (XTStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(
        defaultExchangeSpecification);
    streamingExchange.connect().blockingAwait();
  }

}
