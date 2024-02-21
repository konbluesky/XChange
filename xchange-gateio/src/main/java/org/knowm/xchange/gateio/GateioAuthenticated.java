package org.knowm.xchange.gateio;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.gateio.dto.GateioBaseResponse;
import org.knowm.xchange.gateio.dto.account.GateioCancelWithdrawalResponse;
import org.knowm.xchange.gateio.dto.account.GateioSpotBalanceResponse;
import org.knowm.xchange.gateio.dto.account.GateioUnifiedAccount;
import org.knowm.xchange.gateio.dto.account.GateioDepositAddress;
import org.knowm.xchange.gateio.dto.account.GateioDepositsWithdrawals;
import org.knowm.xchange.gateio.dto.account.GateioFunds;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawStatus;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawalPayload;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawalResponse;
import org.knowm.xchange.gateio.dto.marketdata.GateioFeeInfo;
import org.knowm.xchange.gateio.dto.trade.GateioOpenOrders;
import org.knowm.xchange.gateio.dto.trade.GateioOrder;
import org.knowm.xchange.gateio.dto.trade.GateioOrderStatus;
import org.knowm.xchange.gateio.dto.trade.GateioPlaceOrderReturn;
import org.knowm.xchange.gateio.dto.trade.GateioTradeHistoryReturn;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

@Path("api/v4")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public interface GateioAuthenticated {


  String PATH_UNIFIED_ACCOUNTS = "/unified/accounts";
  String PATH_SPOT_ACCOUNTS = "/spot/accounts";
  String PATH_SPOT_ORDERS = "/spot/orders";
  String PATH_SPOT_OPEN_ORDERS = "/spot/open_orders";
  String PATH_SPOT_ORDERS_PARAM_ID = "/spot/orders/{order_id}";
  String PATH_WITHDRAWALS = "/withdrawals";
  String PATH_WITHDRAWALS_CANCEL = "/withdrawals/{withdrawal_id}";
  String PATH_WALLET_WITHDRAW_STATUS = "/wallet/withdraw_status";

  Map<String, List<Integer>> privatePathRateLimits =
      new HashMap<String, List<Integer>>() {
        {
          put(PATH_UNIFIED_ACCOUNTS, Arrays.asList(80,10));
          put(PATH_SPOT_ACCOUNTS, Arrays.asList(80, 10));
          put(PATH_WITHDRAWALS_CANCEL, Arrays.asList(80, 10));
          put(PATH_WALLET_WITHDRAW_STATUS, Arrays.asList(80, 10));
        }
      };


  @GET
  @Path(PATH_UNIFIED_ACCOUNTS)
  GateioUnifiedAccount getUnifiedAccount(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp
  ) throws IOException;

  /**
   * 获取现货账户余额
   * @param apiKey
   * @param signer
   * @param timestamp
   * @return
   */
  @GET
  @Path(PATH_SPOT_ACCOUNTS)
  List<GateioSpotBalanceResponse> getSpotAccount(@HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp);

  /**
   * 发起提现请求
   * @param apiKey
   * @param signer
   * @param timestamp
   * @param withdrawalPayload
   * @return
   * @throws IOException
   */
  @POST
  @Path(PATH_WITHDRAWALS)
  @Consumes(MediaType.APPLICATION_JSON)
  GateioWithdrawalResponse withdrawals(@HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      GateioWithdrawalPayload withdrawalPayload) throws IOException;


  /**
   * 取消提现
   * @param apiKey
   * @param signer
   * @param timestamp
   * @param withdrawal_id
   * @return
   * @throws IOException
   */
  @DELETE
  @Path(PATH_WITHDRAWALS_CANCEL)
  GateioCancelWithdrawalResponse cancelWithdrawals(@HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      @PathParam("withdrawal_id") String withdrawal_id) throws IOException;

//  @POST
//  @Path(PATH_SPOT_ORDERS)
//  @Consumes(MediaType.APPLICATION_JSON)
//  GateioOrder placeOrder(
//      @HeaderParam("KEY") String apiKey,
//      @HeaderParam("SIGN") ParamsDigest signer,
//      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
//      GateioPlaceOrderPayload payload) throws IOException;


  @GET
  @Path(PATH_SPOT_ORDERS)
  @Consumes(MediaType.APPLICATION_JSON)
  GateioOrder getOrderList(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      JsonNode payload
  ) throws IOException;

  @GET
  @Path(PATH_SPOT_OPEN_ORDERS)
  List<GateioOpenOrders> getOpenList(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("page") Integer page,
      @QueryParam("limit") Integer limit,
      @QueryParam("account") String account
  ) throws IOException;

  /**
   * 下单
   * @param apiKey
   * @param signer
   * @param timestamp
   * @param payload
   * @return
   * @throws IOException
   */
  @POST
  @Path(PATH_SPOT_ORDERS)
  @Consumes(MediaType.APPLICATION_JSON)
  GateioOrder placeOrder(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      JsonNode payload
      ) throws IOException;

  @DELETE
  @Path(PATH_SPOT_ORDERS_PARAM_ID)
  GateioOrder cancelOrder(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      @PathParam("order_id") String order_id,
      @QueryParam("currency_pair") String currency_pair) throws IOException;

  @GET
  @Path(PATH_SPOT_ORDERS_PARAM_ID)
  GateioOrder getOrder(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      @PathParam("order_id") String order_id,
      @QueryParam("currency_pair") String currency_pair) throws IOException;

  /**
   * 官方叫查询提现状态， 实际是查询提现配置
   * @param apiKey
   * @param signer
   * @param timestamp
   * @param currency
   * @return
   * @throws IOException
   */
  @GET
  @Path(PATH_WALLET_WITHDRAW_STATUS)
  List<GateioWithdrawStatus> getWithDrawConfig(@HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp,
      @FormParam("currency") String currency) throws IOException;


  @POST
  @Path("private/balances")
  GateioFunds getFunds(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @HeaderParam("Timestamp") SynchronizedValueFactory<Long> timestamp
      )
      throws IOException;

  @POST
  @Path("private/depositAddress")
  GateioDepositAddress getDepositAddress(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("currency") String currency)
      throws IOException;

  @POST
  @Path("private/withdraw")
  GateioBaseResponse withdraw(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("currency") String currency,
      @FormParam("amount") BigDecimal amount,
      @FormParam("address") String address)
      throws IOException;

  @POST
  @Path("private/cancelorder")
  GateioBaseResponse cancelOrder(
      @FormParam("orderNumber") String orderNumber,
      @FormParam("currencyPair") String currencyPair,
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer)
      throws IOException;

  @POST
  @Path("private/cancelAllOrders")
  GateioBaseResponse cancelAllOrders(
      @FormParam("type") String type,
      @FormParam("currencyPair") String currencyPair,
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer)
      throws IOException;

  @POST
  @Path("private/buy")
  GateioPlaceOrderReturn buy(
      @FormParam("currencyPair") String currencyPair,
      @FormParam("rate") BigDecimal rate,
      @FormParam("amount") BigDecimal amount,
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer)
      throws IOException;

  @POST
  @Path("private/sell")
  GateioPlaceOrderReturn sell(
      @FormParam("currencyPair") String currencyPair,
      @FormParam("rate") BigDecimal rate,
      @FormParam("amount") BigDecimal amount,
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer)
      throws IOException;

  @POST
  @Path("private/openOrders")
  GateioOpenOrders getOpenOrders(
      @HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer)
      throws IOException;

  @POST
  @Path("private/tradeHistory")
  GateioTradeHistoryReturn getUserTradeHistory(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("currencyPair") String currencyPair)
      throws IOException;

  @POST
  @Path("private/depositsWithdrawals")
  GateioDepositsWithdrawals getDepositsWithdrawals(
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer,
      @FormParam("start") Long startUnixTime,
      @FormParam("end") Long endUnixTime)
      throws IOException;

  @POST
  @Path("private/getorder")
  GateioOrderStatus getOrderStatus(
      @FormParam("orderNumber") String orderNumber,
      @FormParam("currencyPair") String currencyPair,
      @HeaderParam("KEY") String apiKey,
      @HeaderParam("SIGN") ParamsDigest signer)
      throws IOException;

  @POST
  @Path("private/feelist")
  Map<String, GateioFeeInfo> getFeeList(
      @HeaderParam("KEY") String apiKey, @HeaderParam("SIGN") ParamsDigest signer)
      throws IOException;

}
