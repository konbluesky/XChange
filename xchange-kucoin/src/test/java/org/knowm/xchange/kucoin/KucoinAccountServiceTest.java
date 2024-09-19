package org.knowm.xchange.kucoin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.kucoin.dto.request.KucoinWithdrawFundsParams;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.utils.DateUtils;

/**
 * <p> @Date : 2024/9/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class KucoinAccountServiceTest extends KucoinBaseTest {

  @Test
  public void testAccount() throws IOException {
    AccountService accountService = exchange.getAccountService();
    AccountInfo accountInfo = accountService.getAccountInfo();
    log.info("trade :{}",accountInfo.getWallet("trade"));
    accountInfo.getWallets().forEach((k, v) -> {
      log.info("key:{}, value:{}", k, v.toString());
    });
  }

  @Test
  public void testGetWithdrawRecord() throws IOException{
    KucoinTradeHistoryParams fundingHistoryParams = (KucoinTradeHistoryParams) exchange.getAccountService()
        .createFundingHistoryParams();
    // 获取当前日期
    Date from = DateUtils.fromRfc3339DateString("2024-09-13 00:00:00");
    log.info("from:{}", from);
    log.info("to:{}",new Date());
    fundingHistoryParams.setStartTime(from);
    fundingHistoryParams.setEndTime(new Date());
    List<FundingRecord> fundingHistory = exchange.getAccountService().getFundingHistory(fundingHistoryParams);
    for (FundingRecord fundingRecord : fundingHistory) {
      log.info("{}",fundingRecord);
    }
  }

  @Test
  public void testWithdraw() throws IOException{
    KucoinWithdrawFundsParams withdrawFundsParams = new KucoinWithdrawFundsParams("0x50196380f3c0805ff5d1254ca7cafa9923ce85c9",
        Currency.USDT, BigDecimal.valueOf(5),KucoinNetwork.BEP20.getInnerChainId());
    String s = exchange.getAccountService().withdrawFunds(withdrawFundsParams);
    log.info("result:{}", s);
  }



}
