package info.bitrich.xchangestream.kucoin;

import info.bitrich.xchangestream.kucoin.dto.KucoinAccountBalanceChangesEvent;
import info.bitrich.xchangestream.kucoin.dto.KucoinOrderBook;
import info.bitrich.xchangestream.kucoin.dto.KucoinOrderBookItem;
import info.bitrich.xchangestream.kucoin.dto.KucoinOrderEventData;
import info.bitrich.xchangestream.kucoin.dto.KucoinWebSocketOrderEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.instrument.Instrument;

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

  public static OrderBook adaptOrderBook(KucoinOrderBook orderbooks, Instrument instrument) {
    List<LimitOrder> asks = new ArrayList<>();
    List<LimitOrder> bids = new ArrayList<>();
    Date timeStamp = new Date(orderbooks.getTs());

    orderbooks.getAsks()
        .forEach(askItem -> asks.add(adaptLimitOrder(askItem, instrument, Order.OrderType.ASK,timeStamp)));

    orderbooks.getBids()
        .forEach(bidItem -> bids.add(adaptLimitOrder(bidItem, instrument, Order.OrderType.BID,timeStamp)));

    return new OrderBook(timeStamp, asks, bids);
  }
  public static LimitOrder adaptLimitOrder(KucoinOrderBookItem orderBookItem,
      Instrument instrument, Order.OrderType orderType,Date timestamp) {
    return adaptOrderBookOrder(orderBookItem.getVolume(), orderBookItem.getPrice(), instrument,
        orderType,timestamp);
  }

  public static LimitOrder adaptOrderBookOrder(BigDecimal amount, BigDecimal price,
      Instrument instrument, Order.OrderType orderType,Date timestamp) {
    return new LimitOrder(orderType, amount, instrument, "", timestamp, price);
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
