package org.knowm.xchange.mexc.service;

import static org.knowm.xchange.mexc.MEXCResilience.REST_UID_RATE_LIMITER;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderCancelResponse;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderDetail;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;

public class MEXCTradeServiceRaw extends MEXCBaseService {

  public MEXCTradeServiceRaw(MEXCExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange,resilienceRegistries);
  }

  public MEXCOrder placeOrder(MEXCOrderRequestPayload request) throws IOException {
    return decorateApiCall(() -> mexcAuthenticated.placeOrder(
        apiKey,
        nonceFactory,
        signatureCreator,
        DEFAULT_RECV_WINDOW,
        request.getSymbol(),
        request.getSide(),
        request.getType(),
        request.getQuantity(),
        request.getQuoteOrderQty(),
        request.getPrice(),
        request.getNewClientOrderId()))
        .withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public MEXCOrderCancelResponse cancelOrder(String symbol, String orderId) throws IOException {
    return decorateApiCall(() -> mexcAuthenticated.cancelOrder(
        apiKey,
        nonceFactory,
        signatureCreator,
        DEFAULT_RECV_WINDOW,
        symbol,
        orderId))
        .withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public List<String> selfSymbols() throws IOException {
    return decorateApiCall(() -> mexcAuthenticated.getSelfSymbols(
        apiKey,
        nonceFactory,
        signatureCreator).getData())
        .withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public MEXCOrderDetail getOrder(String symbol, String id) throws IOException {
    return decorateApiCall(() -> mexcAuthenticated.getOrder(
        apiKey,
        nonceFactory,
        signatureCreator,
        DEFAULT_RECV_WINDOW,
        symbol,
        id))
        .withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public List<MEXCOrderDetail> getOpenOrders(String symbol) throws IOException {
    return decorateApiCall(() -> mexcAuthenticated.getOpenOrders(
        apiKey,
        nonceFactory,
        signatureCreator,
        DEFAULT_RECV_WINDOW,
        symbol))
        .withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public List<MEXCOrderDetail> getAllOrders(String symbol,
      String startTime, String endTime, String limit) throws IOException {
    return decorateApiCall(() -> mexcAuthenticated.getAllOrders(
        apiKey,
        nonceFactory,
        signatureCreator,
        DEFAULT_RECV_WINDOW,
        symbol,
        startTime,
        endTime,
        limit == null ? "500" : limit))
        .withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }
}
