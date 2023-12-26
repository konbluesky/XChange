package org.knowm.xchange.mexc.service;

import static org.knowm.xchange.mexc.MEXCResilience.REST_UID_RATE_LIMITER;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.account.MEXCAccount;
import org.knowm.xchange.mexc.dto.account.MEXCDepositHistory;
import org.knowm.xchange.mexc.dto.account.MEXCWithDrawHistory;
import org.knowm.xchange.mexc.dto.account.MEXCWithdrawApply;

public class MEXCAccountServiceRaw extends MEXCBaseService {

  public MEXCAccountServiceRaw(MEXCExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public MEXCAccount getWalletBalances() throws IOException {
    return decorateApiCall(() -> mexcAuthenticated.getWalletBalances(apiKey, nonceFactory, signatureCreator,DEFAULT_RECV_WINDOW))
        .withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public String cancelWithdraw(String withdrawId) throws IOException {
    return decorateApiCall(() -> {
      JsonNode jsonNode = mexcAuthenticated.cancelWithdraw(apiKey, nonceFactory, signatureCreator, withdrawId);
      return jsonNode.get("id").asText();
    }).withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public String withdraw(MEXCWithdrawApply apply) throws IOException {
    return decorateApiCall(() ->
        mexcAuthenticated.withdraw(
                apiKey,
                nonceFactory,
                signatureCreator,
                apply.getCoin(),
                apply.getWithdrawOrderId(),
                apply.getNetwork(),
                apply.getAddress(),
                apply.getMemo(),
                apply.getAmount(),
                apply.getRemark())
            .get("id").asText()
    ).withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public List<MEXCDepositHistory> getDepositHistory(
      String coinId,
      String status,
      String startTime,
      String endTime
  ) throws IOException {
    return decorateApiCall(() ->
        mexcAuthenticated.getDepositHistory(
            apiKey,
            nonceFactory,
            signatureCreator,
            coinId,
            status,
            startTime,
            endTime)
    ).withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }

  public List<MEXCWithDrawHistory> getWithDrawHistory(
      String coinId,
      String status,
      String startTime,
      String endTime
  ) throws IOException {
    return decorateApiCall(() ->
        mexcAuthenticated.getWithDrawHistory(
            apiKey,
            nonceFactory,
            signatureCreator,
            coinId,
            status,
            startTime,
            endTime)
    ).withRateLimiter(rateLimiter(REST_UID_RATE_LIMITER))
        .call();
  }
}
