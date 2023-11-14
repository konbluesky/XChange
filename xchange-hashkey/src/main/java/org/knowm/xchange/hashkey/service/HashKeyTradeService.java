package org.knowm.xchange.hashkey.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.hashkey.HashKeyAdapters;
import org.knowm.xchange.hashkey.dto.HashKeyCancelOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyCreateOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyEnum.OrderType;
import org.knowm.xchange.hashkey.dto.HashKeyEnum.Side;
import org.knowm.xchange.hashkey.dto.HashKeyOrderDetailResponse;
import org.knowm.xchange.hashkey.dto.params.HashKeyOpenOrdersParams;
import org.knowm.xchange.hashkey.dto.params.HashKeyTradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamInstrument;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParamInstrument;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamInstrument;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyTradeService extends HashKeyTradeServiceRaw implements TradeService {

  public HashKeyTradeService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    try {
      String symbol = HashKeyAdapters.convertToHashKeySymbol(limitOrder.getInstrument().toString());
      String side =
          limitOrder.getType() == Order.OrderType.BID ? Side.BUY.name() : Side.SELL.name();
      String type = OrderType.LIMIT.name();

      HashKeyCreateOrderResponse orderResponse = super.createOrder(symbol, side, type,
          limitOrder.getOriginalAmount(), null, limitOrder.getLimitPrice());
      Order order = HashKeyAdapters.adaptOrder(orderResponse);
      return order.getId();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    try {
      String symbol = HashKeyAdapters.convertToHashKeySymbol(
          marketOrder.getInstrument().toString());
      String side =
          marketOrder.getType() == Order.OrderType.BID ? Side.BUY.name() : Side.SELL.name();
      String type = OrderType.MARKET.name();

      HashKeyCreateOrderResponse orderResponse = super.createOrder(symbol, side, type,
          marketOrder.getOriginalAmount(), null, null);
      Order order = HashKeyAdapters.adaptOrder(orderResponse);
      return order.getId();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    if (params instanceof DefaultOpenOrdersParamInstrument) {
      DefaultOpenOrdersParamInstrument instrumentParams = (DefaultOpenOrdersParamInstrument) params;
      String symbol = HashKeyAdapters.convertToHashKeySymbol(
          instrumentParams.getInstrument().toString());
      //TODO
//      return new OpenOrders(HashKeyAdapters.adaptOpenOrders(super.getOpenOrders(symbol)));
    }
    return new OpenOrders(new ArrayList<>(0));
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    HashKeyOpenOrdersParams openOrdersParams = createOpenOrdersParams();
    String orderId = openOrdersParams.getOrderId();
    String symbol = HashKeyAdapters.convertToHashKeySymbol(
        openOrdersParams.getInstrument().toString());
    Integer limit = openOrdersParams.getLimit();
    List<HashKeyOrderDetailResponse> openOrders = super.getOpenOrders(orderId, symbol, limit);
    return HashKeyAdapters.adaptOpenOrders(openOrders);
  }


  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
    if (params instanceof TradeHistoryParams) {
      String orderId = "", symbol = "", startTime = "", endTime = "";

      if (params instanceof TradeHistoryParamInstrument) {
        symbol = HashKeyAdapters.convertToHashKeySymbol(
            ((TradeHistoryParamInstrument) params).getInstrument().toString());
      }
      if (params instanceof TradeHistoryParamsTimeSpan) {
        startTime = String.valueOf(((TradeHistoryParamsTimeSpan) params).getStartTime().getTime());
        endTime = String.valueOf(((TradeHistoryParamsTimeSpan) params).getEndTime().getTime());
      }
      if (params instanceof HashKeyTradeHistoryParamsTimeSpan) {
        orderId = ((HashKeyTradeHistoryParamsTimeSpan) params).getOrderId();
      }

      //TODO 未完成
      super.getTradeOrders(orderId, symbol, Long.valueOf(startTime), Long.valueOf(endTime), null);

    }
    return TradeService.super.getTradeHistory(params);
  }

  @Override
  public HashKeyOpenOrdersParams createOpenOrdersParams() {
    return new HashKeyOpenOrdersParams();
  }

  @Override
  public TradeHistoryParams createTradeHistoryParams() {
    return HashKeyTradeHistoryParamsTimeSpan.builder().build();
  }

  @Override
  public boolean cancelOrder(String orderId) throws IOException {
    try {
      HashKeyCancelOrderResponse cancelOrderResponse = super.cancelOrderById(orderId);
      Order order = HashKeyAdapters.adaptOrderByCancelOrder(cancelOrderResponse);
      return order.getStatus().isFinal();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    if (orderQueryParams[0] instanceof DefaultQueryOrderParamInstrument) {
      DefaultQueryOrderParamInstrument params = (DefaultQueryOrderParamInstrument) orderQueryParams[0];
      HashKeyOrderDetailResponse order = super.queryOrder(params.getOrderId());
      if (order != null) {
        return Collections.singletonList(HashKeyAdapters.adaptOrderByQueryOrder(order));
      }
    }
    return new ArrayList<>(0);
  }
}
