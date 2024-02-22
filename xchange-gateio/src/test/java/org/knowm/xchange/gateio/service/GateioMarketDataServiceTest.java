package org.knowm.xchange.gateio.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.gateio.GateioExchangeWiremock;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoin;
import org.knowm.xchange.service.marketdata.MarketDataService;

@Slf4j
public class GateioMarketDataServiceTest extends GateioExchangeWiremock {

  GateioMarketDataService gateioMarketDataService =
      (GateioMarketDataService) exchange.getMarketDataService();

  @Test
  public void valid_remote_orderbook() throws IOException {
    OrderBook orderBook = exchange.getMarketDataService().getOrderBook(CurrencyPair.BTC_USDT);

    System.out.println(orderBook.getAsks());
  }

  @Test
  public void valid_orderbook() throws IOException {
    OrderBook actual = gateioMarketDataService.getOrderBook(CurrencyPair.BTC_USDT);

    List<LimitOrder> expectedAsks = new ArrayList<>();
    expectedAsks.add(
        new LimitOrder.Builder(OrderType.ASK, CurrencyPair.BTC_USDT)
            .id("")
            .limitPrice(new BigDecimal("200"))
            .originalAmount(BigDecimal.ONE)
            .build());
    expectedAsks.add(
        new LimitOrder.Builder(OrderType.ASK, CurrencyPair.BTC_USDT)
            .id("")
            .limitPrice(new BigDecimal("250"))
            .originalAmount(BigDecimal.TEN)
            .build());

    List<LimitOrder> expectedBids = new ArrayList<>();
    expectedBids.add(
        new LimitOrder.Builder(OrderType.BID, CurrencyPair.BTC_USDT)
            .id("")
            .limitPrice(new BigDecimal("150"))
            .originalAmount(BigDecimal.ONE)
            .build());
    expectedBids.add(
        new LimitOrder.Builder(OrderType.BID, CurrencyPair.BTC_USDT)
            .id("")
            .limitPrice(new BigDecimal("100"))
            .originalAmount(BigDecimal.TEN)
            .build());
    //    Date expectedTimestamp = Date.from(Instant.parse("2023-05-14T22:10:10.493Z"));

    OrderBook expected = new OrderBook(null, expectedAsks, expectedBids);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFieldsMatchingRegexes(".*userReference")
        .isEqualTo(expected);
  }


  @Test
  public void testMarketData() throws IOException {
    exchange.remoteInit();
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    log.info("exchangeMetaData currencies size: {}", exchangeMetaData.getCurrencies().size());
    log.info("exchangeMetaData instrument size: {}", exchangeMetaData.getInstruments().size());
  }


  @Test
  public void testGetChain() throws IOException {
    GateioMarketDataServiceRaw marketDataService = (GateioMarketDataServiceRaw) exchange.getMarketDataService();
    Map<String, GateioCoin> coins = marketDataService.getGateioCoinInfo();
    coins.forEach((k, v) -> {
      if (v.getChain() != null) {
        System.out.println(k + " : " + v.getChain());
      }
    });
  }

  @Test
  public void testTicker() throws IOException {
    MarketDataService marketDataService = exchange.getMarketDataService();
    List<Ticker> tickers = marketDataService.getTickers(null);
    log.info("size:{}",tickers.size());
    log.info("content:{}", tickers.get(0));
  }


  @Test
  public void coin_info() throws IOException {
    GateioMarketDataServiceRaw marketDataService = (GateioMarketDataServiceRaw) exchange.getMarketDataService();
    Map<String, GateioCoin> coins = marketDataService.getGateioCoinInfo();

    //打印10个gateioCoin
    log.info("coin size: {}", coins.size());
    AtomicInteger count = new AtomicInteger(0);
    coins.forEach((k, v) -> {
      if (v.getFixedRate() != null) {

      } else {
//        if (count.get() < 10) {
        System.out.println(k + " : " + v);
        count.getAndIncrement();
//        }
      }
    });
  }

}
