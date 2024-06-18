package org.knowm.xchange.xt;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderByInstrumentAndIdParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamInstrument;

/**
 * <p> @Date : 2024/6/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTTradeService extends XTExchangeBase{


  @Test
  public void testGetOrderDetail() throws IOException{
//    exchange.getTradeService().getOrder()

  }

  @Test
  public void testTicker24() throws IOException{
    List<Ticker> tickers = exchange.getMarketDataService().getTickers(null);
    for (Ticker ticker : tickers) {
      log.info("symbol:{} volumn : {}",ticker.getInstrument(),ticker.getVolume().toPlainString());
    }
  }

  @Test
  public void testRawPlaceBuyLimitOrder() throws IOException {
    TradeService tradeService = exchange.getTradeService();
    //BENQIUSDT
    CurrencyPair instrument = new CurrencyPair("XT", "USDT");
    LimitOrder limitOrder = new LimitOrder.Builder(OrderType.BID, instrument)
        .limitPrice(new BigDecimal("2.21"))
        .originalAmount(new BigDecimal("3")).build();

    String s = tradeService.placeLimitOrder(limitOrder);
    log.info("place Order ID : {} ", s);

    Collection<Order> orders = tradeService.getOrder(s);
    log.info("Query order size:{}", orders.size());
    log.info("order details:{}", orders.stream().findFirst());

    boolean b = tradeService.cancelOrder(s);
    log.info("cancel result:{}", b);
  }

}
