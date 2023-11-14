package org.knowm.xchange.hashkey;


import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyExchangeTest {

  protected Instrument AVAX_USD = new CurrencyPair("avax", "usd");
  protected Instrument ETH_HKD = new CurrencyPair("ETH", "HKD");

  public Exchange createRawExchange() throws IOException {
    Exchange exchange =
        ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(HashKeyExchange.class);
    ExchangeSpecification specification = exchange.getDefaultExchangeSpecification();
    specification.setHost("hashkey.com");
    specification.setSslUri("https://api-pro.hashkey.com");
    specification.setPort(443);
    specification.setApiKey("jTHlkpOXjm2xbpGs8sNYg89C34sRcQmAMX1FwlbxUFzJgkd8pg5ksxmi2QXkkMeD");
    specification.setSecretKey("3BeaidqsT6EJDEHfYorPPSag2dyJutTkHSgpahJvF2IR27PGUwR0OIG3Qww9Qm8Q");
    specification.setShouldLoadRemoteMetaData(false);
    exchange.applySpecification(specification);
    return exchange;
  }
}