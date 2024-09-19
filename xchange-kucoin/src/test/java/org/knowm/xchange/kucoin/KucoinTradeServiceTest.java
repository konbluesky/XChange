package org.knowm.xchange.kucoin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.service.trade.TradeService;

/**
 * <p> @Date : 2024/9/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class KucoinTradeServiceTest extends KucoinBaseTest {

  TradeService tradeService = exchange.getTradeService();

  @Test
  public void testOpenOrders() throws IOException {
    OpenOrders openOrders = tradeService.getOpenOrders();
    log.info("{}", openOrders);

  }

  @Test
  public void testRawPlaceBuyLimitOrder() throws IOException {
    LimitOrder.Builder builder = new LimitOrder.Builder(OrderType.BID,
        new CurrencyPair("SUNDOG", "USDT"));
    builder.limitPrice(new BigDecimal("0.3001"));
    builder.originalAmount(new BigDecimal("1"));
    String s = tradeService.placeLimitOrder(builder.build());
    log.info("id:{}", s);
  }


  @Test
  public void testGetOrderDetailById() throws IOException {
    Collection<Order> orders = tradeService.getOrder("");
    for (Order order : orders) {
      log.info("order info : {}", order);
    }
  }

  @Test
  public void testCancelLimitOrder() throws IOException {
    boolean b = tradeService.cancelOrder("");
    log.info("id:{}", b);
  }

}
