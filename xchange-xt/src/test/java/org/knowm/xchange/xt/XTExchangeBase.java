package org.knowm.xchange.xt;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.AuthUtils;

/**
 * <p> @Date : 2024/3/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTExchangeBase {

  protected static XTExchange exchange;

  @BeforeClass
  public static void initExchangeMockLocal() {
    ExchangeSpecification spec = new XTExchange().getDefaultExchangeSpecification();
    AuthUtils.setApiAndSecretKey(spec);
    spec.setShouldLoadRemoteMetaData(false);
    exchange = (XTExchange) ExchangeFactory.INSTANCE.createExchange(spec);
  }
}