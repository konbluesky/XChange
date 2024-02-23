package org.knowm.xchange.gateio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.marketdata.params.CurrencyPairsParam;
import org.knowm.xchange.service.marketdata.params.Params;

public class GateioMarketDataService extends GateioMarketDataServiceRaw implements
    MarketDataService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GateioMarketDataService(GateioExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public List<Ticker> getTickers(Params params) throws IOException {
    if (params == null) {
      return getGateioTickers().values().stream().collect(Collectors.toList());
    }

    final List<CurrencyPair> currencyPairs = new ArrayList<>();
    if (params instanceof CurrencyPairsParam) {
      currencyPairs.addAll(((CurrencyPairsParam) params).getCurrencyPairs());
    }
    return getGateioTickers().values().stream().filter(
            ticker -> currencyPairs.size() == 0 || currencyPairs.contains(ticker.getCurrencyPair()))
        .collect(Collectors.toList());
  }


}
