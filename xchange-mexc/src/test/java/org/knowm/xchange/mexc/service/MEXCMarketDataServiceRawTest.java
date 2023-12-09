package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfo;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfoSymbol;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * <p> @Date : 2023/8/30 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCMarketDataServiceRawTest extends BaseWiremockTest {

  @Test
  public void testGetApiSupportSymbols() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<String> supportApiSymbols = marketDataService.getSupportApiSymbols();
    log.info("size:{}", supportApiSymbols.size());
    for (String supportApiSymbol : supportApiSymbols) {
      log.info("symbol: {}, currPair:{} ", supportApiSymbol,
          MEXCAdapters.extractCurrencyPairs(supportApiSymbol));
//      log.info("price:{}",marketDataService.getTickerPair(supportApiSymbol));
    }
  }


  @Test
  public void testGetTickerPrice() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<MEXCPricePair> tickers = marketDataService.getTickersPair();
    for (MEXCPricePair ticker : tickers) {
      log.info("ticker: {} ", ticker);
    }
  }

  @Test
  public void testExchange() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    MEXCExchangeInfo exchangeInfo = marketDataService.getExchangeInfo();
    log.info("exchange symbol size:{}", exchangeInfo.getSymbols().size());
    // network = BEP20(BSC)
    for (MEXCExchangeInfoSymbol symbol : exchangeInfo.getSymbols()) {
      log.info("symbol config: {} ", symbol.toString());
      log.info("order types:{} ", symbol.getOrderTypes());
    }
  }

  @Test
  public void testExchangeMetaData() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    log.info("instrument size: {} ", exchangeMetaData.getInstruments().size());
    log.info("currency size: {} ", exchangeMetaData.getCurrencies().size());
  }


  @Test
  public void testGetAll() throws IOException {
    String bscIdentity = "BEP20(BSC)";
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<MEXCConfig> all = marketDataService.getAll();
//    Map<Currency, MEXCConfig> collect = all.stream().filter(c -> c.getNetworkList().stream()
//        .anyMatch(n -> n.getNetwork().equalsIgnoreCase(bscIdentity))).collect(
//        Collectors.toMap(c -> Currency.getInstance(c.getCoin()), c -> c));
//
//    log.info("size:{}",collect.keySet().size());
//    collect.forEach((k,v)->{
//      log.info("coin:{} ,{}",k,v);
//    });

    // network = BEP20(BSC)
    for (MEXCConfig config : all) {

      boolean present = config.getNetworkList().stream()
          .filter(n -> n.getNetwork().equalsIgnoreCase(bscIdentity.toLowerCase())).findAny()
          .isPresent();
      if (present) {
        log.info("coin  config : {} ", config);
        log.info("coin NetWork size:{} ", config.getNetworkList().size());
        for (MEXCNetwork mexcNetwork : config.getNetworkList()) {
          log.info("coin  network : {} ", mexcNetwork.toString());
        }
      }
    }
  }


  @Test
  public void testGetAllPrintNetwork() throws IOException {
    String bscIdentity = MEXCNetwork.NETWORK_BSC2;
    String arbIdentity = MEXCNetwork.NETWORK_ARB;
    String targetIdentity = bscIdentity;
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<MEXCConfig> all = marketDataService.getAll();
//    Map<Currency, MEXCConfig> collect = all.stream().filter(c -> c.getNetworkList().stream()
//        .anyMatch(n -> n.getNetwork().equalsIgnoreCase(bscIdentity))).collect(
//        Collectors.toMap(c -> Currency.getInstance(c.getCoin()), c -> c));
//
//    log.info("size:{}",collect.keySet().size());
//    collect.forEach((k,v)->{
//      log.info("coin:{} ,{}",k,v);
//    });

    // network = BEP20(BSC)
    for (MEXCConfig config : all) {

      boolean present = config.getNetworkList().stream()
          .filter(n -> n.getNetwork().equalsIgnoreCase(targetIdentity.toLowerCase())).findAny()
          .isPresent();
      if (present) {
        log.info("coin  config : {} ", config);
        log.info("coin NetWork size:{} ", config.getNetworkList().size());
        for (MEXCNetwork mexcNetwork : config.getNetworkList()) {
          log.info("coin  network : {} ", mexcNetwork.toString());
        }
      }
    }
  }

  @Test
  public void testOnlineOffline() throws IOException {

    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MarketDataService marketDataService = exchange.getMarketDataService();
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    CurrencyMetaData currencyMetaData = exchangeMetaData.getCurrencies()
        .get(Currency.getInstance("dis"));
    log.info("{}",currencyMetaData);


  }


}