package org.knowm.xchange.mexc;

import java.math.BigDecimal;
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
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.dto.MEXCBalance;
import org.knowm.xchange.mexc.dto.MEXCOrderBook;
import org.knowm.xchange.mexc.dto.MEXCOrderBookItem;
import org.knowm.xchange.mexc.dto.MEXCOrderDetail;
import org.knowm.xchange.mexc.dto.MEXCTicker;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCStreamingAdapters {

  public static OrderBook adaptOrderBook(MEXCOrderBook orderbooks, Instrument instrument) {
    List<LimitOrder> asks = new ArrayList<>();
    List<LimitOrder> bids = new ArrayList<>();

    orderbooks.getAsks()
        .forEach(okexAsk -> asks.add(adaptLimitOrder(okexAsk, instrument, Order.OrderType.ASK)));

    orderbooks.getBids()
        .forEach(okexBid -> bids.add(adaptLimitOrder(okexBid, instrument, Order.OrderType.BID)));

    return new OrderBook(Date.from(Instant.now()), asks, bids);
  }

  public static LimitOrder adaptLimitOrder(MEXCOrderBookItem orderBookItem, Instrument instrument,
      Order.OrderType orderType) {
    return adaptOrderbookOrder(orderBookItem.getVolume(), orderBookItem.getPrice(), instrument,
        orderType);
  }

  public static LimitOrder adaptOrderbookOrder(BigDecimal amount, BigDecimal price,
      Instrument instrument, Order.OrderType orderType) {

    return new LimitOrder(orderType, amount, instrument, "", null, price);
  }


  public static Ticker adaptTicker(MEXCTicker ticker, String asset, Long timestamp) {
    return new Ticker.Builder()
        .instrument(MEXCAdapters.extractOneCurrencyPairs(asset))
        .last(new BigDecimal(ticker.getPrice()))
        .volume(new BigDecimal(ticker.getQuantity()))
        .timestamp(new Date(timestamp))
        .build();
  }

  public static Balance adaptBalance(MEXCBalance balance, long timestamp) {
    Balance.Builder builder = new Balance.Builder();
    builder.available(new BigDecimal(balance.getBalance()))
        .frozen(new BigDecimal(balance.getFrozen()))
        .timestamp(new Date(timestamp))
        .currency(Currency.getInstance(balance.getCurrency()));
    return builder.build();
  }

  public static Order adaptOrder(MEXCOrderDetail orderDetail, String asset) {
    Instrument symbol = MEXCAdapters.extractOneCurrencyPairs(asset);
    Order order = null;

    if (orderDetail.getIsMaker() == 1) {
      order = new MarketOrder(
          1 == orderDetail.getSide() ? Order.OrderType.BID : Order.OrderType.ASK,
          orderDetail.getAmount(),
          symbol,
          orderDetail.getOrderId(),
          new Date(orderDetail.getCreateTime()),
          orderDetail.getAvgPrice(),
          orderDetail.getRemainAmount(),
          null,
          convertOrderStatus(orderDetail.getStatus()));
    } else {
      order = new LimitOrder(1 == orderDetail.getSide() ? Order.OrderType.BID : Order.OrderType.ASK,
          orderDetail.getAmount(),
          symbol,
          orderDetail.getOrderId(),
          new Date(orderDetail.getCreateTime()),
          orderDetail.getPrice(),
          orderDetail.getAvgPrice(),
          orderDetail.getRemainAmount(),
          null,
          convertOrderStatus(orderDetail.getStatus()));
    }
    return order;
  }

  /**
   * 订单状态 1:未成交 2:已成交 3:部分成交 4:已撤单 5:部分撤单
   *
   * @param status
   * @return
   */
  public static Order.OrderStatus convertOrderStatus(int status) {
    switch (status) {
      case 1:
        return Order.OrderStatus.NEW;
      case 2:
        return Order.OrderStatus.FILLED;
      case 3:
        return Order.OrderStatus.PARTIALLY_FILLED;
      case 4:
        return Order.OrderStatus.CANCELED;
      case 5:
        return Order.OrderStatus.PARTIALLY_CANCELED;
      default:
        return Order.OrderStatus.UNKNOWN;
    }
  }

}
