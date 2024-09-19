package org.knowm.xchange.kucoin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.google.common.io.Files;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.kucoin.dto.response.CurrencyResponseV2;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * <p> @Date : 2024/9/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class KucoinCoinCsvTest extends KucoinBaseTest {

  MarketDataService marketDataService = exchange.getMarketDataService();

  @Test
  public void writeCurrencyToCsv() throws IOException {
    KucoinMarketDataServiceRaw service = (KucoinMarketDataServiceRaw) exchange.getMarketDataService();
    List<CurrencyResponseV2> responseList = service.getKucoinCurrenciesV3();
    try (FileWriter csvWriter = new FileWriter("kucoin-all-coin.csv")) {

      // 写入自定义的 CSV 表头
      csvWriter.append("coin,coin_full_name,network,deposit,withdraw,fee,contract\n");

      // 遍历每一个 CurrencyResponseV2 对象
      for (CurrencyResponseV2 response : responseList) {
        // 遍历 chains 展开每一条数据
        if (response.getChains() == null) {
          continue;
        }

        for (CurrencyResponseV2.Chain chain : response.getChains()) {
          List<String> row = Lists.newArrayList(
              response.getCurrency(),                               // coin
              response.getFullName(),                              // coin_full_name
              chain.getChainName(),                                // network
              chain.getIsDepositEnabled() != null ? chain.getIsDepositEnabled().toString() : "",
              // deposit
              chain.getIsWithdrawEnabled() != null ? chain.getIsWithdrawEnabled().toString() : "",
              // withdraw
              chain.getWithdrawalMinFee() != null ? chain.getWithdrawalMinFee().toString() : "",
              // fee
              chain.getContractAddress()                           // contract
          );

          // 生成 CSV 的行
          csvWriter.append(String.join(",", row)).append("\n");
        }
      }

      // 刷新写入
      csvWriter.flush();
    }
  }

  @Test
  public void testCreateKucoinNetworkFile() throws IOException {
    KucoinMarketDataServiceRaw service = (KucoinMarketDataServiceRaw) exchange.getMarketDataService();
    List<CurrencyResponseV2> responseList = service.getKucoinCurrenciesV3();
    Map<String, String> unimap = Maps.newHashMap();

    for (CurrencyResponseV2 response : responseList) {
      if (response.getChains() == null) {
        continue;
      }
      for (CurrencyResponseV2.Chain chain : response.getChains()) {
        unimap.put(chain.getChainId(), chain.getChainName());
      }
    }

    StringBuilder enumBuilder = new StringBuilder();
    enumBuilder.append("package org.knowm.xchange.kucoin;\n");
    enumBuilder.append("public enum KucoinNetwork {\n");

    unimap.forEach((chainId, chainName) -> {
      enumBuilder.append("    ")
          .append(chainName.replaceAll("[ |\\(|\\)|\\-]","_"))
          .append("(\"").append(chainName.replaceAll(" ","_")).append("\", \"").append(chainId).append("\"),\n");

    });
    // Remove the last comma and add the enum body
    enumBuilder.setLength(enumBuilder.length() - 2); // Remove the last comma
    enumBuilder.append(";\n\n");

    // Enum fields and constructor
    enumBuilder.append("    private final String chainName;\n")
        .append("    private final String innerChainId;\n\n")
        .append("    KucoinNetwork(String chainName, String innerChainId) {\n")
        .append("        this.chainName = chainName;\n")
        .append("        this.innerChainId = innerChainId;\n")
        .append("    }\n\n");

    // Getter methods
    enumBuilder.append("    public String getChainName() {\n")
        .append("        return chainName;\n")
        .append("    }\n\n");

    enumBuilder.append("    public String getInnerChainId() {\n")
        .append("        return innerChainId;\n")
        .append("    }\n");

    // Close enum class
    enumBuilder.append("}\n");
    Files.write(enumBuilder.toString().getBytes(), new File("src/main/java/org/knowm/xchange/kucoin/KucoinNetwork.java"));
    // 输出生成的枚举类
    System.out.println(enumBuilder.toString());
  }


  @Test
  public void testCsv() throws IOException {
    KucoinMarketDataServiceRaw service = (KucoinMarketDataServiceRaw) exchange.getMarketDataService();
    List<CurrencyResponseV2> kucoinCurrenciesV3 = service.getKucoinCurrenciesV3();
    for (CurrencyResponseV2 currencyResponseV2 : kucoinCurrenciesV3) {
      log.info("response: {}", currencyResponseV2);
    }


  }

  @Test
  public void testInstruments() throws IOException {
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    Map<Instrument, InstrumentMetaData> instruments = exchangeMetaData.getInstruments();
    for (Entry<Instrument, InstrumentMetaData> entry : instruments.entrySet()) {
      log.info("instrument :{}  metaData:{}", entry.getKey(), entry.getValue());
    }
  }


  @Test
  public void testCurrencies() throws IOException {
    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    Map<Currency, CurrencyMetaData> currencies = exchangeMetaData.getCurrencies();
    currencies.forEach((k, v) -> {
      log.info("currency :{}  metaData:{}", k, v);
    });
  }
}
