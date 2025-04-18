package org.knowm.xchange.okex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.knowm.xchange.currency.CurrencyPair.TRX_USDT;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.okex.service.OkexTradeService;
import org.knowm.xchange.service.trade.TradeService;

/**
 * <p> @Date : 2024/7/25 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class OkexTradeServiceTest {
  Instrument instrument = new FuturesContract("BTC/USDT/SWAP");
  Exchange exchange;

  @Before
  public void setUp(){
    Properties properties = new Properties();

    try {
      properties.load(this.getClass().getResourceAsStream("/secret.keys"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ExchangeSpecification spec = new OkexExchange().getDefaultExchangeSpecification();

    spec.setApiKey(properties.getProperty("apikey"));
    spec.setSecretKey(properties.getProperty("secret"));
    spec.setExchangeSpecificParametersItem(OkexExchange.PARAM_PASSPHRASE, properties.getProperty("passphrase"));
//        spec.setExchangeSpecificParametersItem(OkexExchange.PARAM_SIMULATED, "1");
    exchange = ExchangeFactory.INSTANCE.createExchange(spec);
  }

  @Test
  public void testPlaceOrder() throws IOException {
    final OkexTradeService okexTradeService = (OkexTradeService) exchange.getTradeService();
    try {
      CurrencyPair currencyPair = new CurrencyPair("SOL", "USDT");
//      TradeService tradeService = getExchange().getTradeService();
      MarketOrder marketOrder = new MarketOrder.Builder(Order.OrderType.BID, currencyPair).originalAmount(new BigDecimal("0.087000000000000000"))
          .build();
      String s = okexTradeService.placeMarketOrder(marketOrder);
      log.info("s:{}",s);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

}
