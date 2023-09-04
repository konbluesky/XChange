package org.knowm.xchange.mexc.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.ws.WebSocketToken;
import org.knowm.xchange.service.account.AccountService;

/**
 * <p> @Date : 2023/8/30 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCWsTokenServiceTest extends BaseWiremockTest {

  @Test
  public void testWsToken() throws IOException {
    MEXCExchange exchange= (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();
    WebSocketToken wsToken = wsTokenService.getWsToken();
    log.info("wsToken : {}",wsToken.getListenKey());
  }

  @Test
  public void testTime() throws IOException {
    MEXCExchange exchange= (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();
    log.info("wsToken : {}",wsTokenService.time());
  }

  @Test
  public void testAccount() throws IOException {
    MEXCExchange exchange= (MEXCExchange) createRawExchange();
    MEXCAccountService accountService = (MEXCAccountService) exchange.getAccountService();
    log.info("{}",accountService.getAccountInfo());
  }

}
