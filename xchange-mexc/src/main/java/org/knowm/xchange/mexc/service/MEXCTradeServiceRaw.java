package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderCancelResponse;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderDetail;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;

public class MEXCTradeServiceRaw extends MEXCBaseService {

  public MEXCTradeServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public MEXCOrder placeOrder(MEXCOrderRequestPayload request) throws IOException {
    return mexcAuthenticated.placeOrder(
        apiKey,
        nonceFactory,
        signatureCreator,
        request.getSymbol(),
        request.getSide(),
        request.getType(),
        request.getQuantity(),
        request.getQuoteOrderQty(),
        request.getPrice(),
        request.getNewClientOrderId()
    );
  }


  public MEXCOrderCancelResponse cancelOrder(String symbol, String orderId) throws IOException {
    return mexcAuthenticated.cancelOrder(
        apiKey,
        nonceFactory,
        signatureCreator,
        symbol,
        orderId
    );
  }

  public MEXCOrderDetail getOrder(String symbol, String id) throws IOException {
    return mexcAuthenticated.getOrder(apiKey, nonceFactory, signatureCreator, symbol, id);
  }

  public List<MEXCOrderDetail> getOpenOrders(String symbol) throws IOException {
    return mexcAuthenticated.getOpenOrders(apiKey, nonceFactory, signatureCreator, symbol);
  }

  public List<MEXCOrderDetail> getAllOrders(String symbol,
      String startTime,String endTime,String limit) throws IOException {
    return mexcAuthenticated.getAllOrders(
        apiKey,
        nonceFactory,
        signatureCreator,
        symbol,
        startTime,
        endTime,
        limit==null?"500":limit
    );
  }


}
