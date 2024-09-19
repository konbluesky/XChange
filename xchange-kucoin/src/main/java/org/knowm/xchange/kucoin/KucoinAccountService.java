package org.knowm.xchange.kucoin;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.FundingRecord.Type;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.account.Wallet.WalletFeature;
import org.knowm.xchange.kucoin.dto.request.ApplyWithdrawApiRequest;
import org.knowm.xchange.kucoin.dto.request.ApplyWithdrawApiRequest.ApplyWithdrawApiRequestBuilder;
import org.knowm.xchange.kucoin.dto.request.KucoinWithdrawFundsParams;
import org.knowm.xchange.kucoin.dto.response.AccountBalancesResponse;
import org.knowm.xchange.kucoin.dto.response.ApplyWithdrawResponse;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.HistoryParamsFundingType;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrency;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

public class KucoinAccountService extends KucoinAccountServiceRaw implements AccountService {

  KucoinAccountService(KucoinExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (params instanceof KucoinWithdrawFundsParams) {
      KucoinWithdrawFundsParams kucoinParam = (KucoinWithdrawFundsParams) params;
      ApplyWithdrawApiRequestBuilder builder = ApplyWithdrawApiRequest.builder();
      builder.currency(kucoinParam.getCurrency().getCurrencyCode());
      builder.address(kucoinParam.getAddress());
      builder.amount(kucoinParam.getAmount());
      builder.chain(kucoinParam.getChain());
      ApplyWithdrawApiRequest payload = builder.build();
      ApplyWithdrawResponse applyWithdrawResponse = super.applyWithdraw(payload);
      return applyWithdrawResponse.getWithdrawalId();
    }
    if (params instanceof DefaultWithdrawFundsParams) {
      DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;
      return withdrawFunds(
          defaultParams.getCurrency(), defaultParams.getAmount(), defaultParams.getAddress());
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }



  @Override
  public AccountInfo getAccountInfo() throws IOException {
    List<AccountBalancesResponse> accounts = getKucoinAccounts();
    return new AccountInfo(
        accounts.stream()
            .map(AccountBalancesResponse::getType)
            .distinct()
            .map(
                type ->
                    Wallet.Builder.from(
                            accounts.stream()
                                .filter(a -> a.getType().equals(type))
                                .map(KucoinAdapters::adaptBalance)
                                .collect(toList()))
                        .id(type)
                        .features(type.equalsIgnoreCase("main") ? Collections.singleton(
                            WalletFeature.FUNDING) : Collections.singleton(WalletFeature.TRADING))
//                        賬戶類型，資金（main）賬戶、现货交易(trade)賬戶、现货高频交易(trade_hf)賬戶、槓桿（margin）賬戶
//                        .features(Collections.singleton(WalletFeature.TRADING))
                        .build())
            .collect(toList()));
  }

  public TradeHistoryParams createFundingHistoryParams() {
    return new KucoinTradeHistoryParams();
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    String currency = null;
    if (params instanceof TradeHistoryParamCurrency) {
      Currency c = ((TradeHistoryParamCurrency) params).getCurrency();
      currency = c == null ? null : c.getCurrencyCode();
    }
    boolean withdrawals = true, deposits = true;
    if (params instanceof HistoryParamsFundingType) {
      HistoryParamsFundingType p = (HistoryParamsFundingType) params;
      withdrawals = p.getType() == null || p.getType() == Type.WITHDRAWAL;
      deposits = p.getType() == null || p.getType() == Type.DEPOSIT;
    }

    Long startAt = null, endAt = null;
    if (params instanceof TradeHistoryParamsTimeSpan) {
      TradeHistoryParamsTimeSpan p = (TradeHistoryParamsTimeSpan) params;
      startAt = p.getStartTime() == null ? null : p.getStartTime().getTime();
      endAt = p.getEndTime() == null ? null : p.getEndTime().getTime();
    }
    List<FundingRecord> result = new ArrayList<>();
    if (withdrawals) {
      result.addAll(
          getWithdrawalsList(currency, null, startAt, endAt, null, null).getItems().stream()
              .map(KucoinAdapters::adaptFundingRecord)
              .collect(Collectors.toList()));
    }
    if (deposits) {
      result.addAll(
          getDepositList(currency, null, startAt, endAt, null, null).getItems().stream()
              .map(KucoinAdapters::adaptFundingRecord)
              .collect(Collectors.toList()));
    }
    return result;
  }


}
