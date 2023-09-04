package org.knowm.xchange.mexc.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.mexc.dto.ws.WebSocketToken;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCWsTokenService extends MEXCBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public MEXCWsTokenService(Exchange exchange) {
    super(exchange);
  }

  public WebSocketToken getWsToken() throws IOException {
    return mexc.getWsToken(
        apiKey,
        nonceFactory,
        signatureCreator
    );
  }

  public JsonNode time() throws IOException {
    return mexc.time().getData();
  }
}
