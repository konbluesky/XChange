package org.knowm.xchange.hashkey.service;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.hashkey.dto.ws.WebSocketToken;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyWsTokenService extends HashKeyBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public HashKeyWsTokenService(Exchange exchange) {
    super(exchange);
  }

  public WebSocketToken getWsToken() throws IOException {
    return hashKey.getWsToken(
        apiKey,
        nonceFactory,
        signatureCreator,
        recWindow
    );
  }

  public long time() throws IOException {
    return hashKey.time().get("serverTime").asLong();
  }
}
