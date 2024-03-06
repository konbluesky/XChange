package org.knowm.xchange.gateio.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gateio.GateioExchangeWiremock;
import org.knowm.xchange.gateio.dto.marketdata.GateioMarketInfoWrapper;
import org.knowm.xchange.gateio.dto.marketdata.GateioMarketInfoWrapper.GateioMarketInfo;
import org.knowm.xchange.gateio.dto.marketdata.GateioPair;

@Slf4j
public class GateioMarketDataServiceRawTest extends GateioExchangeWiremock {

  GateioMarketDataServiceRaw gateioMarketDataServiceRaw =
      (GateioMarketDataServiceRaw) exchange.getMarketDataService();

  @Test
  public void valid_marketinfo() throws IOException {
    Map<CurrencyPair, GateioPair> gateioMarketInfo = gateioMarketDataServiceRaw.getGateioMarketInfo();
    System.out.println(gateioMarketInfo);
  }

  @Test
  public void testServerTime() throws IOException{
    long l = gateioMarketDataServiceRaw.serverTime();
    log.info("Server Time:{}", l);

  }

}
