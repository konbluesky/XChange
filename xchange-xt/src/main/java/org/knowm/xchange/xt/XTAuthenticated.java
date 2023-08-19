package org.knowm.xchange.xt;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.knowm.xchange.xt.dto.XTResponse;
import org.knowm.xchange.xt.dto.account.BalanceResponse;
import org.knowm.xchange.xt.dto.account.WithdrawHistoryResponse;
import org.knowm.xchange.xt.dto.account.WithdrawRequest;
import org.knowm.xchange.xt.dto.trade.GetOrderResponse;
import org.knowm.xchange.xt.dto.trade.PlaceOrderRequest;
import org.knowm.xchange.xt.dto.ws.WebSocketToken;
import si.mazi.rescu.ParamsDigest;

import java.io.IOException;
import java.util.List;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Path("/v4")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
public interface XTAuthenticated {

  @POST
  @Path("/ws-token")
  XTResponse<WebSocketToken> getWsToken(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature
  ) throws IOException;


  @GET
  @Path("/balance")
  XTResponse<BalanceResponse> balance(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature,
      @QueryParam("currency") String currency
  ) throws IOException;


  @GET
  @Path("/balances")
  XTResponse<JsonNode> balances(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature
  ) throws IOException;


  @POST
  @Path("/order")
  @Consumes(MediaType.APPLICATION_JSON)
  XTResponse<JsonNode> placeOrder(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature,
      PlaceOrderRequest requestPayload
  ) throws IOException;

  @GET
  @Path("/order")
  XTResponse<GetOrderResponse> getOrder(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature,
      @QueryParam("orderId") Long orderId,
      @QueryParam("clientOrderId") String clientOrderId
  ) throws IOException;


  @DELETE
  @Path("/order/{orderId}")
  XTResponse<JsonNode> cancelOrder(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature,
      @PathParam("orderId") Long orderId
  ) throws IOException;

  @GET
  @Path("/open-order")
  XTResponse<List<GetOrderResponse>> getOpenOrders(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("bizType") String bizType,
      @QueryParam("side") String side
  ) throws IOException;


  @POST
  @Path("/withdraw")
  @Consumes(MediaType.APPLICATION_JSON)
  XTResponse<JsonNode> withdraw(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature,
      WithdrawRequest requestPayload
  ) throws IOException;

  @GET
  @Path("/withdraw/history")
  XTResponse<JsonNode> getWithdrawHistory(
      @HeaderParam("validate-algorithms") String validateAlgorithms,
      @HeaderParam("validate-appkey") String appkey,
      @HeaderParam("validate-recvwindow") String recvwindow,
      @HeaderParam("validate-timestamp") String timestamp,
      @HeaderParam("validate-signature") ParamsDigest signature,
      @QueryParam("currency") String currency,
      @QueryParam("chain") String chain,
      @QueryParam("status") String status,
      @QueryParam("fromId") Long fromId,
      @QueryParam("direction") String direction,
      @QueryParam("limit") int limit,
      @QueryParam("startTime") Long startTime,
      @QueryParam("endTime") Long endTime
  ) throws IOException;

}

