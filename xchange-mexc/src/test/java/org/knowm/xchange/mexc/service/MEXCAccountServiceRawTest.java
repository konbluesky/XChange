package org.knowm.xchange.mexc.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.io.IOException;
import org.junit.Test;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.MEXCResilience;

public class MEXCAccountServiceRawTest extends BaseWiremockTest {

  @Test
  public void testGetWalletBalances() throws IOException {
    MEXCExchange mexcExchange = (MEXCExchange) createExchange();
    MEXCTradeService mexcAccountService = new MEXCTradeService(mexcExchange,
        MEXCResilience.createRegistries());

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

//        MEXCResult<Map<String, MEXCBalance>> walletBalances = mexcAccountServiceRaw.getWalletBalances();
//
//        Map<String, MEXCBalance> walletBalancesResult = walletBalances.getData();
//        assertThat(walletBalancesResult.get("BTC").getAvailable()).isEqualTo("140");
//        assertThat(walletBalancesResult.get("BTC").getFrozen()).isEqualTo("0");
//
//        assertThat(walletBalancesResult.get("ETH").getAvailable()).isEqualTo("483280.9653659222035");
//        assertThat(walletBalancesResult.get("ETH").getFrozen()).isEqualTo("8471.296525048");
//
//        assertThat(walletBalancesResult.get("USDT").getAvailable()).isEqualTo("27.3629");
//        assertThat(walletBalancesResult.get("USDT").getFrozen()).isEqualTo("0");
//
//
//        assertThat(walletBalancesResult.get("MX").getAvailable()).isEqualTo("450.0137");
//        assertThat(walletBalancesResult.get("MX").getFrozen()).isEqualTo("30.9863");

  }

}
