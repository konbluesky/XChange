package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTResilience;
import org.knowm.xchange.xt.dto.trade.GetOrderResponse;
import org.knowm.xchange.xt.dto.trade.PlaceOrderRequest;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTTradeServiceRaw extends XTBaseResilientExchangeService {

  public XTTradeServiceRaw(XTExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public String placeLimitOrder(PlaceOrderRequest request) throws IOException {
    try {
      JsonNode jsonNode = decorateApiCall(() -> xtAuthenticated.placeOrder(
          BaseParamsDigest.HMAC_SHA_256,
          apiKey,
          RECV_WINDOW,
          String.valueOf(System.currentTimeMillis()),
          signatureCreator,
          request).getData()).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();
      log.info("request:{}",request.toString());
      log.info("response:{}",jsonNode);
      if (jsonNode != null && jsonNode.has("orderId")) {
        return jsonNode.get("orderId").asText();
      } else {
        return null;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GetOrderResponse getOrder(Long orderId, String clientOrderId) throws IOException {
    return xtAuthenticated.getOrder(
        BaseParamsDigest.HMAC_SHA_256,
        apiKey,
        RECV_WINDOW,
        String.valueOf(System.currentTimeMillis()),
        signatureCreator,
        orderId,
        clientOrderId
    ).getData();
  }

  public String cancelOrder(Long orderId) throws IOException {
    JsonNode jsonNode = xtAuthenticated.cancelOrder(
        BaseParamsDigest.HMAC_SHA_256,
        apiKey,
        RECV_WINDOW,
        String.valueOf(System.currentTimeMillis()),
        signatureCreator,
        orderId
    ).getData();
    //{"rc":0,"mc":"SUCCESS","ma":[],"result":{"orderId":"248945504097270977","cancelId":"248956873475876544","clientCancelId":""}}
    return jsonNode.get("cancelId").asText();
  }

  public List<GetOrderResponse> getOpenOrders(String symbol, String bizType, String side)
      throws IOException {
    return xtAuthenticated.getOpenOrders(
        BaseParamsDigest.HMAC_SHA_256,
        apiKey,
        RECV_WINDOW,
        String.valueOf(System.currentTimeMillis()),
        signatureCreator,
        symbol,
        bizType,
        side
    ).getData();
  }


}
