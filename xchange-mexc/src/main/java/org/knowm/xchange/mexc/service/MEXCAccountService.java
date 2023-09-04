package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.account.MEXCAccount;
import org.knowm.xchange.mexc.dto.account.MEXCBalance;
import org.knowm.xchange.mexc.dto.account.MEXCDepositHistory;
import org.knowm.xchange.mexc.dto.account.MEXCFundingHistoryParams;
import org.knowm.xchange.mexc.dto.account.MEXCWithDrawHistory;
import org.knowm.xchange.mexc.dto.account.MEXCWithdrawApply;
import org.knowm.xchange.mexc.dto.account.MEXCWithdrawFundsParams;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.HistoryParamsFundingType;
import org.knowm.xchange.service.trade.params.RippleWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrency;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

@Slf4j
public class MEXCAccountService extends MEXCAccountServiceRaw implements AccountService {

  public MEXCAccountService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    try {
      MEXCAccount walletBalances = getWalletBalances();
      List<MEXCBalance> balances = walletBalances.getBalances();
      return new AccountInfo(MEXCAdapters.adaptMEXCBalances(balances));
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {

    try {
      if (!(params instanceof MEXCWithdrawFundsParams)) {
        throw new IllegalArgumentException("DefaultWithdrawFundsParams must be provided.");
      }
      String withdraw = "";
      if (params instanceof RippleWithdrawFundsParams) {
        MEXCWithdrawFundsParams p = (MEXCWithdrawFundsParams) params;
        MEXCWithdrawApply apply = MEXCWithdrawApply.builder()
            .coin(p.getCurrency().getCurrencyCode())
            .amount(p.getAmount().toPlainString())
            .network(p.getNetwork())
            .address(p.getAddress())
            .build();
        withdraw = super.withdraw(apply);

      }
      return withdraw;
    } catch (MEXCException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    return new MEXCFundingHistoryParams();
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {

    String currency = "", after = "", before = "";

    if (params instanceof TradeHistoryParamCurrency) {
      TradeHistoryParamCurrency innerParams = (TradeHistoryParamCurrency) params;
      currency = innerParams.getCurrency() == null ? null
          : innerParams.getCurrency().getCurrencyCode();
    }

    if (params instanceof TradeHistoryParamsTimeSpan) {
      TradeHistoryParamsTimeSpan innerParams = (TradeHistoryParamsTimeSpan) params;

      after = innerParams.getEndTime() == null ? null
          : String.valueOf(innerParams.getEndTime().getTime());
      before = innerParams.getStartTime() == null ? null
          : String.valueOf(innerParams.getStartTime().getTime());
    }

    List<FundingRecord> result = new ArrayList<>();
    HistoryParamsFundingType innerParams = (HistoryParamsFundingType) params;
    if (innerParams.getType() == FundingRecord.Type.WITHDRAWAL) {

      List<MEXCWithDrawHistory> withDrawHistory = super.getWithDrawHistory(currency, null, after,
          before);
      return MEXCAdapters.adaptWithFundingRecords(withDrawHistory);

    } else if (innerParams.getType() == FundingRecord.Type.DEPOSIT) {

      List<MEXCDepositHistory> depositHistory = super.getDepositHistory(currency, null, after,
          before);
      return MEXCAdapters.adaptDepositFundingRecords(depositHistory);

    } else if (innerParams.getType() == null) {

      List<MEXCWithDrawHistory> withDrawHistory = super.getWithDrawHistory(currency, null, after,
          before);
      List<MEXCDepositHistory> depositHistory = super.getDepositHistory(currency, null, after,
          before);
      List<FundingRecord> fundingRecords = MEXCAdapters.adaptWithFundingRecords(withDrawHistory);

      fundingRecords.addAll(MEXCAdapters.adaptDepositFundingRecords(depositHistory));
      return fundingRecords;
    }
    return result;
  }
}
