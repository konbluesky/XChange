package org.knowm.xchange.xt;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.xt.dto.XTNetwork;
import org.knowm.xchange.xt.dto.account.DepositHistoryResponse;
import org.knowm.xchange.xt.dto.account.XTFundingHistoryParams;
import org.knowm.xchange.xt.dto.account.XTWithdrawFundsParams;
import org.knowm.xchange.xt.service.XTAccountServiceRaw;

/**
 * <p> @Date : 2024/6/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTAccountServiceTest extends XTExchangeBase {

  @Test
  public void testBalance() throws IOException {
    AccountInfo accountInfo = exchange.getAccountService().getAccountInfo();
    log.info("{}", accountInfo);
  }

  @Test
  public void testWithdraw() throws IOException {
    String address = "0xF43f75Cd0694d477797D2772B50423B80189f455";
    XTWithdrawFundsParams xtWithdrawFundsParams = new XTWithdrawFundsParams(address, Currency.USDT,
        new BigDecimal("10"),
        XTNetwork.BNB_SMART_CHAIN);
    String s = exchange.getAccountService().withdrawFunds(xtWithdrawFundsParams);
    log.info("withdraw result : {} ", s);
  }

  @Test
  public void testFundingHistory() throws IOException {
    LocalDate today = LocalDate.now();
    // 获取前两天的日期
    LocalDate twoDaysAgo = today.minusDays(2);
    // 获取当天的 00:00:00 时间
    LocalDateTime twoDaysAgoStartOfDay = twoDaysAgo.atStartOfDay();
    // 将 LocalDateTime 转换为 ZonedDateTime
    ZonedDateTime zonedDateTime = twoDaysAgoStartOfDay.atZone(ZoneId.systemDefault());
    // 获取时间戳（毫秒）
    long timestamp = zonedDateTime.toInstant().toEpochMilli();

    // 格式化日期时间
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = twoDaysAgoStartOfDay.format(formatter);

//    XTWithdrawHistoryParams xtWithdrawHistoryParams = new XTWithdrawHistoryParams(
//        Currency.USDT.getCurrencyCode(), XTNetwork.BNB_SMART_CHAIN, null, null, null, 100,
//        timestamp, null
//    );
    XTFundingHistoryParams param = XTFundingHistoryParams.builder()
        .startTime(timestamp)
        .chain(XTNetwork.BNB_SMART_CHAIN)
        .currency(Currency.USDT.getCurrencyCode())
        .build();

    List<FundingRecord> fundingHistory = exchange.getAccountService().getFundingHistory(param);
    for (FundingRecord fundingRecord : fundingHistory) {
      log.info("funding : {}", fundingRecord);
    }
  }

  @Test
  public void testDepositHistory() throws IOException{
    XTAccountServiceRaw accountService = (XTAccountServiceRaw)exchange.getAccountService();
    LocalDate today = LocalDate.now();
    // 获取前两天的日期
    LocalDate twoDaysAgo = today.minusDays(0);
    // 获取当天的 00:00:00 时间
    LocalDateTime twoDaysAgoStartOfDay = twoDaysAgo.atStartOfDay();
    // 将 LocalDateTime 转换为 ZonedDateTime
    ZonedDateTime zonedDateTime = twoDaysAgoStartOfDay.atZone(ZoneId.systemDefault());
    // 获取时间戳（毫秒）
    long timestamp = zonedDateTime.toInstant().toEpochMilli();

    // 格式化日期时间
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = twoDaysAgoStartOfDay.format(formatter);
    List<DepositHistoryResponse> depositHistory = accountService.getDepositHistory(
//        Currency.SOL.getCurrencyCode(), XTNetwork.SOL_SOL,null, null, null, 10, null,
        Currency.USDT.getCurrencyCode(), XTNetwork.BNB_SMART_CHAIN,null, null, null, 10, timestamp,
        null);
    log.info("depositHistory record :{} ", depositHistory.size());
    for (DepositHistoryResponse depositHistoryResponse : depositHistory) {
      log.info("info : {}",depositHistoryResponse);
    }
  }



}
