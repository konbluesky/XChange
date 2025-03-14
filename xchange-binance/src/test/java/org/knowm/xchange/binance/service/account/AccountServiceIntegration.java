package org.knowm.xchange.binance.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.knowm.xchange.binance.dto.account.TransferType.MAIN_FUNDING;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.binance.BinanceExchangeIntegration;
import org.knowm.xchange.binance.dto.account.AssetDetail;
import org.knowm.xchange.binance.dto.account.BinanceBalance;
import org.knowm.xchange.binance.dto.account.BinanceCurrencyInfo;
import org.knowm.xchange.binance.dto.account.BinanceDeposit;
import org.knowm.xchange.binance.dto.account.TransferAllPurposeResponse;
import org.knowm.xchange.binance.dto.account.TransferHistory;
import org.knowm.xchange.binance.service.BinanceAccountService;
import org.knowm.xchange.binance.service.BinanceAccountServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.utils.StreamUtils;

@Slf4j
public class AccountServiceIntegration extends BinanceExchangeIntegration {

  static BinanceAccountService accountService;

  @BeforeClass
  public static void beforeClass() throws Exception {
    createExchange();
    accountService = (BinanceAccountService) exchange.getAccountService();
  }

  @Before
  public void before() {
    Assume.assumeNotNull(exchange.getExchangeSpecification().getApiKey());
  }

  @Test
  public void testAssetDetail() throws Exception {
    assumeProduction();
    Map<String, AssetDetail> assetDetails =
        ((BinanceAccountService) accountService).getAssetDetails();
    Assert.assertNotNull(assetDetails);
    Assert.assertFalse(assetDetails.isEmpty());
  }


  @Test
  public void testCurrencyInfos() throws Exception {
    assumeProduction();
    List<BinanceCurrencyInfo> currencyInfos = accountService.currencyInfos();
    assertThat(currencyInfos).isNotEmpty();
  }


  @Test
  public void testMetaData() {

    Map<Instrument, InstrumentMetaData> currencyPairs =
        exchange.getExchangeMetaData().getInstruments();
    Map<Currency, CurrencyMetaData> currencies = exchange.getExchangeMetaData().getCurrencies();
    Instrument currPair;
    Currency curr;

    currPair =
        currencyPairs.keySet().stream()
            .filter(cp -> "ETH/BTC".equals(cp.toString()))
            .collect(StreamUtils.singletonCollector());
    Assert.assertNotNull(currPair);

    curr =
        currencies.keySet().stream()
            .filter(Currency.BTC::equals)
            .collect(StreamUtils.singletonCollector());
    Assert.assertNotNull(curr);

    Assert.assertNotNull(curr);
  }

  @Test
  public void testBalances() throws Exception {

    Wallet wallet = accountService.getAccountInfo().getWallet();
    Assert.assertNotNull(wallet);

        Map<Currency, Balance> balances = wallet.getBalances();
        for (Entry<Currency, Balance> entry : balances.entrySet()) {
            Currency curr = entry.getKey();
            Balance bal = entry.getValue();
            if (0 < bal.getAvailable().doubleValue()) {
                log.info("{} Balance: {}", curr, bal.getAvailable().toPlainString());
                Assert.assertSame(curr, bal.getCurrency());
                Assert.assertSame(Currency.getInstance(curr.getCurrencyCode()), bal.getCurrency());
            }
        }
    }
    @Test
    public void testFundingBalance() throws Exception{
        List<BinanceBalance> binanceBalances = accountService.fundingAccount();
        for (BinanceBalance binanceBalance : binanceBalances) {
            log.info("Funding Account: {}",binanceBalance);
        }
    }



    @Test
    public void testWithdrawal() throws Exception {
        assumeProduction();
        accountService.withdrawFunds(
                Currency.BTC, BigDecimal.ONE, "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa");
    }

  @Test
  public void testWithdrawalHistory() throws Exception {
    assumeProduction();
    TradeHistoryParams params = accountService.createFundingHistoryParams();
    List<FundingRecord> fundingHistory = accountService.getFundingHistory(params);
    Assert.assertNotNull(fundingHistory);

    fundingHistory.forEach(
        record -> Assert.assertTrue(record.getAmount().compareTo(BigDecimal.ZERO) > 0));
  }

  @Test
  public void testDepositAddress() throws Exception {
    assumeProduction();
    String address = accountService.requestDepositAddress(Currency.BTC, (String) null);
    Assert.assertNotNull(address);
  }

  @Test
  public void testDepositHistory() throws Exception {
    assumeProduction();
    List<BinanceDeposit> depositHistory = accountService.depositHistory("BTC", null, null);
    Assert.assertNotNull(depositHistory);
  }

    @Test
    public void testTransferHistory() throws Exception {
        assumeProduction();
        List<TransferHistory> transferHistory =
                accountService.getTransferHistory("no@email.com", null, null, 1, 10);
        Assert.assertNotNull(transferHistory);
    }
    @Test
    public void testTransferAllPurpose() throws Exception{
        createExchange();
        BinanceAccountServiceRaw accountServiceRaw = (BinanceAccountServiceRaw) exchange.getAccountService();
        /**
         * 23:24:44.242 [default] [main] INFO  o.k.x.b.s.a.AccountServiceIntegration - HARD Balance: 0.00000003
         * 23:24:44.243 [default] [main] INFO  o.k.x.b.s.a.AccountServiceIntegration - BNB Balance: 0.10173887
         * 23:24:44.243 [default] [main] INFO  o.k.x.b.s.a.AccountServiceIntegration - LDBTTC Balance: 499217.0
         * 23:24:44.243 [default] [main] INFO  o.k.x.b.s.a.AccountServiceIntegration - USDT Balance: 559.57843636
         */
        Map<String,String> result = accountServiceRaw.transferAllPurpose(MAIN_FUNDING, "USDT", new BigDecimal(1),
            null, null);
        log.info("result: {} ",result);
        /**
         * 23:33:25.649 [default] [main] INFO  o.k.x.b.s.a.AccountServiceIntegration - USDT Balance: 558.57843636
         */
    }
    @Test
    public void testTransferAllPurposeHistory() throws Exception{
        createExchange();
        BinanceAccountServiceRaw accountServiceRaw = (BinanceAccountServiceRaw) exchange.getAccountService();
        TransferAllPurposeResponse transferAllPurposeResponse = accountServiceRaw.transferAllPurposeHistory(
            MAIN_FUNDING, null, null, 1, 10, null, null);

        log.info("history: {} ",transferAllPurposeResponse);
    }


}
