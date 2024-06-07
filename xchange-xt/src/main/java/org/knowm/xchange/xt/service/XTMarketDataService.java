package org.knowm.xchange.xt.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.marketdata.params.InstrumentsParams;
import org.knowm.xchange.service.marketdata.params.Params;
import org.knowm.xchange.xt.XTAdapters;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTMarketDataService extends XTMarketDataServiceRaw implements MarketDataService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public XTMarketDataService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public Ticker getTicker(Instrument instrument, Object... args) throws IOException {
    List<XTTicker> tickers = getTickers(XTAdapters.adaptInstrument(instrument), null);
    return XTAdapters.adaptTicker(tickers.get(0), instrument);
  }

  @Override
  public List<Ticker> getTickers(Params params) throws IOException {
    if (params == null) {
      return XTAdapters.adaptTickers(getTickers(null, null));
    }
    if (params instanceof InstrumentsParams) {
      Collection<Instrument> is = ((InstrumentsParams) params).getInstruments();
      String symbols = is.stream().map(i -> i.getBase() + "_" + i.getCounter())
          .reduce((a, b) -> a + "," + b)
          .get();
      List<XTTicker> tickers = getTickers(null, symbols);
      return XTAdapters.adaptTickers(tickers);
    }
    return null;
  }
}
