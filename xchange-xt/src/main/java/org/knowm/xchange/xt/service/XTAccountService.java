package org.knowm.xchange.xt.service;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;
import org.knowm.xchange.xt.XTAdapters;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.dto.account.XTDepositHistoryResponse;
import org.knowm.xchange.xt.dto.account.XTWithdrawHistoryResponse;
import org.knowm.xchange.xt.dto.account.XTWithdrawRequest;
import org.knowm.xchange.xt.dto.account.XTFundingHistoryParams;
import org.knowm.xchange.xt.dto.account.XTWithdrawFundsParams;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTAccountService extends XTAccountServiceRaw implements AccountService {

  public XTAccountService(XTExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    return new AccountInfo(XTAdapters.adaptWallet(balances()));
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    String chain = null;
    if (params instanceof XTWithdrawFundsParams) {
      if (((XTWithdrawFundsParams) params).getChain() != null) {
        chain = ((XTWithdrawFundsParams) params).getChain();
      }

      XTWithdrawFundsParams defaultParams = (XTWithdrawFundsParams) params;
      String address = defaultParams.getAddressTag() != null ? defaultParams.getAddress() + ":"
          + defaultParams.getAddressTag() : defaultParams.getAddress();

      XTWithdrawRequest XTWithdrawRequest = XTWithdrawRequest.builder()
          .amount(defaultParams.getAmount().toPlainString())
          .currency(defaultParams.getCurrency().getCurrencyCode())
          .chain(chain)
          .address(address)
          .build();
      String withdraw = withdraw(XTWithdrawRequest);
      if (!Strings.isNullOrEmpty(withdraw)) {
        return withdraw;
      }

      return null;
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }


  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    if (params instanceof XTFundingHistoryParams) {
      XTFundingHistoryParams xtFundingHistoryParams = (XTFundingHistoryParams) params;
      String currency = xtFundingHistoryParams.getCurrency();
      String chain = xtFundingHistoryParams.getChain();
      String status = xtFundingHistoryParams.getStatus();
      List<XTWithdrawHistoryResponse> withdrawHistory = getWithdrawHistory(currency, chain, status,
          null, null, 100,
          xtFundingHistoryParams.getStartTime(), xtFundingHistoryParams.getEndTime());
      List<XTDepositHistoryResponse> depositHistory = getDepositHistory(currency, chain, status, null,
          null, 100,
          xtFundingHistoryParams.getStartTime(), xtFundingHistoryParams.getEndTime());
      return XTAdapters.adaptWithdraws(withdrawHistory, depositHistory);
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }
}
