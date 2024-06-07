package org.knowm.xchange.xt;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.xt.dto.*;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTStreamingAdapters {

    public static OrderBook adaptOrderBook(List<XTOrderBook> orderbooks, Instrument instrument) {
        List<LimitOrder> asks = new ArrayList<>();
        List<LimitOrder> bids = new ArrayList<>();

        orderbooks.get(0).getAsks()
                  .forEach(okexAsk -> asks.add(adaptLimitOrder(okexAsk, instrument, Order.OrderType.ASK)));

        orderbooks.get(0).getBids()
                  .forEach(okexBid -> bids.add(adaptLimitOrder(okexBid, instrument, Order.OrderType.BID)));

        return new OrderBook(Date.from(Instant.now()), asks, bids);
    }

    public static LimitOrder adaptLimitOrder(XTOrderBookItem xtOrderBookItem, Instrument instrument, Order.OrderType orderType) {
        return adaptOrderbookOrder(xtOrderBookItem.getVolume(), xtOrderBookItem.getPrice(), instrument, orderType);
    }

    public static LimitOrder adaptOrderbookOrder(BigDecimal amount, BigDecimal price, Instrument instrument, Order.OrderType orderType) {

        return new LimitOrder(orderType, amount, instrument, "", null, price);
    }

    public static Balance adaptBalance(XTBalance balance) {
        Balance.Builder builder = new Balance.Builder();
        builder.available(new BigDecimal(balance.getBalance()))
               .frozen(new BigDecimal(balance.getFrozen()))
               .currency(Currency.getInstance(balance.getCurrency()));
        return builder.build();
    }

    public static Order adaptOrder(XTOrderDetail xtOrderDetail) {
        Instrument symbol = XTAdapters.adaptInstrumentId(xtOrderDetail.getSymbol());
        Order order = null;

        if ("market".equalsIgnoreCase(xtOrderDetail.getType())) {
            order = new MarketOrder("buy".equalsIgnoreCase(xtOrderDetail.getSide()) ? Order.OrderType.BID : Order.OrderType.ASK,
                    new BigDecimal(xtOrderDetail.getOrigQty()),
                    symbol,
                    xtOrderDetail.getOrderId(),
                    new Date(xtOrderDetail.getCreateTime()),
                    new BigDecimal(xtOrderDetail.getAvgPrice()==null? "0" : xtOrderDetail.getAvgPrice()),
                    new BigDecimal(xtOrderDetail.getExecutedQty()),
                    new BigDecimal(xtOrderDetail.getFee()),
                    Order.OrderStatus.valueOf(xtOrderDetail.getState()));
        } else {
            order = new LimitOrder("buy".equalsIgnoreCase(xtOrderDetail.getSide()) ? Order.OrderType.BID : Order.OrderType.ASK,
                    new BigDecimal(xtOrderDetail.getOrigQty()),
                    symbol,
                    xtOrderDetail.getOrderId(),
                    new Date(xtOrderDetail.getCreateTime()),
                    new BigDecimal(xtOrderDetail.getPrice()),
                    new BigDecimal(xtOrderDetail.getAvgPrice()==null? "0" : xtOrderDetail.getAvgPrice()),
                    new BigDecimal(xtOrderDetail.getExecutedQty()),
                    new BigDecimal(xtOrderDetail.getFee()),
                    Order.OrderStatus.valueOf(xtOrderDetail.getState()));
        }
        return order;
    }
}
