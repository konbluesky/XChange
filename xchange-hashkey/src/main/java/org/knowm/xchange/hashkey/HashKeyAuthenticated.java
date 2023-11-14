package org.knowm.xchange.hashkey;

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
import org.knowm.xchange.hashkey.dto.HashKeyAccountInfo;
import org.knowm.xchange.hashkey.dto.HashKeyCancelOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyCreateOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyOrderDetailResponse;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * <p> @Date : 2023/11/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
public interface HashKeyAuthenticated extends HashKey {

  @GET
  @Path("/account")
  HashKeyAccountInfo getAccountInformation(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("accountId") String accountId
  ) throws IOException, HashKeyException;


  @POST
  @Path("/spot/order")
  HashKeyCreateOrderResponse createOrder(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("recWindow") long recWindow,
      @QueryParam("symbol") String symbol,
      @QueryParam("side") String side,
      @QueryParam("type") String type,
      @QueryParam("quantity") String quantity,
      @QueryParam("amount") String amount,
      @QueryParam("price") String price,
      @QueryParam("newClientOrderId") String newClientOrderId,
      @QueryParam("timeInForce") String timeInForce
  ) throws IOException, HashKeyException;


  /**
   * 查询订单信息
   *
   * @param orderId           订单 ID
   * @param origClientOrderId 客户端订单 ID
   * @return 订单查询响应
   * @throws IOException      网络异常
   * @throws HashKeyException API异常
   */
  @GET
  @Path("/spot/order")
  HashKeyOrderDetailResponse queryOrder(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("recWindow") long recWindow,
      @QueryParam("orderId") String orderId,
      @QueryParam("origClientOrderId") String origClientOrderId
  ) throws IOException, HashKeyException;

  /**
   * 取消订单
   *
   * @param apiKey        API 密钥
   * @param timestamp     时间戳
   * @param signature     签名
   * @param recWindow     推荐窗口
   * @param orderId       订单 ID
   * @param clientOrderId 客户端订单 ID
   * @return 取消订单响应
   * @throws IOException      网络异常
   * @throws HashKeyException API异常
   */
  @DELETE
  @Path("/spot/order")
  HashKeyCancelOrderResponse cancelOrder(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("recWindow") long recWindow,
      @QueryParam("orderId") String orderId,
      @QueryParam("clientOrderId") String clientOrderId
  ) throws IOException, HashKeyException;

  /**
   * 查询当前未完成订单信息
   *
   * @param apiKey    API 密钥
   * @param timestamp 时间戳
   * @param signature 签名
   * @param recWindow 推荐窗口
   * @param orderId   订单 ID
   * @param symbol    交易对
   * @param limit     查询数量
   * @return 未完成订单查询响应
   * @throws IOException      网络异常
   * @throws HashKeyException API 异常
   */
  @GET
  @Path("/spot/openOrders")
  List<HashKeyOrderDetailResponse> getOpenOrders(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("recWindow") long recWindow,
      @QueryParam("orderId") String orderId,
      @QueryParam("symbol") String symbol,
      @QueryParam("limit") int limit
  ) throws IOException, HashKeyException;


  /**
   * 获取所有已交易订单
   *
   * @param apiKey    API 密钥
   * @param timestamp 时间戳
   * @param signature 签名
   * @param recWindow 推荐窗口
   * @param orderId   订单 ID
   * @param symbol    交易对
   * @param startTime 开始时间戳
   * @param endTime   结束时间戳
   * @param limit     数量限制
   * @return 已交易订单列表
   * @throws IOException      网络异常
   * @throws HashKeyException API异常
   */
  @GET
  @Path("/spot/tradeOrders")
  List<HashKeyOrderDetailResponse> getTradeOrders(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("recWindow") long recWindow,
      @QueryParam("orderId") String orderId,
      @QueryParam("symbol") String symbol,
      @QueryParam("startTime") long startTime,
      @QueryParam("endTime") long endTime,
      @QueryParam("limit") int limit
  ) throws IOException, HashKeyException;


}
