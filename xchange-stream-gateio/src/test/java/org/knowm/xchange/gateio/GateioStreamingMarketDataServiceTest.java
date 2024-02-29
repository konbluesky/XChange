package org.knowm.xchange.gateio;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioStreamingMarketDataServiceTest extends GateioBase {

  @Test
  public void testGetOrderBook() throws InterruptedException {
    streamingExchange.getStreamingMarketDataService()
        .getOrderBook(CurrencyPair.BTC_USDT, "EJS_USDT", 20, "1000ms")
        .subscribe(orderBook -> {
          log.info("深度数据:{}", orderBook);
        });
    TimeUnit.MINUTES.sleep(5);
  }


  @Test
  public void testGetTicker() throws InterruptedException {
    streamingExchange.getStreamingMarketDataService()
//        .getTicker(CurrencyPair.BTC_USDT)
        .getTicker(new CurrencyPair("TRVL","usdt"))
        .subscribe(ticker -> {
          log.info("价格变化:{}", ticker);
        });
    TimeUnit.MINUTES.sleep(5);
  }
}