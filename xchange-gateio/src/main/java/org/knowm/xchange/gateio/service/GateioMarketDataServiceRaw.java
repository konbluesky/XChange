package org.knowm.xchange.gateio.service;

import static org.knowm.xchange.gateio.Gateio.PATH_SPOT_TICKERS;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.gateio.Gateio;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.GateioUtils;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawStatus;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoin;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoinNetwork;
import org.knowm.xchange.gateio.dto.marketdata.GateioPair;
import org.knowm.xchange.gateio.dto.marketdata.GateioTicker;
import org.knowm.xchange.instrument.Instrument;

@Slf4j
public class GateioMarketDataServiceRaw extends GateioBaseResilientExchangeService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GateioMarketDataServiceRaw(GateioExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public Map<CurrencyPair, GateioPair> getGateioMarketInfo() throws IOException {
    try {
      List<GateioPair> marketInfo = decorateApiCall(() -> gateio.getMarketInfo()).withRateLimiter(
          rateLimiter(Gateio.PUBLIC_RULE)).call();

      Map<CurrencyPair, GateioPair> result = Maps.newHashMap();
      for (GateioPair gateioPair : marketInfo) {
        result.put(new CurrencyPair(gateioPair.getBase(), gateioPair.getQuote()), gateioPair);
      }
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      handleException(e);
    }
    return null;
  }

  public Map<String, GateioCoin> getGateioCoinInfo() throws IOException {
    try {
      List<GateioCoin> call = decorateApiCall(() -> gateio.getCoinInfo()).withRateLimiter(
          rateLimiter(Gateio.PUBLIC_RULE)).call();
      return call.stream().collect(Collectors.toMap(GateioCoin::getCurrency, Function.identity()));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      handleException(e);
    }
    return null;
  }


  public List<GateioCoinNetwork> getCoinNetworkBy(String currency) throws IOException {
    try {
      List<GateioCoinNetwork> result = decorateApiCall(
          () -> gateio.getWalletCurrencyChainConfigBy(currency)).withRateLimiter(
          rateLimiter(Gateio.PUBLIC_RULE)).call();
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      handleException(e);
    }
    return null;
  }


  public Map<String, GateioWithdrawStatus> getGateioWithDrawFees(String currency)
      throws IOException {
    try {
      List<GateioWithdrawStatus> result = decorateApiCall(
          () -> gateioAuthenticated.getWithDrawConfig(apiKey, signatureCreator, timestampFactory,
              currency)).withRateLimiter(rateLimiter(Gateio.PUBLIC_RULE)).call();
      return result.stream()
          .collect(Collectors.toMap(GateioWithdrawStatus::getCurrency, Function.identity()));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      handleException(e);
    }
    return null;
  }

  public Map<CurrencyPair, Ticker> getGateioTickers() throws IOException {
    try {
     List<GateioTicker> tickers = decorateApiCall(
          () -> gateio.getTickers(null)).withRateLimiter(
          rateLimiter(PATH_SPOT_TICKERS)).call();
     return tickers.stream().collect(Collectors.toMap(
         ticker -> GateioUtils.toCurrencyPair(ticker.getCurrencyPair()),
         ticker -> GateioAdapters.adaptTicker(GateioUtils.toCurrencyPair(ticker.getCurrencyPair()), ticker)
     ));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      handleException(e);
    }
    return null;
  }

  public Ticker getGateioTicker(CurrencyPair currencyPair) throws IOException {
    try {
      List<GateioTicker> tickers = decorateApiCall(
          () -> gateio.getTickers(GateioUtils.toPairString(currencyPair))).withRateLimiter(
          rateLimiter(PATH_SPOT_TICKERS)).call();
      if (!tickers.isEmpty()) {
        GateioTicker gateioTicker = tickers.get(0);
        return GateioAdapters.adaptTicker(currencyPair, gateioTicker);
      }
    } catch (Exception e) {
      handleException(e);
    }
    return null;
  }

  public List<Instrument> getExchangeSymbols() throws IOException {
    return new ArrayList<>(gateio.getPairs().getPairs());
  }
}
