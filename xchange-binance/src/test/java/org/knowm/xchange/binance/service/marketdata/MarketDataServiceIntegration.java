package org.knowm.xchange.binance.service.marketdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.binance.BinanceExchangeIntegration;
import org.knowm.xchange.binance.dto.account.WithdrawResponse;
import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;
import org.knowm.xchange.binance.dto.meta.BinanceConfig;
import org.knowm.xchange.binance.service.BinanceAccountServiceRaw;
import org.knowm.xchange.binance.service.BinanceMarketDataService;
import org.knowm.xchange.binance.service.BinanceMarketDataServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;

@Slf4j
public class MarketDataServiceIntegration extends BinanceExchangeIntegration {

  static MarketDataService marketService;

  @BeforeClass
  public static void beforeClass() throws Exception {
    createExchange();
    marketService = exchange.getMarketDataService();
  }

  @Before
  public void before() {
    Assume.assumeNotNull(exchange.getExchangeSpecification().getApiKey());
  }

  @Test
  public void testTimestamp() {

    long serverTime = exchange.getTimestampFactory().createValue();
    Assert.assertTrue(0 < serverTime);
  }

  @Test
  public void testBinanceTicker24h() throws Exception {

    List<BinanceTicker24h> tickers = new ArrayList<>();
    for (Instrument cp : exchange.getExchangeMetaData().getInstruments().keySet()) {
      if (cp.getCounter() == Currency.USDT) {
        tickers.add(getBinanceTicker24h(cp));
      }
    }

    tickers.sort((BinanceTicker24h t1, BinanceTicker24h t2) ->
        t2.getPriceChangePercent().compareTo(t1.getPriceChangePercent()));

    tickers
        .forEach(
            t -> System.out.println(
                t.getCurrencyPair()
                    + " => "
                    + String.format("%+.2f%%", t.getPriceChangePercent())));
  }

  @Test
  public void testGetAllCoinConfig() throws Exception {
    BinanceMarketDataServiceRaw service = (BinanceMarketDataServiceRaw) marketService;
    List<BinanceConfig> allCoinConfig = service.getAllCoinConfig();

    log.info("Config Size : {}", allCoinConfig.size());
    allCoinConfig.forEach(config -> {
      log.info("Config : {}", config);
      config.getNetworkList().stream()
          .filter(n->n.getNetwork().equalsIgnoreCase("bsc")||n.getNetwork().equalsIgnoreCase("bnb"))
          .forEach(n -> {
            log.info("Network : {}", n);
          });
    });
  }

  @Test
  public void testWithdraw1() throws Exception {
    BinanceAccountServiceRaw accountService = (BinanceAccountServiceRaw) exchange.getAccountService();
    WithdrawResponse withdraw = accountService.withdraw("USDT",
        "", null, new BigDecimal(10), "BSC");
    log.info("{}",withdraw.getId());

  }

  @Test
  public void testWithdraw2() throws Exception {
    BinanceAccountServiceRaw accountService = (BinanceAccountServiceRaw) exchange.getAccountService();
    WithdrawResponse withdraw = accountService.withdraw("USDT",
        "", "swap", new BigDecimal(10), "BSC");
    log.info("{}",withdraw.getId());

  }

  private BinanceTicker24h getBinanceTicker24h(Instrument pair) throws IOException {
    BinanceMarketDataService service = (BinanceMarketDataService) marketService;
    return service.ticker24hAllProducts(pair);
  }
}
