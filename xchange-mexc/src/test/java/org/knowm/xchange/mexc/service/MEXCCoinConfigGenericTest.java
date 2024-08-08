package org.knowm.xchange.mexc.service;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfo;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfoSymbol;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;
import org.knowm.xchange.mexc.dto.market.MEXC24Ticker;

/**
 * <p> @Date : 2024/3/26 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCCoinConfigGenericTest extends BaseWiremockTest {

  @Test
  public void newMexcBSC2CoinCsv() throws IOException {
    String bscIdentity = MEXCNetwork.NETWORK_BSC2;
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<MEXCConfig> all = marketDataService.getAll();
    Map<Currency, MEXCConfig> collect = all.stream().filter(c -> c.getNetworkList().stream()
            .anyMatch(n -> n.getNetwork().equalsIgnoreCase(bscIdentity)))
        .collect(Collectors.toMap(c -> Currency.getInstance(c.getCoin()), c -> c));

    List<String> lines = Lists.newArrayList();
    String title = "coin,coin_full_name,network,deposit,withdraw,fee,contract";
    lines.add(title);
    collect.forEach((k, v) -> {
      MEXCNetwork network = v.getNetwork(bscIdentity);
      lines.add(
          String.format("%s,%s,%s,%s,%s,%s,%s", k, v.getName(), network.getNetwork(),
              network.isDepositEnable(), network.isWithdrawEnable(), network.getWithdrawFee(),
              network.getContract()));
    });
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("mexc-coin.csv"));
  }


  @Test
  public void newAllCoinCsv() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<MEXCConfig> all = marketDataService.getAll();

    List<MEXC24Ticker> tickers24 = marketDataService.getTickers24();
    //ticker24以coin为key
    Map<String, MEXC24Ticker> ticker24Map = tickers24.stream()
        .collect(Collectors.toMap(MEXC24Ticker::getSymbol, t -> t));

    List<String> lines = Lists.newArrayList();
    String title = "coin,coin_full_name,network,deposit,withdraw,fee,contract,volume24";
    lines.add(title);
    all.forEach(c -> {
      c.getNetworkList().forEach(n -> {
        lines.add(
            String.format("%s,%s,%s,%s,%s,%s,%s", c.getCoin(), c.getName(), n.getNetwork(),
                n.isDepositEnable(), n.isWithdrawEnable(), n.getWithdrawFee(), n.getContract()));
      });
    });
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("mexc-all-coin.csv"));

  }


  @Test
  public void ticker24Csv() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    List<MEXC24Ticker> tickers24 = marketDataService.getTickers24();
    /**
     * public class MEXC24Ticker {
     *   /**
     *    * 参数名	说明
     *    * symbol	交易对
     *    * priceChange	价格变化
     *    * priceChangePercent	价格变化比
     *    * prevClosePrice	前一收盘价
     *    * lastPrice	最新价
     *    * lastQty	最新量
     *    * bidPrice	买盘价格
     *    * bidQty	买盘数量
     *    * askPrice	卖盘价格
     *    * askQty	卖盘数量
     *    * openPrice	开始价
     *    * highPrice	最高价
     *    * lowPrice	最低价
     *    * volume	成交量
     *    * quoteVolume	成交额
     *    * openTime	开始时间
     *    * closeTime	结束时间
     *    * count
     *    */
    List<String> lines = Lists.newArrayList();
    String title = "symbol,priceChange,priceChangePercent,prevClosePrice,lastPrice,lastQty,bidPrice,bidQty,askPrice,askQty,openPrice,highPrice,lowPrice,volume,quoteVolume";

    lines.add(title);
    tickers24.forEach(t -> {
      //18 column
      String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
          t.getSymbol(), t.getPriceChange(), t.getPriceChangePercent(), t.getPrevClosePrice(),
          t.getLastPrice(), t.getLastQty(), t.getBidPrice(), t.getBidQty(), t.getAskPrice(),
          t.getAskQty(), t.getOpenPrice(), t.getHighPrice(), t.getLowPrice(), t.getVolume(),
          t.getQuoteVolume());
      lines.add(line);
    });
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("mexc-ticker24.csv"));
  }

  @Test
  public void allAllowCoinCsv() throws IOException {
    MEXCExchange exchange = (MEXCExchange) createRawExchange();
    MEXCMarketDataServiceRaw marketDataService = (MEXCMarketDataServiceRaw) exchange.getMarketDataService();
    Set<String> identities = Sets.newHashSet(MEXCNetwork.NETWORK_BSC1, MEXCNetwork.NETWORK_BSC2,
        MEXCNetwork.NETWORK_ARB);

    List<MEXCConfig> configs = marketDataService.getAll();
    MEXCExchangeInfo exchangeInfo = marketDataService.getExchangeInfo();
    Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
    Map<Instrument, InstrumentMetaData> instruments = new HashMap<>();
    Table<Currency, String, MEXCNetwork> currencyNetworks = HashBasedTable.create();

    List<String> lines = Lists.newArrayList();
    String title = "currency_pair,status,order_types,spot_trading_allowed,network";
    lines.add(title);

    for (MEXCConfig config : configs) {
      for (MEXCNetwork networkConfig : config.getNetworkList()) {
        if (identities.contains(networkConfig.getNetwork())) {
          currencyNetworks.put(Currency.getInstance(config.getCoin()), networkConfig.getNetwork(),
              networkConfig);
        }
      }
    }

    for (MEXCExchangeInfoSymbol symbol : exchangeInfo.getSymbols()) {

      Instrument pair = MEXCAdapters.extractOneCurrencyPairs(symbol.getSymbol());
      Currency base = pair.getBase();
      Set<String> networks = currencyNetworks.row(base).keySet();
      lines.add(
          String.format("%s,%s,%s,%s,%s", pair, symbol.getStatus(),
              Joiner.on("-").join(symbol.getOrderTypes()),
              symbol.isSpotTradingAllowed(), Joiner.on("-").join(networks)));

//      if (!"ENABLED".equals(symbol.getStatus())
//          || !symbol.getOrderTypes().containsAll(Arrays.asList("MARKET", "LIMIT_MAKER", "LIMIT"))
//          || !symbol.isSpotTradingAllowed()
//          || currencyNetworks.row(base).isEmpty()
//          || Sets.intersection(identities, currencyNetworks.row(base).keySet()).isEmpty()
//      ) {
//        continue;
//      }

//      instruments.put(pair, new InstrumentMetaData.Builder()
//          .minimumAmount(new BigDecimal(symbol.getBaseSizePrecision()))
//          .marketOrderEnabled(true)
//          .build());
//      // 包装类,使用默认网络(BSC),将可提现,可充值等信息存入实体
//      MEXCCurrencyMetaData currencyMetaData = new MEXCCurrencyMetaData(
//          symbol.getBaseAssetPrecision(),
//          null,
//          null,
//          null
//      );
//      currencyMetaData.setNetworks(currencyNetworks.row(base).values());
//      currencies.put(pair.getBase(), currencyMetaData);
    }
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("mexc-allow-coin.csv"));
  }

}
