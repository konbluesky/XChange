package org.knowm.xchange.hashkey.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.hashkey.HashKeyExchange;
import org.knowm.xchange.hashkey.HashKeyExchangeTest;
import org.knowm.xchange.hashkey.dto.ws.WebSocketToken;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyWsTokenServiceTest extends HashKeyExchangeTest {

  @Test
  public void testWsToken() throws IOException {
    HashKeyExchange exchange = (HashKeyExchange) createRawExchange();
    HashKeyWsTokenService wsTokenService = exchange.getWsTokenService();
    WebSocketToken wsToken = wsTokenService.getWsToken();
    log.info("wsToken : {}", wsToken.getListenKey());
  }

}