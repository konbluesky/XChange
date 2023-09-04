package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;

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
  public void testGetAll() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<MEXCConfig> all = marketDataService.getAll();
    //network = BEP20(BSC)
    for (MEXCConfig config : all) {
      log.info("coin  config : {} ", config.toString());
      log.info("coin NetWork size:{} ",config.getNetworkList().size());
      for (MEXCNetwork mexcNetwork : config.getNetworkList()) {
        log.info("coin  network : {} ", mexcNetwork.toString());
      }
    }
  }


}