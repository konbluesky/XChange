package org.knowm.xchange.xt;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;

/**
 * <p> @Date : 2024/3/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTExchangeBase {

  protected static XTExchange exchange;
  protected static String API_KEY = "c9d68fd8-6eda-4e00-863c-0c4bb79454b1";
  protected static String SECRET_KEY = "7c0f352ee245d5c004ae0de83f61ebc2a34e0305";

  @BeforeClass
  public static void initExchangeMockLocal() {
    ExchangeSpecification spec = new XTExchange().getDefaultExchangeSpecification();
    spec.setApiKey(API_KEY);
    spec.setSecretKey(SECRET_KEY);
    spec.setShouldLoadRemoteMetaData(false);
    exchange = (XTExchange) ExchangeFactory.INSTANCE.createExchange(spec);
  }
}