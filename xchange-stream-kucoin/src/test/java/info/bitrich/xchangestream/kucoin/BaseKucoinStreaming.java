package info.bitrich.xchangestream.kucoin;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.AuthUtils;

/**
 * <p> @Date : 2024/9/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class BaseKucoinStreaming {

  protected static KucoinStreamingExchange streamingExchange;

  @BeforeClass
  public static void initExchange() {
    ExchangeSpecification spec =
        StreamingExchangeFactory.INSTANCE
            .createExchangeWithoutSpecification(KucoinStreamingExchange.class)
            .getDefaultExchangeSpecification();
    AuthUtils.setApiAndSecretKey(spec);
    spec.setExchangeSpecificParametersItem("passphrase",
        AuthUtils.getSecretProperties(null).get("passphrase"));
    spec.setShouldLoadRemoteMetaData(false);
    streamingExchange = (KucoinStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(
        spec);
  }

}
