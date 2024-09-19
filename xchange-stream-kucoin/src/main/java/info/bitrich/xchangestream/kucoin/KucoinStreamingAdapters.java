package info.bitrich.xchangestream.kucoin;

import info.bitrich.xchangestream.kucoin.dto.KucoinAccountBalanceChangesEvent;
import info.bitrich.xchangestream.kucoin.dto.KucoinOrderEventData;
import info.bitrich.xchangestream.kucoin.dto.KucoinWebSocketOrderEvent;
import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;

public class KucoinStreamingAdapters {

  public static Order adaptOrder(KucoinWebSocketOrderEvent orderEvent) {
    KucoinOrderEventData data = orderEvent.data;

    Order.OrderType orderType = "buy".equals(data.side) ? Order.OrderType.BID : Order.OrderType.ASK;
    CurrencyPair currencyPair = data.getCurrencyPair();

    Order.Builder orderBuilder =
        "market".equals(data.orderType)
            ? new MarketOrder.Builder(orderType, currencyPair)
            : new LimitOrder.Builder(orderType, currencyPair)
                .limitPrice(new BigDecimal(data.price));

    orderBuilder
        .id(data.orderId)
        .originalAmount(new BigDecimal(data.size))
        .timestamp(new Date(data.orderTime))
        .cumulativeAmount(new BigDecimal(data.filledSize))
        .orderStatus(adaptStatus(data.status));

    return orderBuilder.build();
  }

  private static Order.OrderStatus adaptStatus(String status) {
    if ("open".equals(status)) {
      return Order.OrderStatus.NEW;
    }
    if ("match".equals(status)) {
      return Order.OrderStatus.PARTIALLY_FILLED;
    }
    if ("done".equals(status)) {
      return Order.OrderStatus.FILLED;
    }
    return null;
  }

  public static Balance adaptBalance(KucoinAccountBalanceChangesEvent balanceChangesEvent) {
    Balance.Builder builder = new Balance.Builder();
    builder.currency(Currency.getInstance(balanceChangesEvent.data.currency))
        .available(balanceChangesEvent.data.available)
        .total(balanceChangesEvent.data.total)
        .frozen(balanceChangesEvent.data.hold)
        .timestamp(balanceChangesEvent.data.time);
    return builder.build();
  }


}
