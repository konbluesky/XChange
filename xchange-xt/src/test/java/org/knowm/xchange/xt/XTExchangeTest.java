package org.knowm.xchange.xt;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyChainInfo;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyInfo;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.service.XTMarketDataServiceRaw;

/**
 * <p> @Date : 2024/3/6 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTExchangeTest extends XTExchangeBase {

  @Test
  public void testCurrencyIds() throws IOException {
    XTMarketDataServiceRaw marketDataService = (XTMarketDataServiceRaw) exchange.getMarketDataService();
    List<XTCurrencyInfo> currencyInfos = marketDataService.getCurrencyInfos();
    List<String> lines = Lists.newArrayList();
//    String title="id,currency,fullName,displayName,type,logo,cmcLink,weight,nominalValue,maxPrecision,depositStatus,withdrawStatus,convertEnabled,transferEnabled,isChainExist,plates";
    String title = "id,coin,coin_full_name,network,deposit,withdraw,fee,contract,transfer,cmclink";
    lines.add(title);
    for (XTCurrencyInfo currencyInfo : currencyInfos) {
      if (currencyInfo.getDepositStatus().equalsIgnoreCase("1")
          && currencyInfo.getWithdrawStatus().equalsIgnoreCase("1")
          && currencyInfo.getTransferEnabled().equalsIgnoreCase("1")) {
        lines.add(
            String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                currencyInfo.getId(),
                currencyInfo.getCurrency().toUpperCase(),
                currencyInfo.getFullName(),
                "",
                currencyInfo.getDepositStatus(),
                currencyInfo.getWithdrawStatus(),
                " ",
                " ",
                currencyInfo.getTransferEnabled(),
                currencyInfo.getCmcLink()));
      }
    }
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("xt-coin-ids.csv"));

  }

  @Test
  public void testXtCoinInstrumentCsv() throws IOException {
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    XTMarketDataServiceRaw marketDataService = (XTMarketDataServiceRaw) exchange.getMarketDataService();
    List<XTSymbol> symbols = marketDataService.getSymbols(null, null);
    String title = "pair,baseCurrency,baseCurrencyId,quoteCurrency,quoteCurrencyId";
    List<String> lines = Lists.newArrayList();
    lines.add(title);
    for (XTSymbol symbol : symbols) {
      String line = String.format("%s,%s,%s,%s,%s",
          symbol.getSymbol().toUpperCase(),
          symbol.getBaseCurrency().toUpperCase(),
          symbol.getBaseCurrencyId(),
          symbol.getQuoteCurrency().toUpperCase(),
          symbol.getQuoteCurrencyId());
      lines.add(line);
    }
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("xt-coin-id-map.csv"));
  }


  @Test
  public void newXtCoinCsv() throws IOException {
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    Map<Currency, CurrencyMetaData> currencies = exchangeMetaData.getCurrencies();
    XTMarketDataServiceRaw marketDataService = (XTMarketDataServiceRaw) exchange.getMarketDataService();
    Map<String, XTCurrencyWalletInfo> currencyWalletInfoMap = marketDataService
        .getWalletSupportCurrencysMap(null);

    List<XTSymbol> symbols = marketDataService.getSymbols(null, null);

    log.info("networkMap :{} symbols:{}", currencyWalletInfoMap.size(), symbols.size());
//    for (XTSymbol symbol : symbols) {
//
//      log.info("Symbol : {}",symbol.getSymbol());
//
//    }

//    exchangeMetaData = XTAdapters.adaptToExchangeMetaData(symbols, currencyWalletInfoMap);
//    currencies.forEach((k, v) -> {
//      log.info("{} - {}", k, v);
//    });

    List<String> lines = Lists.newArrayList();
    String title = "coin,coin_full_name,network,deposit,withdraw,fee,contract";
    lines.add(title);
    currencyWalletInfoMap.forEach((k, v) -> {
      for (XTCurrencyChainInfo supportChain : v.getSupportChains()) {
        lines.add(
            String.format("%s,%s,%s,%s,%s,%s,%s", k.toUpperCase(), k.toUpperCase(),
                supportChain.getChain(),
                supportChain.isDepositEnabled(), supportChain.isWithdrawEnabled(),
                supportChain.getWithdrawFeeAmount(),
                ""));
      }
    });
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("xt-coin.csv"));


  }

  @Test
  public void getSymbol() throws IOException{
    XTMarketDataServiceRaw marketDataService = (XTMarketDataServiceRaw) exchange.getMarketDataService();
    List<XTSymbol> symbols = marketDataService.getSymbols(null, null);
    for (XTSymbol symbol : symbols) {
      log.info("{}",symbol);
    }
  }


}