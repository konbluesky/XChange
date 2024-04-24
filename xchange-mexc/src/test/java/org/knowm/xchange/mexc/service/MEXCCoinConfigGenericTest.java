package org.knowm.xchange.mexc.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
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
    Map<String, MEXC24Ticker> ticker24Map = tickers24.stream().collect(Collectors.toMap(MEXC24Ticker::getSymbol, t -> t));

    List<String> lines = Lists.newArrayList();
    String title = "coin,coin_full_name,network,deposit,withdraw,fee,contract";
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

}
