package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTResilience;
import org.knowm.xchange.xt.dto.XTResponse;
import org.knowm.xchange.xt.dto.account.XTBalanceResponse;
import org.knowm.xchange.xt.dto.account.XTDepositHistoryResponse;
import org.knowm.xchange.xt.dto.account.XTWithdrawHistoryResponse;
import org.knowm.xchange.xt.dto.account.XTWithdrawRequest;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTAccountServiceRaw extends XTBaseResilientExchangeService {

  public XTAccountServiceRaw(XTExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public XTBalanceResponse balance(String currency) throws IOException {
    try {
      XTResponse<XTBalanceResponse> call = decorateApiCall(() -> xtAuthenticated.balance(
          BaseParamsDigest.HMAC_SHA_256,
          apiKey,
          RECV_WINDOW,
          String.valueOf(System.currentTimeMillis()),
          signatureCreator,
          currency)).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();
      return safeGetResponse(call);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<XTBalanceResponse> balances() throws IOException {
    try {
      XTResponse<JsonNode> call = decorateApiCall(() -> xtAuthenticated.balances(
          BaseParamsDigest.HMAC_SHA_256,
          apiKey,
          RECV_WINDOW,
          String.valueOf(System.currentTimeMillis()),
          signatureCreator)).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE))
          .call();
      JsonNode jsonNode = safeGetResponse(call);
      return mapper.treeToValue(jsonNode.get("assets"), mapper.getTypeFactory()
          .constructCollectionType(List.class, XTBalanceResponse.class));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public String time() throws IOException {
    return decorateApiCall(() -> xt.time().getData().get("serverTime").toString()).withRateLimiter(
        rateLimiter(XTResilience.IP_RATE_TYPE)).call();
  }

  public String withdraw(XTWithdrawRequest request) throws IOException {
    try {
      XTResponse<JsonNode> call = decorateApiCall(() -> xtAuthenticated.withdraw(
          BaseParamsDigest.HMAC_SHA_256,
          apiKey,
          RECV_WINDOW,
          String.valueOf(System.currentTimeMillis()),
          signatureCreator,
          request
      )).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();
      JsonNode jsonNode = safeGetResponse(call);
      return jsonNode.get("id").asText();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<XTWithdrawHistoryResponse> getWithdrawHistory(
      String currency,
      String chain,
      String status,
      Long fromId,
      String direction,
      int limit,
      Long startTime,
      Long endTime
  ) throws IOException {
    try {
      XTResponse<JsonNode> call = decorateApiCall(
          () -> xtAuthenticated.getWithdrawHistory(
              BaseParamsDigest.HMAC_SHA_256,
              apiKey,
              RECV_WINDOW,
              String.valueOf(System.currentTimeMillis()),
              signatureCreator,
              currency,
              chain,
              status,
              fromId,
              direction,
              limit,
              startTime,
              endTime
          )).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE))
          .call();
      JsonNode jsonNode = safeGetResponse(call);
      return mapper.treeToValue(jsonNode.get("items"), mapper.getTypeFactory()
          .constructCollectionType(List.class, XTWithdrawHistoryResponse.class));
    } catch (Exception e) {
      throw e;
    }
  }

  public List<XTDepositHistoryResponse> getDepositHistory(
      String currency,
      String chain,
      String status,
      Long fromId,
      String direction,
      int limit,
      Long startTime,
      Long endTime
  ) throws IOException {

    try {
      XTResponse<JsonNode> call = decorateApiCall(() -> xtAuthenticated.getDepositHistory(
          BaseParamsDigest.HMAC_SHA_256,
          apiKey,
          RECV_WINDOW,
          String.valueOf(System.currentTimeMillis()),
          signatureCreator,
          currency,
          chain,
          status,
          fromId,
          direction,
          limit,
          startTime,
          endTime
      )).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();

      JsonNode jsonNode = safeGetResponse(call);
      if (jsonNode.hasNonNull("items")) {
        return mapper.treeToValue(jsonNode.get("items"), mapper.getTypeFactory()
            .constructCollectionType(List.class, XTDepositHistoryResponse.class));
      } else {
        return Lists.newArrayList();
      }
    } catch (Exception e) {
      throw e;
    }

  }


}
