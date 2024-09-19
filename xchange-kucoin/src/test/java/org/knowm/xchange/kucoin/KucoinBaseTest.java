package org.knowm.xchange.kucoin;

import java.io.IOException;
import org.junit.BeforeClass;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.AuthUtils;

public class KucoinBaseTest {

  protected static Exchange exchange;

  @BeforeClass
  public static void createRawExchange() throws IOException {
    exchange =
        ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(KucoinExchange.class);
    ExchangeSpecification specification = exchange.getDefaultExchangeSpecification();
    specification.setHost("www.kucoin.com");
    specification.setSslUri("https://api.kucoin.com");
//    specification.setShouldLoadRemoteMetaData(false);
    specification.setShouldLoadRemoteMetaData(true);
//  load secret.keys from resources/
    AuthUtils.setApiAndSecretKey(specification);
    specification.setExchangeSpecificParametersItem("passphrase",
        AuthUtils.getSecretProperties(null).get("passphrase"));
    exchange.applySpecification(specification);
  }
}
