package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTResilience;
import org.knowm.xchange.xt.dto.XTNetwork;
import org.knowm.xchange.xt.dto.XTResponse;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyInfo;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTMarketDataServiceRaw extends XTBaseResilientExchangeService {


  public XTMarketDataServiceRaw(XTExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public List<XTSymbol> getSymbols(String symbol, String currency) {
    try {
      XTResponse<JsonNode> call = decorateApiCall(
          () -> xt.symbol(symbol == null ? "" : symbol, currency == null ? "" : currency, null)
      ).withRateLimiter(rateLimiter(XTResilience.IP_RATE_TYPE)).call();
      JsonNode currencies = safeGetResponse(call);
      return mapper.treeToValue(currencies.get("symbols"),
          mapper.getTypeFactory().constructCollectionType(List.class, XTSymbol.class));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<XTCurrencyWalletInfo> getWalletSupportCurrencies() {
    return safeGetResponse(xt.walletSupportCurrency());
  }

  public Map<String, XTCurrencyWalletInfo> getWalletSupportCurrencysMap(String filterChain) {

    if (Strings.isNullOrEmpty(filterChain)) {
      filterChain = XTNetwork.BNB_SMART_CHAIN;
    }

    List<XTCurrencyWalletInfo> data = getWalletSupportCurrencies();
    //将data转换成map结构，使用currency 作为key，XTCurrencyWalletInfo 作为value
    String finalFilterChain = filterChain;
    Map<String, XTCurrencyWalletInfo> collected = data.stream().filter(
            xtCurrencyWalletInfo -> xtCurrencyWalletInfo.getSupportChains().stream().anyMatch(
                supportChain -> supportChain.getChain().startsWith(finalFilterChain)
                    && supportChain.isDepositEnabled() && supportChain.isWithdrawEnabled()))
        .collect(Collectors.toMap(XTCurrencyWalletInfo::getCurrency, x -> x));
    return collected;
  }


  public List<XTCurrencyInfo> getCurrencyInfos() {
    try {
      JsonNode currencies = decorateApiCall(() -> xt.currencies().getData()).withRateLimiter(
          rateLimiter(XTResilience.IP_RATE_TYPE)).call();
      return mapper.treeToValue(currencies.get("currencies"),
          mapper.getTypeFactory().constructCollectionType(List.class, XTCurrencyInfo.class));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Map<String, XTCurrencyInfo> getCurrencyInfosMap() {
    try {
      List<XTCurrencyInfo> data = getCurrencyInfos();
      //将data转换成map结构，使用currency 作为key，XTCurrencyInfo 作为value
      Map<String, XTCurrencyInfo> collected = data.stream()
          .collect(Collectors.toMap(XTCurrencyInfo::getCurrency, x -> x));
      return collected;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<XTTicker> getTickers(String symbol, String symbols) {
    try {
      return decorateApiCall(
          () -> xt.getFullTickerPrice(symbol, symbols, null).getData()).withRateLimiter(
          rateLimiter(XTResilience.IP_RATE_TYPE)).call();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
