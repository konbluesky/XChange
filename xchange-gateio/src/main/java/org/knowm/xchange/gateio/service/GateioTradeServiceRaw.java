package org.knowm.xchange.gateio.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gateio.GateioAuthenticated;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.GateioUtils;
import org.knowm.xchange.gateio.dto.GateioEnums;
import org.knowm.xchange.gateio.dto.GateioEnums.AccountType;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderType;
import org.knowm.xchange.gateio.dto.GateioEnums.TimeInForce;
import org.knowm.xchange.gateio.dto.trade.GateioOpenOrders;
import org.knowm.xchange.gateio.dto.trade.GateioOrder;
import org.knowm.xchange.gateio.dto.trade.GateioPlaceOrderPayload;
import org.knowm.xchange.gateio.dto.trade.GateioTradeHistoryReturn;

public class GateioTradeServiceRaw extends GateioBaseResilientExchangeService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GateioTradeServiceRaw(GateioExchange exchange, ResilienceRegistries resilienceRegistries) {

    super(exchange, resilienceRegistries);
  }

  public GateioOrder placeLimitOrder(CurrencyPair currencyPair,
      GateioEnums.OrderSide orderSide, BigDecimal price, BigDecimal amount, TimeInForce timeInForce)
      throws IOException {
    GateioPlaceOrderPayload payload = new GateioPlaceOrderPayload();
    payload.setCurrencyPair(GateioUtils.toPairString(currencyPair).toUpperCase());
    payload.setType(OrderType.LIMIT);
    payload.setSide(orderSide);
    payload.setAccount(AccountType.SPOT.name());
    payload.setAmount(amount.toPlainString());
    payload.setPrice(price.toPlainString());
    payload.setTimeInForce(timeInForce);
    return placeOrder(payload);
  }

  public GateioOrder placeMarketOrder(
      CurrencyPair currencyPair,
      GateioEnums.OrderSide orderSide, BigDecimal amount, TimeInForce timeInForce)
      throws IOException {
    GateioPlaceOrderPayload payload = new GateioPlaceOrderPayload();
    payload.setCurrencyPair(GateioUtils.toPairString(currencyPair).toUpperCase());
    payload.setType(OrderType.MARKET);
    payload.setSide(orderSide);
    payload.setAccount(AccountType.SPOT.name());
    payload.setAmount(amount.toPlainString());
    payload.setTimeInForce(timeInForce);
    return placeOrder(payload);
  }

  /**
   * 最终下单方法
   *
   * @param payload
   * @return
   */
  public GateioOrder placeOrder(GateioPlaceOrderPayload payload) throws IOException {
    try {

      ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
      objectNode.put("text", payload.getText());
      objectNode.put("currency_pair", payload.getCurrencyPair());
      objectNode.put("type", payload.getType().name().toLowerCase());
      objectNode.put("account", payload.getAccount().toLowerCase());
      objectNode.put("side", payload.getSide().name().toLowerCase());
      objectNode.put("amount", payload.getAmount());
      objectNode.put("price", payload.getPrice());
      objectNode.put("time_in_force", payload.getTimeInForce().name().toLowerCase());

      return decorateApiCall(
          () -> gateioAuthenticated.placeOrder(apiKey, signatureCreator, timestampFactory,
              objectNode)).
          withRateLimiter(rateLimiter(GateioAuthenticated.PATH_POST_SPOT_ORDER)).
          call();

//      return decorateApiCall(
//          () -> gateioAuthenticated.placeOrder(apiKey, signatureCreator, timestampFactory,
//              payload)).
//          withRateLimiter(rateLimiter(GateioAuthenticated.PATH_SPOT_ORDERS)).
//          call();

    } catch (Exception e) {
      throw e;
    }
  }

  public GateioOrder cancelOrder(String orderId, CurrencyPair currencyPair) throws IOException {
    GateioOrder order = decorateApiCall(
        () -> gateioAuthenticated.cancelOrder(apiKey, signatureCreator, timestampFactory,
            orderId, GateioUtils.toPairString(currencyPair))).
        withRateLimiter(rateLimiter(GateioAuthenticated.PATH_SPOT_ORDERS_PARAM_ID)).
        call();
    return order;
  }

  public GateioOrder getOrder(String orderId, CurrencyPair currencyPair) throws IOException {
    GateioOrder order = decorateApiCall(
        () -> gateioAuthenticated.getOrder(apiKey, signatureCreator, timestampFactory,
            orderId, GateioUtils.toPairString(currencyPair))).
        withRateLimiter(rateLimiter(GateioAuthenticated.PATH_SPOT_ORDERS_PARAM_ID)).
        call();
    return order;
  }

  public List<GateioOpenOrders> getGateioOpenOrders() throws IOException {
    List<GateioOpenOrders> orders = decorateApiCall(
        () -> gateioAuthenticated.getOpenList(apiKey, signatureCreator, timestampFactory,
            null, null, "spot")).
        withRateLimiter(rateLimiter(GateioAuthenticated.PATH_SPOT_ORDERS_PARAM_ID)).
        call();
    return orders;
  }

  public GateioTradeHistoryReturn getGateioTradeHistory(CurrencyPair currencyPair)
      throws IOException {

    GateioTradeHistoryReturn gateioTradeHistoryReturn =
        gateioAuthenticated.getUserTradeHistory(
            apiKey, signatureCreator, GateioUtils.toPairString(currencyPair));

    return handleResponse(gateioTradeHistoryReturn);
  }

}
