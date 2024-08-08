package org.knowm.xchange;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.AuthUtils;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTStreamingExchange;

/**
 * Unit test for simple App.
 */
@Ignore
@Slf4j
public class XTStreamingExchangePublic extends XTStreamingBase {

  // Enter your authentication details here to run private endpoint tests
  private Instrument instrument = new FuturesContract("ARB/USDT/SWAP");

  @Test
  public void testPrivate() throws InterruptedException {
    XTStreamingExchange xtStreamingExchange = (XTStreamingExchange) streamingExchange;
    instrument = new CurrencyPair("btc", "usdt");
    Disposable dis = xtStreamingExchange.getStreamingMarketDataService()
        .getOrderBook(instrument, 20)
        .subscribe(orderBook -> {
          log.info("sub: {}", orderBook.toString());
        });
    TimeUnit.SECONDS.sleep(30);

    dis.dispose();
  }


  @Test
  public void testDepthOrderBook() throws IOException, InterruptedException {
    XTStreamingExchange xtStreamingExchange = (XTStreamingExchange) streamingExchange;
    StreamingMarketDataService streamingMarketDataService = xtStreamingExchange.getStreamingMarketDataService();
    Disposable dis = streamingMarketDataService.getOrderBook(
        new CurrencyPair(Currency.BTC, Currency.USDT), 5).subscribe(orderBook -> {
      log.info("orderbook: {}", orderBook);
    });
    TimeUnit.SECONDS.sleep(30);
    dis.dispose();
  }


  @Test
  public void testTicker() throws IOException, InterruptedException {
    XTStreamingExchange xtStreamingExchange = (XTStreamingExchange) streamingExchange;
    CurrencyPair currencyPair = new CurrencyPair(Currency.BTC, Currency.USDT);
    Disposable dis = xtStreamingExchange.getStreamingMarketDataService().getTicker(currencyPair)
        .subscribe(ticker -> {
          log.info("ticker - {}:", ticker);

        });
    TimeUnit.SECONDS.sleep(30);
    dis.dispose();

  }


}
