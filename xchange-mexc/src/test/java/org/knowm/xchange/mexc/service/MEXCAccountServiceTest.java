package org.knowm.xchange.mexc.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.account.Wallet.WalletFeature;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.MEXCResilience;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;
import org.knowm.xchange.mexc.dto.account.MEXCWithdrawFundsParams;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;

@Slf4j
public class MEXCAccountServiceTest extends BaseWiremockTest {

  @Test
  public void testGetWalletBalances() throws IOException {
    MEXCExchange mexcExchange = (MEXCExchange) createExchange();
    MEXCAccountService mexcAccountService = new MEXCAccountService(mexcExchange, MEXCResilience.createRegistries());

    String walletBalanceDetails = "{\n" +
        "    \"code\": 200,\n" +
        "    \"data\": {\n" +
        "        \"BTC\": {\n" +
        "            \"frozen\": \"0\",\n" +
        "            \"available\": \"140\"\n" +
        "        },\n" +
        "        \"ETH\": {\n" +
        "            \"frozen\": \"8471.296525048\",\n" +
        "            \"available\": \"483280.9653659222035\"\n" +
        "        },\n" +
        "        \"USDT\": {\n" +
        "            \"frozen\": \"0\",\n" +
        "            \"available\": \"27.3629\"\n" +
        "        },\n" +
        "        \"MX\": {\n" +
        "            \"frozen\": \"30.9863\",\n" +
        "            \"available\": \"450.0137\"\n" +
        "        }\n" +
        "    }\n" +
        "}";

    stubFor(
        get(urlPathEqualTo("/open/api/v2/account/info"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(walletBalanceDetails)
            )
    );

    AccountInfo accountInfo = mexcAccountService.getAccountInfo();
    assertThat(accountInfo.getWallet().getBalance(new Currency("BTC")).getFrozen()).isEqualTo(
        new BigDecimal("0"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("BTC")).getAvailable()).isEqualTo(
        new BigDecimal("140"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("BTC")).getTotal()).isEqualTo(
        new BigDecimal("140"));

    assertThat(accountInfo.getWallet().getBalance(new Currency("ETH")).getFrozen()).isEqualTo(
        new BigDecimal("8471.2965250480000"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("ETH")).getAvailable()).isEqualTo(
        new BigDecimal("483280.9653659222035"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("ETH")).getTotal()).isEqualTo(
        new BigDecimal("491752.2618909702035"));

    assertThat(accountInfo.getWallet().getBalance(new Currency("USDT")).getFrozen()).isEqualTo(
        new BigDecimal("0.0000"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("USDT")).getAvailable()).isEqualTo(
        new BigDecimal("27.3629"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("USDT")).getTotal()).isEqualTo(
        new BigDecimal("27.3629"));

    assertThat(accountInfo.getWallet().getBalance(new Currency("MX")).getFrozen()).isEqualTo(
        new BigDecimal("30.9863"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("MX")).getAvailable()).isEqualTo(
        new BigDecimal("450.0137"));
    assertThat(accountInfo.getWallet().getBalance(new Currency("MX")).getTotal()).isEqualTo(
        new BigDecimal("481.0000"));

  }


  @Test
  public void testGetV3balance() throws IOException {
    Exchange exchange = createRawExchange();
    AccountService accountService = exchange.getAccountService();
    log.info("Account : {}", accountService.getAccountInfo());
    Wallet wallet = accountService.getAccountInfo().getWallet(WalletFeature.TRADING);
    log.info("Account wallet: {}", wallet);

    wallet.getBalances().forEach((k, v) -> {
      log.info("{} , {} ,{} ", v.getCurrency(), v.getAvailable(), v.getFrozen());
    });

  }

  @Test
  public void testFundingHistory() throws IOException {
    Exchange exchange = createRawExchange();
    AccountService accountService = exchange.getAccountService();
    TradeHistoryParams fundingHistoryParams = accountService.createFundingHistoryParams();

    List<FundingRecord> fundingHistory = accountService.getFundingHistory(fundingHistoryParams);

    log.info("size:{}", fundingHistory.size());

    for (FundingRecord fundingRecord : fundingHistory) {
      log.info("fundting record :  {} ", fundingRecord);
    }
  }

  @Test
  public void testWithdraw() throws IOException, InterruptedException {
    Exchange exchange = createRawExchange();
    MEXCAccountService accountService = (MEXCAccountService)exchange.getAccountService();
//    MEXCWithdrawFundsParams mexcWithdrawFundsParams=MEXCWithdrawFundsParams
    String s = accountService.withdrawFunds(
        new MEXCWithdrawFundsParams("0x7D57C886F058413783ab19DE1165ec736BD88e3a", Currency.USDT,
            new BigDecimal("10"), MEXCNetwork.NETWORK_BSC2));
    log.info("with result:{}",s);

    TimeUnit.SECONDS.sleep(3);

    String s1 = accountService.cancelWithdraw(s);
    log.info("cancel with id : {}" ,s1);


  }


}
