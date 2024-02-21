package org.knowm.xchange.gateio.service;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.dto.GateioEnums;
import org.knowm.xchange.gateio.dto.GateioEnums.TimeInForce;
import org.knowm.xchange.gateio.dto.trade.GateioOpenOrders;
import org.knowm.xchange.gateio.dto.trade.GateioOrder;
import org.knowm.xchange.gateio.dto.trade.GateioTrade;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderByCurrencyPairAndIdParams;
import org.knowm.xchange.service.trade.params.DefaultTradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

@Slf4j
public class GateioTradeService extends GateioTradeServiceRaw implements TradeService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GateioTradeService(GateioExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    return getOpenOrders(createOpenOrdersParams());
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    if (params == null) {
      List<GateioOpenOrders> orderList = super.getGateioOpenOrders();
      List<LimitOrder> limitOrders = GateioAdapters.adaptOpenOrders(orderList);
      return new OpenOrders(limitOrders);
    }
    return new OpenOrders(Lists.newArrayList());
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {

    CurrencyPair currencyPair = (CurrencyPair) limitOrder.getInstrument();
    GateioEnums.OrderSide orderSide =
        limitOrder.getType().equals(Order.OrderType.BID) ? GateioEnums.OrderSide.BUY
            : GateioEnums.OrderSide.SELL;
    TimeInForce timeInForce = TimeInForce.GTC;
    BigDecimal price = limitOrder.getLimitPrice();
    BigDecimal amount = limitOrder.getOriginalAmount();

    GateioOrder gateioOrder = super.placeLimitOrder(currencyPair, orderSide, price, amount,
        timeInForce);
    return String.valueOf(gateioOrder.getId());
  }

  @Override
  public boolean cancelOrder(String orderId) throws IOException {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public Class[] getRequiredCancelOrderParamClasses() {
    return new Class[]{CancelOrderByIdParams.class, CancelOrderByCurrencyPair.class};
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    if (orderParams instanceof DefaultCancelOrderByCurrencyPairAndIdParams) {
      DefaultCancelOrderByCurrencyPairAndIdParams cancelParam = (DefaultCancelOrderByCurrencyPairAndIdParams) orderParams;
      CurrencyPair currencyPair = cancelParam.getCurrencyPair();
      String orderId = cancelParam.getOrderId();
      GateioOrder order = super.cancelOrder(orderId, currencyPair);
      return order.getStatus() == GateioEnums.OrderStatus.CANCELLED;
    } else {
      return false;
    }
  }


  /**
   * Required parameter: {@link TradeHistoryParamCurrencyPair}
   */
  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params)
      throws ExchangeException, IOException {

    CurrencyPair pair = ((TradeHistoryParamCurrencyPair) params).getCurrencyPair();
    List<GateioTrade> userTrades = getGateioTradeHistory(pair).getTrades();

    return GateioAdapters.adaptUserTrades(userTrades);
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    List<Order> orders = new ArrayList<>();
    for (OrderQueryParams param : orderQueryParams) {
      if (!(param instanceof DefaultQueryOrderParamCurrencyPair)) {
        throw new NotAvailableFromExchangeException(
            "getOrder in gateio needs orderId and currency pair");
      }
      DefaultQueryOrderParamCurrencyPair queryOrderParamCurrencyPair =
          (DefaultQueryOrderParamCurrencyPair) param;
      GateioOrder gateioOrder =
          getOrder(
              queryOrderParamCurrencyPair.getOrderId(),
              queryOrderParamCurrencyPair.getCurrencyPair());
      Order order = GateioAdapters.adaptLimitOrder(gateioOrder);
      orders.add(order);
    }
    return orders;
  }

  @Override
  public TradeHistoryParamCurrencyPair createTradeHistoryParams() {

    return new DefaultTradeHistoryParamCurrencyPair();
  }

  @Override
  public OpenOrdersParams createOpenOrdersParams() {
    return null;
  }
}
