package org.knowm.xchange.gateio;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.recording.RecordSpecBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.AuthUtils;

/** Sets up the wiremock for exchange */
public abstract class GateioExchangeWiremock {

  protected static GateioExchange exchange;

  private static final boolean IS_RECORDING = false;

  @ClassRule
  public static WireMockClassRule wireMockRule = new WireMockClassRule(options().dynamicPort());

//  @BeforeClass
  public static void initExchangeMockLocal() {
    ExchangeSpecification exSpec = new ExchangeSpecification(GateioExchange.class);
    exSpec.setSslUri("http://localhost:" + wireMockRule.port());

    if (IS_RECORDING) {
      // use default url and record the requests
      wireMockRule.startRecording(
          new RecordSpecBuilder()
              .forTarget("https://api.gateio.ws")
              .matchRequestBodyWithEqualToJson()
              .extractTextBodiesOver(1L)
              .chooseBodyMatchTypeAutomatically());
    }

    exchange = (GateioExchange) ExchangeFactory.INSTANCE.createExchange(exSpec);
  }

  @BeforeClass
  public static void initExchange(){
    ExchangeSpecification exSpec = new ExchangeSpecification(GateioExchange.class);
    AuthUtils.setApiAndSecretKey(exSpec);
    exSpec.setShouldLoadRemoteMetaData(true);
    exSpec.setMetaDataJsonFileOverride(null);
    exchange = (GateioExchange) ExchangeFactory.INSTANCE.createExchange(exSpec);
  }


  @AfterClass
  public static void stop() {
    if (IS_RECORDING) {
      wireMockRule.stopRecording();
    }
  }
}
