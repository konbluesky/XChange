package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.ws.WebSocketToken;

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
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();
    WebSocketToken wsToken = wsTokenService.getWsToken();
    log.info("wsToken : {}", wsToken.getListenKey());
  }

  @Test
  public void testGetWsTokens() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();

    List<String> wsTokens = wsTokenService.getWsTokens();
    log.info("size: {}", wsTokens.size());
    wsTokens.forEach(wsToken -> log.info("wsToken : {}", wsToken));

  }

  @Test
  public void testDeleteWsToken() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();
    List<String> wsTokens = wsTokenService.getWsTokens();
    log.info("before Tokens : {} ", wsTokens.size());
    wsTokenService.deleteWsToken(wsTokens.stream().findFirst().get());
    log.info("after Tokens : {} ", wsTokenService.getWsTokens().size());
  }

  @Test
  public void testDeleteAll() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();
    List<String> wsTokens = wsTokenService.getWsTokens();
    wsTokens.forEach(wsToken -> {
      try {
        wsTokenService.deleteWsToken(wsToken);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }


  @Test
  public void testPutWsToken() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();
    WebSocketToken wsToken = wsTokenService.getWsToken();
    log.info("wsToken : {}", wsToken.getListenKey());

    WebSocketToken webSocketToken = wsTokenService.putWsToken(wsToken.getListenKey());
    log.info("refresh Token:{}", webSocketToken.getListenKey());

  }

  @Test
  public void testTime() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCWsTokenService wsTokenService = exchange.getWsTokenService();
    log.info("wsToken : {}", wsTokenService.time());
  }

  @Test
  public void testAccount() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCAccountService accountService = (MEXCAccountService) exchange.getAccountService();
    log.info("{}", accountService.getAccountInfo());
  }

}
