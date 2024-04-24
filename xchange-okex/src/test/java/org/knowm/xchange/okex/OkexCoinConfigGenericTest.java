package org.knowm.xchange.okex;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.okex.dto.marketdata.OkexCurrency;
import org.knowm.xchange.okex.dto.marketdata.OkexExchangeMetaData;
import org.knowm.xchange.okex.service.OkexMarketDataServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> @Date : 2024/4/16 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class OkexCoinConfigGenericTest {
  Instrument instrument = new FuturesContract("BTC/USDT/SWAP");
  Exchange exchange;

  @Before
  public void setUp(){
    Properties properties = new Properties();

    try {
      properties.load(this.getClass().getResourceAsStream("/secret.keys"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ExchangeSpecification spec = new OkexExchange().getDefaultExchangeSpecification();

    spec.setApiKey(properties.getProperty("apikey"));
    spec.setSecretKey(properties.getProperty("secret"));
    spec.setExchangeSpecificParametersItem(OkexExchange.PARAM_PASSPHRASE, properties.getProperty("passphrase"));
//        spec.setExchangeSpecificParametersItem(OkexExchange.PARAM_SIMULATED, "1");

    exchange = ExchangeFactory.INSTANCE.createExchange(spec);
  }
  @Test
  public void newGateioBSC2CoinCsv() throws IOException {
    OkexExchangeMetaData exchangeMetaData = (OkexExchangeMetaData) exchange.getExchangeMetaData();
    OkexMarketDataServiceRaw marketDataServiceRaw = (OkexMarketDataServiceRaw) exchange.getMarketDataService();


    List<String> lines = Lists.newArrayList();
    String title = "coin,coin_full_name,network,deposit,withdraw,fee,feeRate,contract";
    lines.add(title);

    List<OkexCurrency> datas = marketDataServiceRaw.getOkexCurrencies().getData();
    for (OkexCurrency data : datas) {
        lines.add(
            String.format("%s,%s,%s,%s,%s,%s,%s", data.getCurrency(),data.getName(), data.getChain(),
                data.isCanDep(),data.isCanWd(),data.getMinFee(),data.getLogoLink()
                ));
    }

//    Multimap<Currency, OkexCurrency> okexCurrencyMap = exchangeMetaData.getOkexCurrencys();
//    for (Currency currencyKey : okexCurrencyMap.keySet()) {
//      List<OkexCurrency> okexCurrencys = Lists.newArrayList(okexCurrencyMap.get(currencyKey));
//      for (OkexCurrency okexCurrency : okexCurrencys) {
//        log.info("{}", okexCurrency);
//      }
//    }

//    Map<String, GateioCoin> coins = marketDataService.getGateioCoinInfo();
//    Map<String, GateioWithdrawStatus> gateioWithDrawFees = marketDataService.getGateioWithDrawFees(
//        null);
//    for (Entry<String, GateioCoin> stringGateioCoinEntry : coins.entrySet()) {
//      String k=stringGateioCoinEntry.getKey();
//      GateioCoin v=stringGateioCoinEntry.getValue();
//      GateioWithdrawStatus coinChainConfig = gateioWithDrawFees.get(v.getCurrency());
//      if(coinChainConfig==null){
//        log.error("coinChainConfig is null for {}", v.getCurrency());
//        continue;
//      }
//      List<GateioCoinNetwork> coinNetworkBy = null;
//      try {
//        coinNetworkBy = marketDataService.getCoinNetworkBy(
//            coinChainConfig.getCurrency());
//        TimeUnit.MILLISECONDS.sleep(200);
//      } catch (IOException | InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//      if(coinNetworkBy.isEmpty()){
//        log.error("coinNetworkBy is empty for {}", v.getCurrency());
//        continue;
//      }
//      for (GateioCoinNetwork gateioCoinNetwork : coinNetworkBy) {

//      }
//
//    }
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("okex-coins.csv"));
  }
}
