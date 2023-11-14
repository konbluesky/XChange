package org.knowm.xchange.hashkey.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.hashkey.dto.HashKeyCancelOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyCreateOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyEnum;
import org.knowm.xchange.hashkey.dto.HashKeyOrderDetailResponse;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyTradeServiceRaw extends HashKeyBaseService {

  public static final int DEFAULT_LIMIT_ORDER_PARAM = 500;

  public HashKeyTradeServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public HashKeyCreateOrderResponse createOrder(String symbol, String side, String type,
      BigDecimal quantity, BigDecimal amount, BigDecimal price) throws Exception {
    if (HashKeyEnum.OrderType.LIMIT.name().equalsIgnoreCase(type)
        || HashKeyEnum.OrderType.LIMIT_MARKER.name().equalsIgnoreCase(type)) {
      return hashKeyAuthenticated.createOrder(apiKey, nonceFactory, signatureCreator, recWindow,
          symbol, side, type, quantity.toPlainString(), null, price.toPlainString(),
          String.valueOf(new Date().getTime()), null);
    }
    if (HashKeyEnum.OrderType.MARKET.name().equalsIgnoreCase(type)) {
      return hashKeyAuthenticated.createOrder(apiKey, nonceFactory, signatureCreator, recWindow,
          symbol, side, type, quantity != null ? quantity.toPlainString() : null,
          amount != null ? amount.toPlainString() : null,
          price != null ? price.toPlainString() : null, String.valueOf(new Date().getTime()), null);
    }
    return null;

  }

  public HashKeyOrderDetailResponse queryOrder(String orderId) throws IOException {
    return hashKeyAuthenticated.queryOrder(apiKey, nonceFactory, signatureCreator, recWindow,
        orderId, null);

  }

  public HashKeyCancelOrderResponse cancelOrderById(String orderId) throws IOException {
    return hashKeyAuthenticated.cancelOrder(apiKey, nonceFactory, signatureCreator, recWindow,
        orderId, null);

  }

  public List<HashKeyOrderDetailResponse> getOpenOrders(String orderId, String symbol,
      Integer limit) throws IOException {
    return hashKeyAuthenticated.getOpenOrders(apiKey, nonceFactory, signatureCreator, recWindow,
        orderId, symbol,
        limit == null || limit == 0 ? DEFAULT_LIMIT_ORDER_PARAM : limit);

  }

  public List<HashKeyOrderDetailResponse> getTradeOrders(String orderId, String symbol,
      Long startTime, Long endTime, Integer limit) throws IOException {
    return hashKeyAuthenticated.getTradeOrders(apiKey, nonceFactory, signatureCreator, recWindow,
        orderId,
        symbol,
        startTime != null ? startTime : 0,
        endTime != null ? endTime : 0,
        limit == null || limit == 0 ? DEFAULT_LIMIT_ORDER_PARAM : limit);
  }
}
