package org.knowm.xchange.kucoin;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * <p> @Date : 2024/9/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class KucoinMarketDataServiceTest extends KucoinBaseTest {

  MarketDataService marketDataService = exchange.getMarketDataService();

  @Test
  public void testInstruments() throws IOException{
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    Map<Instrument, InstrumentMetaData> instruments = exchangeMetaData.getInstruments();
    for(Map.Entry<Instrument, InstrumentMetaData> entry : instruments.entrySet()){
      log.info("instrument :{}  metaData:{}", entry.getKey(), entry.getValue());
    }
  }


  @Test
  public void testCurrencies() throws IOException{
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    Map<Currency, CurrencyMetaData> currencies = exchangeMetaData.getCurrencies();
    currencies.forEach((k,v)->{
      log.info("currency :{}  metaData:{}", k, v);
    });
  }


  @Test
  public void testTickers() throws IOException{
    List<Ticker> tickers = exchange.getMarketDataService().getTickers(null);
    for (Ticker ticker : tickers) {
      log.info("symbol : {}   price : {}   vol: {}  quoteVol:{}", ticker.getInstrument(),
          ticker.getLast(), ticker.getVolume(), ticker.getQuoteVolume());
    }
  }

}
