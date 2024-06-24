package org.knowm.xchange.xt;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.marketdata.params.InstrumentsParams;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParamInstrument;
import org.knowm.xchange.xt.dto.account.BalanceResponse;
import org.knowm.xchange.xt.dto.account.XTFundingHistoryParams;
import org.knowm.xchange.xt.dto.account.XTWithdrawFundsParams;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyInfo;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.service.XTAccountServiceRaw;
import org.knowm.xchange.xt.service.XTMarketDataService;

@Slf4j
public class XTExchangeIntegration extends XTExchangeBase {

  @Test
  public void testBalance() throws IOException {
    XTAccountServiceRaw xtAccountServiceRaw = exchange.accountServiceRaw();
    BalanceResponse usdt = xtAccountServiceRaw.balance("usdt");
    log.info("usdt:{}", usdt.getAvailableAmount());

  }


  @Test
  public void testBalances() throws IOException {
    AccountInfo accountInfo = exchange.getAccountService().getAccountInfo();
    log.info("{}", accountInfo.toString());
  }


  @Test
  public void testTime() throws IOException {
    XTAccountServiceRaw xtAccountServiceRaw = exchange.accountServiceRaw();
    log.info("{}", xtAccountServiceRaw.time());
  }

  @Test
  public void testMarketMetadataCurrency() throws IOException {
    XTMarketDataService marketDataService = (XTMarketDataService) exchange.getMarketDataService();
    List<XTCurrencyWalletInfo> currencys = marketDataService.getWalletSupportCurrencys();

    log.info("Currencys size:{} ", currencys.size());
    Multiset<String> currcount = HashMultiset.create();
    for (XTCurrencyWalletInfo currency : currencys) {
      log.info("{}", currency);
      currcount.add(currency.getCurrency());
    }

    //BNB Smart Chain

    log.info("currcount:{}", currcount.size());
    log.info("============================");
    List<XTCurrencyInfo> currencyInfos = marketDataService.getCurrencyInfos().stream()
        .filter(xtCurrencyInfo -> xtCurrencyInfo.getDepositStatus()
            .equals("1") && xtCurrencyInfo.getWithdrawStatus()
            .equals("1"))
        .collect(ImmutableList.toImmutableList());
    log.info("currencyInfos size:{} ", currencyInfos.size());
    for (XTCurrencyInfo currencyInfo : currencyInfos) {
      log.info("{}", currencyInfo);
    }

    List<XTSymbol> symbols = marketDataService.getSymbols(null, null);
    log.info("Symbols size:{} ", symbols.size());
    for (XTSymbol symbol : symbols) {
      log.info("{}", symbol);
    }

  }


  @Test
  public void testTicker() throws IOException {
    MarketDataService marketDataService = exchange.getMarketDataService();
    Instrument instrument = new CurrencyPair("BTC", "USDT");
    Ticker ticker = marketDataService.getTicker(instrument);
    log.info("{}", ticker.toString());

    Instrument instrument1 = new CurrencyPair("ETH", "USDT");

    List<Ticker> tickers = marketDataService.getTickers(
        (InstrumentsParams) () -> Lists.newArrayList(instrument, instrument1));

    log.info("TICKERS:{}", tickers);


  }


  @Test
  public void filterBNB() {
    XTMarketDataService marketDataService = (XTMarketDataService) exchange.getMarketDataService();
    List<XTCurrencyWalletInfo> currencys = marketDataService.getWalletSupportCurrencys();
    List<XTSymbol> symbols1 = marketDataService.getSymbols(null, null).stream()
        .filter(xt -> xt.getState().equalsIgnoreCase("online"))
        .collect(Collectors.toList());
    ImmutableList<XTCurrencyWalletInfo> collect = currencys.stream()
        .filter(xtCurrencyWalletInfo -> xtCurrencyWalletInfo.getSupportChains()
            .stream()
            .anyMatch(supportChain -> supportChain.getChain()
                .startsWith("BNB Smart Chain")
                && supportChain.isDepositEnabled()
                && supportChain.isWithdrawEnabled()))

        .collect(ImmutableList.toImmutableList());

    log.info("BNB size:{}", collect.size());

    List<String> symbols = collect.stream().map(XTCurrencyWalletInfo::getCurrency)
        .collect(Collectors.toList());

    log.info("BNB symbols:{}", symbols);
    for (XTCurrencyWalletInfo xtCurrencyWalletInfo : collect) {
      log.info("currency Info :{}", xtCurrencyWalletInfo);
    }
  }

  @Test
  public void testExchangeMetadata() throws IOException {
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    log.info("exchangeMetaData size:{},currencyPairs size:{} ,instrument size:{}",
        exchangeMetaData.getCurrencies().size(),
        exchangeMetaData.getCurrencies().size(),
        exchangeMetaData.getInstruments().size());

    exchangeMetaData.getInstruments().forEach((instrument, instrumentMetaData) -> {
      log.info("instrument:{} ,instrumentMetaData:{}", instrument, instrumentMetaData);
    });


  }

  @Test
  public void testFundingHistory() throws IOException {
    List<FundingRecord> fundingHistory = exchange.getAccountService()
        .getFundingHistory(XTFundingHistoryParams.builder()
            .build());
    log.info("{}", fundingHistory.toString());

  }

  @Test
  public void placeLimitBuyOrder() throws IOException {
    LimitOrder limitOrder = new LimitOrder.Builder(Order.OrderType.BID,
        new CurrencyPair("XTZ", "USDT"))
        .limitPrice(new BigDecimal("0.75"))
        .originalAmount(new BigDecimal("100"))
        .build();
    exchange.getTradeService()
        .placeLimitOrder(limitOrder);
  }

  @Test
  public void getOrder() throws IOException {
    Collection<Order> orders = exchange.getTradeService()
        .getOrder("249401475713759872");

    //248956193088058240
    log.info("orders:{}", orders);
  }

  @Test
  public void cancelOrder() throws IOException {
    boolean result = exchange.getTradeService()
        .cancelOrder(String.valueOf(248956193088058240L));
    // 处理撤单结果
  }

  @Test
  public void getOpenOrders() throws IOException {
    OpenOrders openOrders = exchange.getTradeService()
        .getOpenOrders(new DefaultOpenOrdersParamInstrument(new CurrencyPair("XTZ/USDT")));

    log.info("XTZ_USDT openOrders:{}", openOrders.getOpenOrders().size());

    for (LimitOrder openOrder : openOrders.getOpenOrders()) {
      log.info("XTZ_USDT openOrder:{}", openOrder);
    }

    log.info("==================================");
    openOrders = exchange.getTradeService().getOpenOrders();
    log.info("openOrders:{}", openOrders.getOpenOrders().size());
    for (LimitOrder openOrder : openOrders.getOpenOrders()) {
      log.info("openOrder:{}", openOrder);
    }


  }

  @Test
  public void withdraw() throws IOException {
    String result = exchange.getAccountService()
        .withdrawFunds(
            new XTWithdrawFundsParams("0x7D57C886F058413783ab19DE1165ec736BD88e3a", Currency.USDT,
                new BigDecimal("14"), "BNB Smart Chain"));
    log.info("result:{}", result);
  }


}

