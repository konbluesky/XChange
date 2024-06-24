package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTResilience;
import org.knowm.xchange.xt.dto.account.BalanceResponse;
import org.knowm.xchange.xt.dto.account.DepositHistoryResponse;
import org.knowm.xchange.xt.dto.account.WithdrawHistoryResponse;
import org.knowm.xchange.xt.dto.account.WithdrawRequest;
import org.knowm.xchange.xt.dto.account.XTFundingHistoryRequest;

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

  public BalanceResponse balance(String currency) throws IOException {
    try {
      return decorateApiCall(() -> xtAuthenticated.balance(
          BaseParamsDigest.HMAC_SHA_256,
          apiKey,
          RECV_WINDOW,
          String.valueOf(System.currentTimeMillis()),
          signatureCreator,
          currency).getData()).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<BalanceResponse> balances() throws IOException {
    try {
      JsonNode jsonNode = decorateApiCall(() -> xtAuthenticated.balances(
          BaseParamsDigest.HMAC_SHA_256,
          apiKey,
          RECV_WINDOW,
          String.valueOf(System.currentTimeMillis()),
          signatureCreator).getData()).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE))
          .call();
      return mapper.treeToValue(jsonNode.get("assets"), mapper.getTypeFactory()
          .constructCollectionType(List.class, BalanceResponse.class));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public String time() throws IOException {
    return decorateApiCall(() -> xt.time().getData().get("serverTime").toString()).withRateLimiter(
        rateLimiter(XTResilience.IP_RATE_TYPE)).call();
  }

  public String withdraw(WithdrawRequest request) throws IOException {
    JsonNode jsonNode = decorateApiCall(() -> xtAuthenticated.withdraw(
        BaseParamsDigest.HMAC_SHA_256,
        apiKey,
        RECV_WINDOW,
        String.valueOf(System.currentTimeMillis()),
        signatureCreator,
        request
    ).getData()).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();
    return jsonNode.get("id").asText();
  }

  public List<WithdrawHistoryResponse> getWithdrawHistory(
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
      JsonNode jsonNode = decorateApiCall(
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
          ).getData()).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE))
          .call();
      return mapper.treeToValue(jsonNode.get("items"), mapper.getTypeFactory()
          .constructCollectionType(List.class, WithdrawHistoryResponse.class));
    } catch (Exception e) {
      throw e;
    }
  }

  public List<DepositHistoryResponse> getDepositHistory(
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
//      XTFundingHistoryRequest request=XTFundingHistoryRequest.builder()
//          .currency(currency)
//          .chain(chain)
//          .status(status)
//          .fromId(fromId)
//          .startTime(startTime)
//          .endTime(endTime)
//          .limit(limit)
//          .direction(direction)
//          .build();

      JsonNode jsonNode = decorateApiCall(() -> xtAuthenticated.getDepositHistory(
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
      ).getData()).withRateLimiter(rateLimiter(XTResilience.API_RATE_TYPE)).call();
      if(jsonNode.hasNonNull("items")){
        return mapper.treeToValue(jsonNode.get("items"), mapper.getTypeFactory()
            .constructCollectionType(List.class, DepositHistoryResponse.class));
      }else {
        return Lists.newArrayList();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }


}
