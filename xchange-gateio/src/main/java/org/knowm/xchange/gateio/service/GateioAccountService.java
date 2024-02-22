package org.knowm.xchange.gateio.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.dto.account.GateioDepositResponse;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawalPayload;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawalResponse;
import org.knowm.xchange.gateio.params.GateioDefaultWithdrawFundsParams;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultTradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

public class GateioAccountService extends GateioAccountServiceRaw implements AccountService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GateioAccountService(GateioExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    return new AccountInfo(GateioAdapters.adaptWalletForSpotAccount(super.getGateioAccountInfo()));
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (params instanceof GateioDefaultWithdrawFundsParams) {
      GateioDefaultWithdrawFundsParams gateIoParam = (GateioDefaultWithdrawFundsParams) params;
      GateioWithdrawalPayload payload = new GateioWithdrawalPayload();
      payload.setCurrency(gateIoParam.getCurrency().getCurrencyCode());
      payload.setAddress(gateIoParam.getAddress());
      payload.setAmount(gateIoParam.getAmount());
      payload.setMemo(gateIoParam.getMemo());
      payload.setChain(gateIoParam.getChain());
      GateioWithdrawalResponse response = super.withdraw(payload);
      return response.getId();
    }
    if (params instanceof DefaultWithdrawFundsParams) {
      DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;
      return withdrawFunds(
          defaultParams.getCurrency(), defaultParams.getAmount(), defaultParams.getAddress());
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }

  @Override
  public String requestDepositAddress(Currency currency, String... args) throws IOException {
    return super.getGateioDepositAddress(currency).getBaseAddress();
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    return new DefaultTradeHistoryParamsTimeSpan();
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    Date start = null;
    Date end = null;
    if (params instanceof TradeHistoryParamsTimeSpan) {
      TradeHistoryParamsTimeSpan timeSpan = (TradeHistoryParamsTimeSpan) params;
      start = timeSpan.getStartTime();
      end = timeSpan.getEndTime();
    }

    List<GateioWithdrawalResponse> gateioWithdrawls = super.getGateioWithdrawls(
        start.getTime() / 1000,
        end.getTime() / 1000);
    List<GateioDepositResponse> gateioDeposits = super.getGateioDeposits(start.getTime() / 1000,
        end.getTime() / 1000);

    List<FundingRecord> fundingRecords = GateioAdapters.adaptDepositsWithdrawals(gateioDeposits,
        gateioWithdrawls);
    return fundingRecords;
  }
}
