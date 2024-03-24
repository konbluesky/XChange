package info.bitrich.xchangestream.okex;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.DateUtils;

@Slf4j
public class OkexStreamingPublicDataIntegration {

  private final Instrument currencyPair = CurrencyPair.BTC_USDT;
  private final Instrument instrument = new FuturesContract("BTC/USDT/SWAP");
  private StreamingExchange exchange;

  @Before
  public void setUp() {
    exchange = StreamingExchangeFactory.INSTANCE.createExchange(OkexStreamingExchange.class);
    exchange.connect().blockingAwait();
  }

  @Test
  public void testTrades() throws InterruptedException {
    Disposable dis =
        exchange
            .getStreamingMarketDataService()
            .getTrades(currencyPair)
            .subscribe(
                trade -> {
                  log.info("trades:{}", trade);
                  assertThat(trade.getInstrument()).isEqualTo(currencyPair);
                });
    Disposable dis2 =
        exchange
            .getStreamingMarketDataService()
            .getTrades(instrument)
            .subscribe(
                trade -> {
                  log.info("trades:{}", trade);
                  assertThat(trade.getInstrument()).isEqualTo(instrument);
                });
    TimeUnit.SECONDS.sleep(3);
    dis.dispose();
    dis2.dispose();
  }

  @Test
  public void testTicker() throws InterruptedException {
    Disposable dis =
        exchange
            .getStreamingMarketDataService()
            .getTicker(currencyPair)
            .subscribe(System.out::println);
    Disposable dis2 =
        exchange
            .getStreamingMarketDataService()
            .getTicker(instrument)
            .subscribe(System.out::println);
    TimeUnit.SECONDS.sleep(3);
    dis.dispose();
    dis2.dispose();
  }

  @Test
  public void testFundingRateStream() throws InterruptedException {
    Disposable dis =
        exchange
            .getStreamingMarketDataService()
            .getFundingRate(instrument)
            .subscribe(System.out::println);
    TimeUnit.SECONDS.sleep(3);
    dis.dispose();
  }

  @Test
  public void testOrderBook() throws InterruptedException {
    Disposable dis =
        exchange
            .getStreamingMarketDataService()
            .getOrderBook(currencyPair)
            .subscribe(
                orderBook -> {

                  log.info("orderbook1:{}", DateUtils.toUTCISODateString(orderBook.getTimeStamp()));
                  assertThat(orderBook.getBids().get(0).getLimitPrice())
                      .isLessThan(orderBook.getAsks().get(0).getLimitPrice());
                  assertThat(orderBook.getBids().get(0).getInstrument()).isEqualTo(currencyPair);
                });
    Disposable dis2 =
        exchange
            .getStreamingMarketDataService()
            .getOrderBook(instrument)
            .subscribe(
                orderBook -> {
                  log.info("orderbook2:{}", DateUtils.toUTCISODateString(orderBook.getTimeStamp()));
                  assertThat(orderBook.getBids().get(0).getLimitPrice())
                      .isLessThan(orderBook.getAsks().get(0).getLimitPrice());
                  assertThat(orderBook.getBids().get(0).getInstrument()).isEqualTo(instrument);
                });
    TimeUnit.MINUTES.sleep(3);
    dis.dispose();
    dis2.dispose();
  }
}
