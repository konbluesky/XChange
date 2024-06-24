package org.knowm.xchange.xt.service;

import java.io.IOException;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTResilience;
import org.knowm.xchange.xt.dto.ws.WebSocketToken;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTWsTokenService extends XTBaseResilientExchangeService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public XTWsTokenService(XTExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public WebSocketToken getWsToken() throws IOException {
    return decorateApiCall(()->xtAuthenticated.getWsToken(
        BaseParamsDigest.HMAC_SHA_256,
        apiKey,
        RECV_WINDOW,
        String.valueOf(System.currentTimeMillis()),
        signatureCreator
    ).getData()).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();
  }

}
