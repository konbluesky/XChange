package org.knowm.xchange.hashkey;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.AuthUtils;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeytreamingExchangeTest {

  // Enter your authentication details here to run private endpoint tests
  private static final String API_KEY = "jTHlkpOXjm2xbpGs8sNYg89C34sRcQmAMX1FwlbxUFzJgkd8pg5ksxmi2QXkkMeD";
  private static final String SECRET_KEY = "3BeaidqsT6EJDEHfYorPPSag2dyJutTkHSgpahJvF2IR27PGUwR0OIG3Qww9Qm8Q";
  StreamingExchange streamingExchange;
  private Instrument instrument = new CurrencyPair("ETH","HDK");

  @Before
  public void setUp() throws IOException {
    ExchangeSpecification spec = new HashKeyExchange().getDefaultExchangeSpecification();
    AuthUtils.setApiAndSecretKey(spec);
    ExchangeSpecification defaultExchangeSpecification = new HashKeyStreamingExchange().getDefaultExchangeSpecification();
    AuthUtils.setApiAndSecretKey(defaultExchangeSpecification);
    streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(
        defaultExchangeSpecification);
    streamingExchange.connect().blockingAwait();
  }

  @Test
  public void testPublic() throws InterruptedException {
    streamingExchange = (HashKeyStreamingExchange) streamingExchange;
    instrument = new CurrencyPair("btc", "usdt");
    Disposable dis = streamingExchange.getStreamingMarketDataService()
        .getOrderBook(instrument)
        .subscribe(orderBook -> {
          log.info("sub: {}", orderBook.toString());
        },throwable -> {
          log.error("error : {}", throwable.getMessage());
        });
    TimeUnit.SECONDS.sleep(60);

    dis.dispose();
  }


  @Test
  public void testPrivate() throws IOException {
    instrument = new CurrencyPair("btc", "usdt");
    Disposable dis = streamingExchange.getStreamingTradeService()
        .getOrderChanges(instrument, null)
        .subscribe(order -> {
          log.info("order : {}", order.toString());
        }, throwable -> {
          log.error("error : {}", throwable.getMessage());
        });

    try {
      TimeUnit.MINUTES.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    dis.dispose();

  }

  @Test
  public void testBalance() throws IOException {
    Disposable dis = streamingExchange.getStreamingAccountService()
        .getBalanceChanges(null)
        .subscribe(balance -> {
          log.info("balance : {}", balance.toString());
        });

    try {
      TimeUnit.MINUTES.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    dis.dispose();
  }


}
