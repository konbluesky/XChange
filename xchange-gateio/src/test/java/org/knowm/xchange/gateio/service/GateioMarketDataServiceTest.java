package org.knowm.xchange.gateio.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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
import org.knowm.xchange.gateio.dto.marketdata.GateioCoinNetwork;
import org.knowm.xchange.instrument.Instrument;
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


  /**
   * 打印币种和chain信息，统计chain数量
   *
   * @throws IOException
   */
  @Test
  public void testGetChain() throws IOException {
    GateioMarketDataServiceRaw marketDataService = (GateioMarketDataServiceRaw) exchange.getMarketDataService();
    Map<String, GateioCoin> coins = marketDataService.getGateioCoinInfo();
    //根据key排序输出
    List<String> sortKey = coins.keySet().stream().sorted().collect(Collectors.toList());
    Multiset<String> chainCount = HashMultiset.create();
    Multimap<String, String> chainCoinMap = HashMultimap.create();
    Multimap<String, String> coinChainMap = HashMultimap.create();
    log.info("coin size:{}", coins.size());
    sortKey.forEach(k -> {
//      System.out.println(k + " : " + coins.get(k).getChain());
      chainCount.add(coins.get(k).getChain());
      chainCoinMap.put(coins.get(k).getChain(), k);
      //去掉币种名称后面的_xxxx
      coinChainMap.put(k, coins.get(k).getChain());
    });
    log.info("chain size:{}", chainCount.size());
    //遍历chainCount，打印每个chain有多少币种
    //打印大于10的chain
    for (Entry<String> entry : chainCount.entrySet()) {
      if (entry.getCount() > 10) {
        log.info("more than the 10 chain:{} size:{}", entry.getElement(), entry.getCount());
      }
    }
    log.info("=====================");
    //打印每个chain的币种
    for (Map.Entry<String, Collection<String>> entry : chainCoinMap.asMap()
        .entrySet()) {
      log.info("chain:{} coin:{}", entry.getKey(), entry.getValue());
    }
    log.info("+++++++++++++++++++++++++++++++++");
    //打印每个币种的链
    for (Map.Entry<String, Collection<String>> entry : chainCoinMap.asMap()
        .entrySet()) {
      log.info("coin:{} chain:[]", entry.getKey(), entry.getValue());
    }


  }

  @Test
  public void getWithdrawFee() throws IOException{
    exchange.remoteInit();
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    log.info("exchangeMetaData currencies size: {}", exchangeMetaData.getCurrencies().size());

  }

  @Test
  public void testGetContractAddress() throws IOException {
    GateioMarketDataServiceRaw marketDataService = (GateioMarketDataServiceRaw) exchange.getMarketDataService();
    List<GateioCoinNetwork> coinNetworkBy = marketDataService.getCoinNetworkBy("SXP");
    //格式化输出  chain: chainname contract: contractaddress
    coinNetworkBy.forEach(coinNetwork -> {
      log.info("chain:{} contract:{}", coinNetwork.getChain(), coinNetwork.getContractAddress());
    });

  }


  @Test
  public void testSingleTicker() throws IOException {
    MarketDataService marketDataService = exchange.getMarketDataService();
    Instrument instrument = new CurrencyPair("trvl","busd");
    Ticker ticker = marketDataService.getTicker(instrument);
    log.info("ticker:{}",ticker);
  }

  @Test
  public void testAllTicker() throws IOException {
    MarketDataService marketDataService = exchange.getMarketDataService();
    List<Ticker> tickers = marketDataService.getTickers(null);
    log.info("size:{}", tickers.size());
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
