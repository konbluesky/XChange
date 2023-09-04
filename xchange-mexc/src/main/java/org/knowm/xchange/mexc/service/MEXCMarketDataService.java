package org.knowm.xchange.mexc.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;
import org.knowm.xchange.service.marketdata.MarketDataService;

@Slf4j
public class MEXCMarketDataService extends MEXCMarketDataServiceRaw implements MarketDataService {

  public MEXCMarketDataService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public Ticker getTicker(Instrument instrument, Object... args) throws IOException {
    try {
      String symbol = MEXCAdapters.convertToMEXCSymbol(instrument.toString());
      MEXCPricePair tickerPair = getTickerPair(symbol);
      return MEXCAdapters.adaptTicker(tickerPair);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

}
