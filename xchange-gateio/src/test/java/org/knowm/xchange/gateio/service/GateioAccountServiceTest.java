package org.knowm.xchange.gateio.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.account.Wallet.WalletFeature;
import org.knowm.xchange.gateio.GateioExchangeWiremock;
import org.knowm.xchange.gateio.dto.account.GateioCancelWithdrawalResponse;
import org.knowm.xchange.gateio.dto.account.GateioSpotBalanceResponse;
import org.knowm.xchange.gateio.dto.account.GateioUnifiedAccount;
import org.knowm.xchange.gateio.dto.marketdata.GateioCurrencyMetaData;
import org.knowm.xchange.gateio.params.GateioDefaultWithdrawFundsParams;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultTradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.utils.DateUtils;

/**
 * <p> @Date : 2024/2/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioAccountServiceTest extends GateioExchangeWiremock {

  GateioAccountServiceRaw rawAccountService = (GateioAccountServiceRaw) exchange.getAccountService();
  AccountService accountService = exchange.getAccountService();


  @Test
  public void getGateioUnifiedAccount() throws IOException {
    GateioUnifiedAccount gateioUnifiedAccount = rawAccountService.getGateioUnifiedAccount();
    System.out.println(gateioUnifiedAccount);
  }

  @Test
  public void getGateioSpotAccount() throws IOException {
    List<GateioSpotBalanceResponse> gateioSpotAccount = rawAccountService.getGateioSpotAccount();
    System.out.println("----");
    System.out.println(gateioSpotAccount);
  }

  @Test
  public void getAccountInfo() throws IOException {
    AccountInfo accountInfo = accountService.getAccountInfo();
    Map<String, Wallet> wallets = accountInfo.getWallets();
    wallets.forEach((k, v) -> {
      log.info("currency: {}, balance: {}", k, v);
    });
  }

  @Test
  public void withdrawFundsTest() throws IOException, InterruptedException {
    AccountInfo accountInfo = accountService.getAccountInfo();
    GateioCurrencyMetaData currencyMetaData = (GateioCurrencyMetaData) exchange.getExchangeMetaData()
        .getCurrencies().get("USDT");
    Wallet wallet = accountInfo.getWallet(WalletFeature.TRADING);
    for (Balance balance : wallet.balances()) {
      log.info("balance:{}", balance);
    }

    GateioDefaultWithdrawFundsParams params = new GateioDefaultWithdrawFundsParams("",
        Currency.USDT, new BigDecimal("5"), "BSC", null);

    String s = accountService.withdrawFunds(params);
    log.info("withdrawFunds:{}", s);

    TimeUnit.SECONDS.sleep(5);
    //w53012855
    GateioCancelWithdrawalResponse gateioCancelWithdrawalResponse = rawAccountService.cancelWithdraw(
        s);
    /**
     * 即使及时取消 ，状态也是 CANCELPEND
     */
    log.info("cancel jsonNode:{}", gateioCancelWithdrawalResponse);
    log.info("statusType:{}", gateioCancelWithdrawalResponse.getStatusType());
  }

  @Test
  public void cancelWithdraw() throws IOException {
    GateioCancelWithdrawalResponse gateioCancelWithdrawalResponse = rawAccountService.cancelWithdraw(
        "w53015179");
    log.info("cancel jsonNode:{}", gateioCancelWithdrawalResponse);
    log.info("statusType:{}", gateioCancelWithdrawalResponse.getStatusType());
  }

  @Test
  public void testQueryDepAndWithRecord() throws IOException {
    // 获取当前日期
    Date from = DateUtils.fromRfc3339DateString("2024-02-20 00:00:00");
    log.info("from:{}", from);
    log.info("to:{}",new Date());
    // 获取前一天的日期
    TradeHistoryParamsTimeSpan params = new DefaultTradeHistoryParamsTimeSpan(from,new Date());
    List<FundingRecord> fundingHistory = accountService.getFundingHistory(params);
    log.info("fundingHistory:{}", fundingHistory);
  }


}
