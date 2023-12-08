package org.knowm.xchange.mexc.service;

import static org.knowm.xchange.mexc.MEXCResilience.REST_IP_RATE_LIMITER;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfo;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;

public class MEXCMarketDataServiceRaw extends MEXCBaseService {

  public MEXCMarketDataServiceRaw(MEXCExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public List<MEXCConfig> getAll() {
    try {
      return decorateApiCall(() -> mexcAuthenticated.getCoinConfig(apiKey, nonceFactory,
          signatureCreator)).withRateLimiter(rateLimiter(REST_IP_RATE_LIMITER)).call();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public MEXCExchangeInfo getExchangeInfo() {
    try {
      return decorateApiCall(() -> mexc.getExchangeInfo())
          .withRateLimiter(rateLimiter(REST_IP_RATE_LIMITER))
          .call();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<String> getSupportApiSymbols() {
    try {
      return decorateApiCall(() -> mexc.getSupportApiSymbols().getData())
          .withRateLimiter(rateLimiter(REST_IP_RATE_LIMITER))
          .call();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public MEXCPricePair getTickerPair(String symbol) {
    try {
      return decorateApiCall(() -> mexc.getTickerPrice(symbol))
          .withRateLimiter(rateLimiter(REST_IP_RATE_LIMITER))
          .call();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<MEXCPricePair> getTickersPair() {
    try {
      return decorateApiCall(() -> mexc.getTickersPrice())
          .withRateLimiter(rateLimiter(REST_IP_RATE_LIMITER))
          .call();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
