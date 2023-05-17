package org.knowm.xchange.okex.service;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.okex.OkexAdapters;
import org.knowm.xchange.okex.OkexExchange;
import org.knowm.xchange.okex.dto.OkexException;
import org.knowm.xchange.okex.dto.OkexResponse;
import org.knowm.xchange.okex.dto.account.*;
import org.knowm.xchange.okex.service.params.OkexFundingHistoryParams;
import org.knowm.xchange.okex.service.params.OkexWithdrawFundsParams;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.HistoryParamsFundingType;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

import java.io.IOException;
import java.util.List;

/** Author: Max Gao (gaamox@tutanota.com) Created: 08-06-2021 */
public class OkexAccountService extends OkexAccountServiceRaw implements AccountService {

  public OkexAccountService(OkexExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public AccountInfo getAccountInfo() throws IOException {
    // null to get assets (with non-zero balance), remaining balance, and available amount in the
    // account.
    OkexResponse<List<OkexWalletBalance>> tradingBalances = getWalletBalances(null);
    OkexResponse<List<OkexAssetBalance>> assetBalances = getAssetBalances(null);
    OkexResponse<List<OkexAccountPositionRisk>> positionRis = getAccountPositionRisk();
    return new AccountInfo(
        OkexAdapters.adaptOkexBalances(tradingBalances.getData()),
        OkexAdapters.adaptOkexAssetBalances(assetBalances.getData()),
        OkexAdapters.adaptOkexAccountPositionRisk(positionRis.getData())
    );
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
      String chain=null;
      String clientId=null;
      if (params instanceof OkexWithdrawFundsParams) {
          if (((OkexWithdrawFundsParams) params).getChain() != null) {
              chain = ((OkexWithdrawFundsParams) params).getChain();
          }
          if (((OkexWithdrawFundsParams) params).getClientId() != null) {
              clientId = ((OkexWithdrawFundsParams) params).getClientId();
      }
      OkexWithdrawFundsParams defaultParams = (OkexWithdrawFundsParams) params;
      String address = defaultParams.getAddressTag() != null ? defaultParams.getAddress() + ":" + defaultParams.getAddressTag() : defaultParams.getAddress();
      OkexResponse<List<OkexWithdrawalResponse>> okexResponse = assetWithdrawal(
              defaultParams.getCurrency().getCurrencyCode(),
              defaultParams.getAmount().toPlainString(),
              ON_CHAIN_METHOD,
              address,
              defaultParams.getCommission() != null ? defaultParams.getCommission().toPlainString() : null,
              chain,
              clientId
      );
      if (!okexResponse.isSuccess())
        throw new OkexException(okexResponse.getMsg(), Integer.parseInt(okexResponse.getCode()));

      return okexResponse.getData().get(0).getWithdrawalId();
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }

    @Override
    public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
        if (params instanceof OkexFundingHistoryParams) {

            OkexFundingHistoryParams innerParams = (OkexFundingHistoryParams) params;
            String currency = innerParams.getCurrency() == null ? null : innerParams.getCurrency().getCurrencyCode();
            String after = innerParams.getAfter() == null ? null : String.valueOf(innerParams.getAfter().getTime());
            String before = innerParams.getBefore() == null ? null : String.valueOf(innerParams.getBefore().getTime());

            if (innerParams.getType() == FundingRecord.Type.WITHDRAWAL) {
                OkexResponse<List<OkexWithdrawalHistoryResponse>> okexResponse = getWithdrawalHistory(currency, null, null, null, null, null, after, before, null);

                if (!okexResponse.isSuccess())
                    throw new OkexException(okexResponse.getMsg(), Integer.parseInt(okexResponse.getCode()));

                return OkexAdapters.adaptOkexWithdrawalHistory(okexResponse.getData());
            } else if (innerParams.getType() == FundingRecord.Type.DEPOSIT) {
                OkexResponse<List<OkexDepositHistoryResponse>> okexResponse = getDepositHistory(currency, null, null, null, null, null, after, before, null);

                if (!okexResponse.isSuccess())
                    throw new OkexException(okexResponse.getMsg(), Integer.parseInt(okexResponse.getCode()));

                return OkexAdapters.adaptOkexDepositHistory(okexResponse.getData());
            } else if (innerParams.getType() == null) {

                OkexResponse<List<OkexWithdrawalHistoryResponse>> okexResponse = getWithdrawalHistory(currency, null, null, null, null, null, after, before, null);

                if (!okexResponse.isSuccess())
                    throw new OkexException(okexResponse.getMsg(), Integer.parseInt(okexResponse.getCode()));

                List<FundingRecord> withdrawalHistory = OkexAdapters.adaptOkexWithdrawalHistory(okexResponse.getData());

                OkexResponse<List<OkexDepositHistoryResponse>> okexResponse2 = getDepositHistory(currency, null, null, null, null, null, after, before, null);

                if (!okexResponse2.isSuccess())
                    throw new OkexException(okexResponse2.getMsg(), Integer.parseInt(okexResponse2.getCode()));

                List<FundingRecord> depositHistory = OkexAdapters.adaptOkexDepositHistory(okexResponse2.getData());

                withdrawalHistory.addAll(depositHistory);

                return withdrawalHistory;
            }
        }
        throw new IllegalStateException("Don't know how to get funding history: " + params);
    }
}
