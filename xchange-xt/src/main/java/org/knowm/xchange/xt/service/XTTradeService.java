package org.knowm.xchange.xt.service;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamInstrument;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.xt.XTAdapters;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.dto.trade.GetOrderResponse;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTTradeService extends XTTradeServiceRaw implements TradeService {

  public XTTradeService(XTExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    return placeLimitOrder(XTAdapters.adaptOrder(limitOrder, true));
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    return placeLimitOrder(XTAdapters.adaptOrder(marketOrder, false));
  }

  @Override
  public boolean cancelOrder(String orderId) throws IOException {
    String s = super.cancelOrder(Long.valueOf(orderId));
    return true;
  }

  @Override
  public Collection<Order> getOrder(String... orderIds) throws IOException {
    if (orderIds.length == 1) {
      GetOrderResponse order = super.getOrder(Long.valueOf(orderIds[0]), null);
      return Lists.newArrayList(XTAdapters.adaptOrder(order, null));
    } else {
      throw new RuntimeException("not support");
    }
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    return XTAdapters.adaptOpenOrders(super.getOpenOrders(null, null, null), null);
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    if (params instanceof OpenOrdersParamInstrument) {
      String instrumentId = XTAdapters.adaptInstrument(
          ((OpenOrdersParamInstrument) params).getInstrument());
      return XTAdapters.adaptOpenOrders(super.getOpenOrders(instrumentId, null, null), null);
    } else {
      throw new RuntimeException("not support");
    }
  }
}

