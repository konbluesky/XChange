package org.knowm.xchange.xt;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.service.XTMarketDataServiceRaw;

/**
 * <p> @Date : 2024/7/18 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTCoinCoinfigGenericTest extends XTExchangeBase {


  @Test
  public void testXtCoinInstrumentCsv() throws IOException {
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    XTMarketDataServiceRaw marketDataService = (XTMarketDataServiceRaw) exchange.getMarketDataService();
    List<XTSymbol> symbols = marketDataService.getSymbols(null, null);
    String title = "pair,baseCurrency,baseCurrencyId,quoteCurrency,quoteCurrencyId";
    List<String> lines = Lists.newArrayList();
    lines.add(title);
    for (XTSymbol symbol : symbols) {
      String line = String.format("%s,%s,%s,%s,%s",
          symbol.getSymbol().toUpperCase(),
          symbol.getBaseCurrency().toUpperCase(),
          symbol.getBaseCurrencyId(),
          symbol.getQuoteCurrency().toUpperCase(),
          symbol.getQuoteCurrencyId());
      lines.add(line);
    }
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("xt-coin-id-map.csv"));
  }

}
