package org.knowm.xchange.hashkey;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.hashkey.dto.HashKeyDepthData;
import org.knowm.xchange.hashkey.dto.HashKeyDepthItem;
import org.knowm.xchange.hashkey.dto.HashKeyEnum;
import org.knowm.xchange.hashkey.dto.HashKeyExecutionReport;
import org.knowm.xchange.hashkey.dto.HashKeyOutboundAccountInfo;
import org.knowm.xchange.hashkey.dto.HashKeyOutboundAccountInfo.BalanceInfo;
import org.knowm.xchange.hashkey.dto.HashKeyTickerInfo;
import org.knowm.xchange.instrument.Instrument;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyStreamingAdapters {

  public static OrderBook adaptOrderBook(HashKeyDepthData orderbooks, Instrument instrument) {
    List<LimitOrder> asks = new ArrayList<>();
    List<LimitOrder> bids = new ArrayList<>();

    orderbooks.getAsks()
        .forEach(okexAsk -> asks.add(adaptLimitOrder(okexAsk, instrument, Order.OrderType.ASK)));

    orderbooks.getBids()
        .forEach(okexBid -> bids.add(adaptLimitOrder(okexBid, instrument, Order.OrderType.BID)));

    return new OrderBook(Date.from(Instant.now()), asks, bids);
  }

  public static LimitOrder adaptLimitOrder(HashKeyDepthItem orderBookItem, Instrument instrument,
      Order.OrderType orderType) {
    return adaptOrderbookOrder(orderBookItem.getQuantity(), orderBookItem.getPrice(), instrument,
        orderType);
  }

  public static LimitOrder adaptOrderbookOrder(BigDecimal amount, BigDecimal price,
      Instrument instrument, Order.OrderType orderType) {

    return new LimitOrder(orderType, amount, instrument, "", null, price);
  }

  public static List<Ticker> adaptTickers(List<HashKeyTickerInfo> tickersRaw) {
    List<Ticker> tickers = Lists.newArrayList();
    for (HashKeyTickerInfo ticker : tickersRaw) {
      tickers.add(adaptTicker(ticker));
    }
    return tickers;
  }

  public static Ticker adaptTicker(HashKeyTickerInfo ticker) {
    return new Ticker.Builder()
        .instrument(HashKeyAdapters.extractOneCurrencyPairs(ticker.getSymbol()))
        .last(ticker.getPrice())
        .volume(ticker.getQuantity())
        .timestamp(new Date(ticker.getTime()))
        .build();
  }

  public static List<Balance> adaptBalance(HashKeyOutboundAccountInfo balancesRaw) {
    Balance.Builder builder = new Balance.Builder();
    List<Balance> balances = Lists.newArrayList();
    for (BalanceInfo balance : balancesRaw.getBalances()) {
      builder.available(balance.getFree())
          .frozen(balance.getLocked())
          .timestamp(new Date(balancesRaw.getEventTime()))
          .currency(Currency.getInstance(balance.getAsset()));
      balances.add(builder.build());
    }
    return balances;
  }

  public static List<Order> adaptOrders(List<HashKeyExecutionReport> orderDetails) {
    List<Order> orders = Lists.newArrayList();
    for (HashKeyExecutionReport orderDetail : orderDetails) {
      orders.add(adaptOrder(orderDetail));
    }
    return orders;
  }

  public static Order adaptOrder(HashKeyExecutionReport orderDetail) {
    Instrument symbol = HashKeyAdapters.extractOneCurrencyPairs(orderDetail.getSymbol());
    Order order = null;

    if (HashKeyEnum.OrderType.fromString(orderDetail.getOrderType())
        == HashKeyEnum.OrderType.MARKET) {
      order = new MarketOrder(
          orderDetail.getSide().equalsIgnoreCase("buy") ? Order.OrderType.BID
              : Order.OrderType.ASK,
          orderDetail.getOrderQuantity(),
          symbol,
          String.valueOf(orderDetail.getOrderId()),
          new Date(orderDetail.getOrderCreationTime()),
          orderDetail.getOrderPrice(),
          orderDetail.getCumulativeFilledQuantity(),
          null,
          convertOrderStatus(orderDetail.getCurrentOrderStatus()));
    } else {
      order = new LimitOrder(
          orderDetail.getSide().equalsIgnoreCase("buy") ? Order.OrderType.BID
              : Order.OrderType.ASK,
          orderDetail.getOrderQuantity(),
          symbol,
          String.valueOf(orderDetail.getOrderId()),
          new Date(orderDetail.getOrderCreationTime()),
          orderDetail.getOrderPrice(),
          orderDetail.getOrderPrice()
              .divide(orderDetail.getOrderQuantity(), 8, RoundingMode.DOWN),
          orderDetail.getCumulativeFilledQuantity(),
          null,
          convertOrderStatus(orderDetail.getCurrentOrderStatus()));
    }
    return order;
  }

  /**
   * NEW PARTIALLY_FILLED FILLED PARTIALLY_CANCELED CANCELED REJECTED
   *
   * @param status
   * @return
   */
  public static Order.OrderStatus convertOrderStatus(String status) {
    switch (status) {
      case "NEW":
        return Order.OrderStatus.NEW;
      case "PARTIALLY_FILLED":
        return Order.OrderStatus.PARTIALLY_FILLED;
      case "FILLED":
        return Order.OrderStatus.FILLED;
      case "PARTIALLY_CANCELED":
        return Order.OrderStatus.PARTIALLY_CANCELED;
      case "CANCELED":
        return Order.OrderStatus.CANCELED;
      case "REJECTED":
        return Order.OrderStatus.REJECTED;
      default:
        return Order.OrderStatus.UNKNOWN;
    }
  }

}
