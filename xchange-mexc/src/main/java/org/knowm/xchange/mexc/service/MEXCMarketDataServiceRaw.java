package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfo;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;

public class MEXCMarketDataServiceRaw extends MEXCBaseService {

  public MEXCMarketDataServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public List<MEXCConfig> getAll() {
    try {
      return mexcAuthenticated.getCoinConfig(
          apiKey,
          nonceFactory,
          signatureCreator
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public MEXCExchangeInfo getExchangeInfo() {
    try {
      return mexc.getExchangeInfo(
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public List<String> getSupportApiSymbols() {
    try {
      return mexc.getSupportApiSymbols().getData();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public MEXCPricePair getTickerPair(String symbol) {
    try {
      return mexc.getTickerPrice(symbol);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<MEXCPricePair> getTickersPair() {
    try {
      return mexc.getTickersPrice();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
