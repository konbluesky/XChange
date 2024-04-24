package org.knowm.xchange.gateio.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.gateio.GateioExchangeWiremock;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawStatus;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoin;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoinNetwork;

/**
 * <p> @Date : 2024/3/26 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioCoinConfigGenericTest extends GateioExchangeWiremock {

  @Test
  public void newGateioBSC2CoinCsv() throws IOException {
    GateioMarketDataServiceRaw marketDataService = (GateioMarketDataServiceRaw) exchange.getMarketDataService();
    Map<String, GateioCoin> coins = marketDataService.getGateioCoinInfo();
    Map<String, GateioWithdrawStatus> gateioWithDrawFees = marketDataService.getGateioWithDrawFees(
        null);
    List<String> lines = Lists.newArrayList();
    String title = "coin,coin_full_name,network,deposit,withdraw,fee,feeRate,contract";
    lines.add(title);
    for (Entry<String, GateioCoin> stringGateioCoinEntry : coins.entrySet()) {
      String k=stringGateioCoinEntry.getKey();
      GateioCoin v=stringGateioCoinEntry.getValue();
      GateioWithdrawStatus coinChainConfig = gateioWithDrawFees.get(v.getCurrency());
      if(coinChainConfig==null){
        log.error("coinChainConfig is null for {}", v.getCurrency());
        continue;
      }
      List<GateioCoinNetwork> coinNetworkBy = null;
      try {
        coinNetworkBy = marketDataService.getCoinNetworkBy(
            coinChainConfig.getCurrency());
        TimeUnit.MILLISECONDS.sleep(200);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
      if(coinNetworkBy.isEmpty()){
        log.error("coinNetworkBy is empty for {}", v.getCurrency());
        continue;
      }
      for (GateioCoinNetwork gateioCoinNetwork : coinNetworkBy) {
        lines.add(
            String.format("%s,%s,%s,%s,%s,%s,%s,%s", k, v.getCurrency(), gateioCoinNetwork.getChain(),
                v.isDepositDisabled(), v.isWithdrawDisabled(), coinChainConfig.getWithdrawFix(),v.getFixedRate(),gateioCoinNetwork.getContractAddress()
                ));
      }

    }
    String join = Joiner.on("\n").join(lines);
    Files.write(join.getBytes(), new File("gateio-coin.csv"));
  }


}
