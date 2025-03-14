package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.marketdata.params.Params;

@Slf4j
public class MEXCMarketDataService extends MEXCMarketDataServiceRaw implements MarketDataService {

  public MEXCMarketDataService(MEXCExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
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

  @Override
  public List<Ticker> getTickers(Params params) throws IOException {
    try {
      if (params == null) {
        return MEXCAdapters.adaptTickers(getTickers24());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
