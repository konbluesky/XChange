package org.knowm.xchange.mexc;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.account.MEXCAccount;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCDepositHistory;
import org.knowm.xchange.mexc.dto.account.MEXCWithDrawHistory;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderCancelResponse;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderDetail;
import org.knowm.xchange.mexc.service.MEXCException;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

@Path("/api/v3")
@Produces(MediaType.APPLICATION_JSON)
public interface MEXCAuthenticated extends MEXC {

  @GET
  @Path("/account")
  MEXCAccount getWalletBalances(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature
  ) throws IOException, MEXCException;

  @POST
  @Path("/order")
  MEXCOrder placeOrder(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("side") String side,
      @QueryParam("type") String type,
      @QueryParam("quantity") String quantity,
      @QueryParam("quoteOrderQty") String quoteOrderQty,
      @QueryParam("price") String price,
      @QueryParam("newClientOrderId") String newClientOrderId) throws IOException, MEXCException;


  @DELETE
  @Path("/order")
  MEXCOrderCancelResponse cancelOrder(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("orderId") String orderId
  ) throws IOException, MEXCException;

  @GET
  @Path("/order")
  MEXCOrderDetail getOrder(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("orderId") String orderId) throws IOException, MEXCException;

  @GET
  @Path("/openOrders")
  List<MEXCOrderDetail> getOpenOrders(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("symbol") String symbol) throws IOException, MEXCException;

  @GET
  @Path("/allOrders")
  List<MEXCOrderDetail> getAllOrders(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("startTime") String startTime,
      @QueryParam("endTime") String endTime,
      @QueryParam("limit") String limit) throws IOException, MEXCException;

  @GET
  @Path("/selfSymbols")
  MEXCResult<List<String>> getSelfSymbols(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature
  ) throws IOException, MEXCException;


  @DELETE
  @Path("/capital/withdraw")
  JsonNode cancelWithdraw(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("id") String id
  ) throws IOException, MEXCException;

  @POST
  @Path("/capital/withdraw/apply")
  JsonNode withdraw(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("coin") String coinId,
      @QueryParam("withdrawOrderId") String withdrawOrderId,
      @QueryParam("network") String network,
      @QueryParam("address") String address,
      @QueryParam("memo") String memo,
      @QueryParam("amount") String amount,
      @QueryParam("remark") String remark
  ) throws IOException, MEXCException;

  @GET
  @Path("/capital/config/getall")
  List<MEXCConfig> getCoinConfig(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature
  ) throws IOException, MEXCException;


  @GET
  @Path("/capital/deposit/hisrec")
  List<MEXCDepositHistory> getDepositHistory(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("coin") String coinId,
      @QueryParam("status") String status,
      @QueryParam("startTime") String startTime,
      @QueryParam("endTime") String endTime
  ) throws IOException, MEXCException;

  @GET
  @Path("/capital/withdraw/history")
  List<MEXCWithDrawHistory> getWithDrawHistory(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("coin") String coinId,
      @QueryParam("status") String status,
      @QueryParam("startTime") String startTime,
      @QueryParam("endTime") String endTime
  ) throws IOException, MEXCException;


}