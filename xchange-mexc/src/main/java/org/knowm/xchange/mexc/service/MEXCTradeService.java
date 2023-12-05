package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderCancelResponse;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderDetail;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderByInstrumentAndIdParams;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParamInstrument;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamInstrument;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class MEXCTradeService extends MEXCTradeServiceRaw implements TradeService {

  public MEXCTradeService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    try {
      MEXCOrderRequestPayload orderRequestPayload = MEXCAdapters.adaptOrder(limitOrder);
      MEXCOrder order = placeOrder(orderRequestPayload);
      return order.getId();
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {

    if (orderQueryParams[0] instanceof DefaultQueryOrderParamInstrument) {
      DefaultQueryOrderParamInstrument params = (DefaultQueryOrderParamInstrument) orderQueryParams[0];
      MEXCOrderDetail order = super.getOrder(
          MEXCAdapters.convertToMEXCSymbol(params.getInstrument().toString()),
          params.getOrderId());
      if(order != null){
        return Collections.singletonList(MEXCAdapters.adaptOrder(order));
      }
    }
    return new ArrayList<>(0);
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    if (orderParams instanceof DefaultCancelOrderByInstrumentAndIdParams) {
      DefaultCancelOrderByInstrumentAndIdParams params = (DefaultCancelOrderByInstrumentAndIdParams) orderParams;

      try {
        MEXCOrderCancelResponse mexcOrderCancelResponse = super.cancelOrder(
            MEXCAdapters.convertToMEXCSymbol(params.getInstrument().toString()),
            params.getOrderId());
        return mexcOrderCancelResponse.getSymbol() != null;
      } catch (MEXCException e) {
        throw new ExchangeException(e);
      }
    }
    return false;
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    try {
      MEXCOrderRequestPayload orderRequestPayload = MEXCAdapters.adaptMarketOrder(marketOrder);
      MEXCOrder order = placeOrder(orderRequestPayload);
      return order.getId();
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    throw new MEXCException("MEXC not supported OpenOrders,Please use getOpenOrders(OpenOrdersParams params)",999);
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    if (params instanceof DefaultOpenOrdersParamInstrument) {
      DefaultOpenOrdersParamInstrument instrumentParams = (DefaultOpenOrdersParamInstrument) params;
      String symbol = MEXCAdapters.convertToMEXCSymbol(instrumentParams.getInstrument().toString());
      return new OpenOrders(MEXCAdapters.adaptOpenOrders(super.getOpenOrders(symbol)));
    }
    return new OpenOrders(new ArrayList<>());
  }
}
