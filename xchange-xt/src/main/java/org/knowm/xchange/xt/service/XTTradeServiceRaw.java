package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.dto.trade.GetOrderResponse;
import org.knowm.xchange.xt.dto.trade.PlaceOrderRequest;

import java.io.IOException;
import java.util.List;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTTradeServiceRaw extends XTBaseService {
    /**
     * Constructor
     *
     * @param exchange
     */
    public XTTradeServiceRaw(Exchange exchange) {
        super(exchange);
    }


    public String placeLimitOrder(PlaceOrderRequest request) throws IOException {
        JsonNode jsonNode = xtAuthenticated.placeOrder(
                BaseParamsDigest.HMAC_SHA_256,
                apiKey,
                RECV_WINDOW,
                String.valueOf(System.currentTimeMillis()),
                signatureCreator,
                request).getData();
        return jsonNode.get("orderId").asText();
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

    public List<GetOrderResponse> getOpenOrders(String symbol, String bizType, String side) throws IOException {
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
