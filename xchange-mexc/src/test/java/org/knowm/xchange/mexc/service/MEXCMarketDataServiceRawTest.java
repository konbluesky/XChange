package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import org.knowm.xchange.mexc.dto.market.MEXCCurrencyNetworkMapping;
import org.knowm.xchange.mexc.dto.market.MEXCCurrencyMetaData;
import org.knowm.xchange.mexc.dto.market.MEXCExchangeMetaData;
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
  public void testShowIsSpotTradingAllowed() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    MEXCExchangeInfo exchangeInfo = marketDataService.getExchangeInfo();
    log.info("exchange symbol size:{}", exchangeInfo.getSymbols().size());
    List<MEXCExchangeInfoSymbol> isSpotTradingAllowedList= exchangeInfo.getSymbols().stream()
        .filter(f -> f.isSpotTradingAllowed())
        .filter(f->f.getOrderTypes().containsAll(
            Arrays.asList("MARKET", "LIMIT_MAKER", "LIMIT")))
        .filter(f->f.getStatus().equalsIgnoreCase("ENABLED")).collect(Collectors.toList());

    log.info("isSpotTradingAllowed size:{}", isSpotTradingAllowedList.size());
    // isSpotTradingAllowed size:1028

    MEXCExchangeMetaData exchangeMetaData = (MEXCExchangeMetaData) exchange.getExchangeMetaData();
    // network = BEP20(BSC)
    for (MEXCExchangeInfoSymbol symbol : isSpotTradingAllowedList) {
      log.info("symbol config: {} ", symbol.toString());
      log.info("order types:{} ", symbol.getOrderTypes());
    }
  }




  @Test
  public void testExchangeMetaData() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCExchangeMetaData exchangeMetaData = (MEXCExchangeMetaData) exchange.getExchangeMetaData();
    log.info("instrument size: {} ", exchangeMetaData.getInstruments().size());
    log.info("currency size: {} ", exchangeMetaData.getCurrencies().size());
    MEXCCurrencyNetworkMapping currencyNetworkMapping = new MEXCCurrencyNetworkMapping(
        exchangeMetaData.getCurrencyNetwork());

    Collection<String> currenciesForNetworkBSC = currencyNetworkMapping.getCurrenciesForNetwork(
        MEXCNetwork.NETWORK_BSC2);
    log.info(" BNB Smart Chain(BEP20) size:  {}", currenciesForNetworkBSC.size());
    log.info(" \t BNB content:  {}", currenciesForNetworkBSC);
    Collection<String> currenciesForNetworkARB = currencyNetworkMapping.getCurrenciesForNetwork(
        MEXCNetwork.NETWORK_ARB);
    log.info(" ARB  size:  {}", currenciesForNetworkARB.size());
    log.info(" \t ARB content:  {}", currenciesForNetworkARB);
  }

  @Test
  public void testGetAllContractAddress() throws IOException {
    String bscIdentity = MEXCNetwork.NETWORK_BSC2;
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();

    //过滤可以api交易的
    MEXCExchangeInfo exchangeInfo = marketDataService.getExchangeInfo();
    log.info("exchange symbol size:{}", exchangeInfo.getSymbols().size());
    List<MEXCExchangeInfoSymbol> isSpotTradingAllowedList = exchangeInfo.getSymbols().stream()
        .filter(f -> f.isSpotTradingAllowed())
        .filter(f->f.getOrderTypes().containsAll(
            Arrays.asList("MARKET", "LIMIT_MAKER", "LIMIT")))
        .filter(f->f.getStatus().equalsIgnoreCase("ENABLED")).collect(Collectors.toList());

    //isSpotTradingAllowedList 转成map
    Map<String, MEXCExchangeInfoSymbol> symbolMap = isSpotTradingAllowedList.stream()
       .collect(Collectors.toMap(
           symbol -> MEXCAdapters.extractOneCurrencyPairs(symbol.getSymbol()).getBase().getCurrencyCode(),
           symbol -> symbol,
           (existing, replacement) -> existing));

    List<MEXCConfig> all = marketDataService.getAll();
    // network = BEP20(BSC)
    int count = 0;
    for (MEXCConfig config : all) {

      boolean present = config.getNetworkList().stream()
          .filter(n -> n.getNetwork().equalsIgnoreCase(bscIdentity.toLowerCase())).findAny()
          .isPresent();
      if (present) {
        // log.info("coin  config : {} ,coin NetWork size:{} ", config,config.getNetworkList().size());
        List<MEXCNetwork> mexcNetworks = config.getNetworkList().stream()
            .filter(n -> n.getNetwork().equalsIgnoreCase(bscIdentity.toLowerCase()))
            .filter(n -> n.isDepositEnable() &&  n.isWithdrawEnable())  // both : 506
            // .filter(n -> n.isWithdrawEnable())  //withdraw: 901
            // .filter(n -> n.isDepositEnable()) //deposit: 524
            .collect(Collectors.toList());
        log.info("mexcNetworks : {}",mexcNetworks.size());
        for (MEXCNetwork mexcNetwork : mexcNetworks) {
          log.info("{},{},{}", config.getCoin(), mexcNetwork.getNetwork(),
              mexcNetwork.getContract());
          if(symbolMap.containsKey(config.getCoin())) {
            count++;
          }
          // log.info("coin  network : {} ", mexcNetwork.toString());
        }
      }
    }
    log.info("find coin:{}", count);
    //isSpotTradingAllowed:  261





  }


  @Test
  public void testMEXCCurrencyMetaData() throws IOException {
    String bscIdentity = MEXCNetwork.NETWORK_BSC2;
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCExchangeMetaData exchangeMetaData = (MEXCExchangeMetaData) exchange.getExchangeMetaData();
    Map<Currency, CurrencyMetaData> currencies = exchangeMetaData.getCurrencies();
    log.info("size:{}",currencies.size());
    currencies.forEach((k, v) -> {
          MEXCCurrencyMetaData mexcCurrencyMetaData=(MEXCCurrencyMetaData) v;
          // log.info("{}",mexcCurrencyMetaData.getDefaultNetwork());
      log.info("coin:{}",k);

      if (mexcCurrencyMetaData.getNetworks().size()>1){
        log.info("default:{}",mexcCurrencyMetaData.getDefaultNetwork());
        log.info("two {}",mexcCurrencyMetaData.getNetworksMap());
      }
    });

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
        log.info("\tNetWork size:{} ", config.getNetworkList().size());
        for (MEXCNetwork mexcNetwork : config.getNetworkList()) {
          log.info("\t\tnetwork : {} ", mexcNetwork.toString());
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
    log.info("{}", currencyMetaData);


  }


}