package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;

/**
 * <p> @Date : 2023/12/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCMarketDataServiceTest extends BaseWiremockTest {

  @Test
  public void testTicker24() throws IOException {
    Exchange exchange = createRawExchange();
    List<Ticker> tickers = exchange.getMarketDataService().getTickers(null);
    tickers.forEach(ticker -> log.info("ticker: {}", ticker));

    //计算大于
  }

}