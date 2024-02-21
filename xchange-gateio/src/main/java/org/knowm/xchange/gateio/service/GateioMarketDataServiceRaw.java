package org.knowm.xchange.gateio.service;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.gateio.Gateio;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawStatus;
import org.knowm.xchange.gateio.dto.marketdata.GateioCandlestickHistory;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoin;
import org.knowm.xchange.gateio.dto.marketdata.GateioDepth;
import org.knowm.xchange.gateio.dto.marketdata.GateioKline;
import org.knowm.xchange.gateio.dto.marketdata.GateioKlineInterval;
import org.knowm.xchange.gateio.dto.marketdata.GateioPair;
import org.knowm.xchange.gateio.dto.marketdata.GateioTicker;
import org.knowm.xchange.gateio.dto.marketdata.GateioTradeHistory;
import org.knowm.xchange.instrument.Instrument;

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

  public Map<CurrencyPair, GateioPair> getGateioMarketInfo()
      throws IOException {

    try {
      List<GateioPair> marketInfo = decorateApiCall(() -> gateio.getMarketInfo()).withRateLimiter(
          rateLimiter(Gateio.PUBLIC_RULE)).call();

      Map<CurrencyPair, GateioPair> result = Maps.newHashMap();
      for (GateioPair gateioPair : marketInfo) {
        result.put(new CurrencyPair(gateioPair.getBase(), gateioPair.getQuote()), gateioPair);
      }
      return result;

    } catch (Exception e) {
      throw e;
    }
  }

  public Map<String, GateioCoin> getGateioCoinInfo() throws IOException {
    try {

      List<GateioCoin> call = decorateApiCall(() -> gateio.getCoinInfo()).withRateLimiter(
          rateLimiter(Gateio.PUBLIC_RULE)).call();

      return call
          .stream()
          .collect(Collectors.toMap(GateioCoin::getCurrency, Function.identity()));

    } catch (Exception e) {
      throw e;
    }
  }

  public Map<String, GateioWithdrawStatus> getGateioWithDrawFees(String currency)
      throws IOException {
    try {
      List<GateioWithdrawStatus> result = decorateApiCall(
          () -> gateioAuthenticated.getWithDrawConfig(apiKey, signatureCreator, timestampFactory,
              currency))
          .withRateLimiter(
              rateLimiter(Gateio.PUBLIC_RULE)).call();
      return result.stream()
          .collect(Collectors.toMap(GateioWithdrawStatus::getCurrency, Function.identity()));
    } catch (Exception e) {
      throw e;
    }
  }

  public Map<CurrencyPair, Ticker> getGateioTickers() throws IOException {

    Map<String, GateioTicker> gateioTickers = gateio.getTickers();
    Map<CurrencyPair, Ticker> adaptedTickers = new HashMap<>(gateioTickers.size());
    gateioTickers.forEach(
        (currencyPairString, gateioTicker) -> {
          String[] currencyPairStringSplit = currencyPairString.split("_");
          CurrencyPair currencyPair =
              new CurrencyPair(
                  Currency.getInstance(currencyPairStringSplit[0].toUpperCase()),
                  Currency.getInstance(currencyPairStringSplit[1].toUpperCase()));
          adaptedTickers.put(currencyPair, GateioAdapters.adaptTicker(currencyPair, gateioTicker));
        });

    return adaptedTickers;
  }

  public Map<CurrencyPair, GateioDepth> getGateioDepths() throws IOException {
    Map<String, GateioDepth> depths = gateio.getDepths();
    Map<CurrencyPair, GateioDepth> adaptedDepths = new HashMap<>(depths.size());
    depths.forEach(
        (currencyPairString, gateioDepth) -> {
          String[] currencyPairStringSplit = currencyPairString.split("_");
          CurrencyPair currencyPair =
              new CurrencyPair(
                  Currency.getInstance(currencyPairStringSplit[0].toUpperCase()),
                  Currency.getInstance(currencyPairStringSplit[1].toUpperCase()));
          adaptedDepths.put(currencyPair, gateioDepth);
        });
    return adaptedDepths;
  }

  public GateioTicker getBTERTicker(String tradableIdentifier, String currency) throws IOException {

    GateioTicker gateioTicker =
        gateio.getTicker(tradableIdentifier.toLowerCase(), currency.toLowerCase());

    return handleResponse(gateioTicker);
  }

  public GateioDepth getBTEROrderBook(String tradeableIdentifier, String currency)
      throws IOException {

    GateioDepth gateioDepth =
        gateio.getFullDepth(tradeableIdentifier.toLowerCase(), currency.toLowerCase());

    return handleResponse(gateioDepth);
  }

  public GateioTradeHistory getBTERTradeHistory(String tradeableIdentifier, String currency)
      throws IOException {

    GateioTradeHistory tradeHistory = gateio.getTradeHistory(tradeableIdentifier, currency);

    return handleResponse(tradeHistory);
  }

  public GateioTradeHistory getBTERTradeHistorySince(
      String tradeableIdentifier, String currency, String tradeId) throws IOException {

    GateioTradeHistory tradeHistory =
        gateio.getTradeHistorySince(tradeableIdentifier, currency, tradeId);

    return handleResponse(tradeHistory);
  }

  public List<Instrument> getExchangeSymbols() throws IOException {

    return new ArrayList<>(gateio.getPairs().getPairs());
  }

  public List<GateioKline> getKlines(CurrencyPair pair, GateioKlineInterval interval, Integer hours)
      throws IOException {

    if (hours != null && hours < 1) {
      throw new ExchangeException("Variable 'hours' should be more than 0!");
    }

    GateioCandlestickHistory candlestickHistory =
        handleResponse(
            gateio.getKlinesGate(
                pair.toString().replace('/', '_').toLowerCase(), hours, interval.getSeconds()));

    return candlestickHistory.getCandlesticks().stream()
        .map(
            data ->
                new GateioKline(
                    Long.parseLong(data.get(0)),
                    new BigDecimal(data.get(1)),
                    new BigDecimal(data.get(2)),
                    new BigDecimal(data.get(3)),
                    new BigDecimal(data.get(4)),
                    new BigDecimal(data.get(5))))
        .collect(Collectors.toList());
  }
}
