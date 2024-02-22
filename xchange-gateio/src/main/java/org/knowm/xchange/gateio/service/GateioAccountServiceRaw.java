package org.knowm.xchange.gateio.service;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.gateio.GateioAuthenticated;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.dto.account.GateioCancelWithdrawalResponse;
import org.knowm.xchange.gateio.dto.account.GateioDepositAddress;
import org.knowm.xchange.gateio.dto.account.GateioDepositResponse;
import org.knowm.xchange.gateio.dto.account.GateioSpotBalanceResponse;
import org.knowm.xchange.gateio.dto.account.GateioUnifiedAccount;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawalPayload;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawalResponse;

public class GateioAccountServiceRaw extends GateioBaseResilientExchangeService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GateioAccountServiceRaw(GateioExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public List<GateioSpotBalanceResponse> getGateioAccountInfo() throws IOException {
    return getGateioSpotAccount();
  }

  public GateioUnifiedAccount getGateioUnifiedAccount() throws IOException {
    try {
      return decorateApiCall(
          () -> gateioAuthenticated.getUnifiedAccount(this.apiKey, signatureCreator,
              timestampFactory)).withRateLimiter(rateLimiter(GateioAuthenticated.PATH_UNIFIED_ACCOUNTS))
          .call();
    } catch (Exception e) {
      throw e;
    }
  }

  public List<GateioSpotBalanceResponse> getGateioSpotAccount() throws IOException {
    try {
      return decorateApiCall(
          () -> gateioAuthenticated.getSpotAccount(this.apiKey, signatureCreator,
              timestampFactory)).withRateLimiter(rateLimiter(GateioAuthenticated.PATH_SPOT_ACCOUNTS))
          .call();
    } catch (Exception e) {
      throw e;
    }
  }


  public List<GateioWithdrawalResponse> getGateioWithdrawls(Long start,Long to) throws IOException {
    try {
      return decorateApiCall(
          () -> gateioAuthenticated.getWithdrawals(
              this.apiKey,
              signatureCreator,
              timestampFactory,
              null,
              start,
              to,
              null,
              null
          ))
          .withRateLimiter(rateLimiter(GateioAuthenticated.PATH_QUERY_WALLET_WITHDRAWALS)).call();
    } catch (Exception e) {
      throw e;
    }
  }

  public List<GateioDepositResponse> getGateioDeposits(Long start,Long to) throws IOException {
    try {
      return decorateApiCall(
          () -> gateioAuthenticated.getDeposits(
              this.apiKey,
              signatureCreator,
              timestampFactory,
              null,
              start,
              to,
              null,
              null
          ))
          .withRateLimiter(rateLimiter(GateioAuthenticated.PATH_QUERY_WALLET_DEPOSITS)).call();
    } catch (Exception e) {
      throw e;
    }
  }


  public GateioDepositAddress getGateioDepositAddress(Currency currency) throws IOException {
    GateioDepositAddress depositAddress =
        gateioAuthenticated.getDepositAddress(
            exchange.getExchangeSpecification().getApiKey(),
            signatureCreator,
            currency.getCurrencyCode());
    return depositAddress;
  }


  public GateioWithdrawalResponse withdraw(GateioWithdrawalPayload payload) throws IOException {
    try {
      GateioWithdrawalResponse response = decorateApiCall(
          () -> gateioAuthenticated.withdrawals(
              exchange.getExchangeSpecification().getApiKey(),
              signatureCreator,
              timestampFactory,
              payload
          )).withRateLimiter(rateLimiter(GateioAuthenticated.PATH_WITHDRAWALS))
          .call();
      return response;
    } catch (Exception e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GateioCancelWithdrawalResponse cancelWithdraw(String withdrawal_id) throws IOException {
    try {
      GateioCancelWithdrawalResponse result = decorateApiCall(
          () -> gateioAuthenticated.cancelWithdrawals(
              exchange.getExchangeSpecification().getApiKey(),
              signatureCreator,
              timestampFactory,
              withdrawal_id
          )).withRateLimiter(rateLimiter(GateioAuthenticated.PATH_WITHDRAWALS_CANCEL))
          .call();
      return result;
    } catch (Exception e) {
      throw new ExchangeException(e.getMessage());
    }
  }

}
