package info.bitrich.xchangestream.kucoin;

import info.bitrich.xchangestream.core.ProductSubscription;
import java.util.Currency;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;

/**
 * <p> @Date : 2024/9/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class KucoinStreamingExchangeTest extends BaseKucoinStreaming {
  Instrument instrument = new CurrencyPair("BTC/USDT");

  @Test
  public void testOrderBook() throws InterruptedException {
    streamingExchange
        .connect(
            ProductSubscription.create()
                .addOrderbook(instrument)
                .addTicker(instrument)
                .addTrades(instrument)
                .build())
        .blockingAwait();
    streamingExchange.getStreamingMarketDataService().getOrderBook(instrument).subscribe(
      orderBook -> {
        log.info("orderboork : {}", orderBook);
      }
    );
    TimeUnit.MINUTES.sleep(10);
  }


  @Test
  public void testTicker() throws InterruptedException {
    streamingExchange
        .connect(
            ProductSubscription.create()
                .addOrderbook(instrument)
                .addTicker(instrument)
                .addTrades(instrument)
                .build())
        .blockingAwait();
    streamingExchange.getStreamingMarketDataService().getTicker(instrument).subscribe(
        orderBook -> {
          log.info("ticker : {}", orderBook);
        }
    );
    TimeUnit.MINUTES.sleep(10);
  }


  @Test
  public void testAccount() throws InterruptedException {
    instrument = new CurrencyPair("SUNDOG/USDT");
    streamingExchange
        .connect(
            ProductSubscription.create()
                .addOrders(instrument)
//                .addOrderbook(instrument)
//                .addTicker(instrument)
//                .addTrades(instrument)
                .build())
        .blockingAwait();
    streamingExchange.getStreamingAccountService().getBalanceChanges(null).subscribe(
        orderBook -> {
          log.info("order change : {}", orderBook);
        }
    );
    TimeUnit.MINUTES.sleep(10);
  }


}