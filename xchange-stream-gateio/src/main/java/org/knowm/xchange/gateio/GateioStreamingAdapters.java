package org.knowm.xchange.gateio;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderStatus;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.gateio.dto.GateioBalance;
import org.knowm.xchange.gateio.dto.GateioOrderBook;
import org.knowm.xchange.gateio.dto.GateioOrderBookItem;
import org.knowm.xchange.gateio.dto.GateioOrderDetail;
import org.knowm.xchange.gateio.dto.GateioTicker;
import org.knowm.xchange.gateio.dto.GateioTradeDetail;
import org.knowm.xchange.instrument.Instrument;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioStreamingAdapters {

  public static OrderBook adaptOrderBook(GateioOrderBook orderbooks, Instrument instrument) {
    List<LimitOrder> asks = new ArrayList<>();
    List<LimitOrder> bids = new ArrayList<>();

    orderbooks.getAsks()
        .forEach(askItem -> asks.add(adaptLimitOrder(askItem, instrument, Order.OrderType.ASK)));

    orderbooks.getBids()
        .forEach(bidItem -> bids.add(adaptLimitOrder(bidItem, instrument, Order.OrderType.BID)));

    return new OrderBook(Date.from(Instant.now()), asks, bids);
  }

  public static LimitOrder adaptLimitOrder(GateioOrderBookItem xtOrderBookItem,
      Instrument instrument, Order.OrderType orderType) {
    return adaptOrderbookOrder(xtOrderBookItem.getVolume(), xtOrderBookItem.getPrice(), instrument,
        orderType);
  }

  public static LimitOrder adaptOrderbookOrder(BigDecimal amount, BigDecimal price,
      Instrument instrument, Order.OrderType orderType) {
    return new LimitOrder(orderType, amount, instrument, "", null, price);
  }

  public static List<Balance> adaptBalances(List<GateioBalance> gateioBalances) {
    List<Balance> balances = new ArrayList<>();
    for (GateioBalance gateioBalance : gateioBalances) {
      balances.add(adaptBalance(gateioBalance));
    }
    return balances;
  }

  public static Balance adaptBalance(GateioBalance balance) {
    Balance.Builder builder = new Balance.Builder();
    builder
        .total(new BigDecimal(balance.getTotal()))
        .available(new BigDecimal(balance.getAvailable()))
        .frozen(new BigDecimal(balance.getFreeze()))
        .currency(Currency.getInstance(balance.getCurrency()));
    return builder.build();
  }

  public static Ticker adaptTicker(GateioTicker gateioTicker) {
    BigDecimal ask = gateioTicker.getLowestAsk();
    BigDecimal bid = gateioTicker.getHighestBid();
    BigDecimal last = gateioTicker.getLast();
    BigDecimal low = gateioTicker.getLow24h();
    BigDecimal high = gateioTicker.getHigh24h();
    // Looks like gate.io vocabulary is inverted...
    BigDecimal baseVolume = gateioTicker.getQuoteVolume();
    BigDecimal quoteVolume = gateioTicker.getBaseVolume();
    BigDecimal percentageChange = gateioTicker.getChangePercentage();
    CurrencyPair currencyPair = GateioAdapters.adaptCurrencyPair(gateioTicker.getCurrencyPair());

    return new Ticker.Builder()
        .currencyPair(currencyPair)
        .ask(ask)
        .bid(bid)
        .last(last)
        .low(low)
        .high(high)
        .volume(baseVolume)
        .quoteVolume(quoteVolume)
        .percentageChange(percentageChange)
        .timestamp(new Date(gateioTicker.getTimeMs()))
        .build();
  }


  public static Trade adaptTrade(GateioTradeDetail tradeDetail) {
    Trade.Builder tradeBuilder = new Trade.Builder();
    tradeBuilder.id(String.valueOf(tradeDetail.getId()));
    tradeBuilder.timestamp(new Date(Long.valueOf(tradeDetail.getCreateTimeMs())));
    tradeBuilder.type(
        "buy".equalsIgnoreCase(tradeDetail.getSide()) ? OrderType.BID : OrderType.ASK);
    tradeBuilder.instrument(GateioAdapters.adaptCurrencyPair(tradeDetail.getCurrencyPair()));
    tradeBuilder.originalAmount(tradeDetail.getAmount());
    tradeBuilder.price(tradeDetail.getPrice());
    return tradeBuilder.build();
  }

  public static Order adaptOrder(GateioOrderDetail orderDetail) {
    Instrument symbol = GateioAdapters.adaptCurrencyPair(orderDetail.getCurrencyPair());
    Order order = null;
    BigDecimal amount = new BigDecimal(orderDetail.getAmount());
    BigDecimal left = new BigDecimal(orderDetail.getLeft());
    if ("market".equalsIgnoreCase(orderDetail.getType())) {
      order = new MarketOrder(
          "buy".equalsIgnoreCase(orderDetail.getSide()) ? Order.OrderType.BID
              : Order.OrderType.ASK,
          amount,
          symbol,
          orderDetail.getId(),
          new Date(Long.valueOf(orderDetail.getUpdateTimeMs())),
          new BigDecimal(orderDetail.getAvgDealPrice()),
          amount.subtract(left),
          new BigDecimal(orderDetail.getFee()),
          convertOrderStatus(orderDetail.getFinishAs()));
    } else {
      order = new LimitOrder(
          "buy".equalsIgnoreCase(orderDetail.getSide()) ? Order.OrderType.BID
              : Order.OrderType.ASK,
          amount,
          symbol,
          orderDetail.getId(),
          new Date(Long.valueOf(orderDetail.getUpdateTimeMs())),
          new BigDecimal(orderDetail.getPrice()),
          new BigDecimal(orderDetail.getAvgDealPrice()),
          left,
          new BigDecimal(orderDetail.getFee()),
          convertOrderStatus(orderDetail.getFinishAs()));
    }
    return order;
  }

  /**
   *  OPEN,
   *     FILLED,
   *     CANCELLED,
   *     IOC,
   *     STP
   * @param status
   * @return
   */
  public static OrderStatus convertOrderStatus(String status) {
    switch (status) {
      case "open":
        return OrderStatus.NEW;
      case "closed":
        return OrderStatus.FILLED;
      case "cancelled":
        return OrderStatus.CANCELED;
      default:
        return OrderStatus.UNKNOWN;
    }
  }
}
