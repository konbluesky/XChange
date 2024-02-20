package org.knowm.xchange.gateio;

import java.io.IOException;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.gateio.service.GateioAccountService;
import org.knowm.xchange.gateio.service.GateioMarketDataService;
import org.knowm.xchange.gateio.service.GateioMarketDataServiceRaw;
import org.knowm.xchange.gateio.service.GateioTradeService;
import si.mazi.rescu.SynchronizedValueFactory;

public class GateioExchange extends BaseExchange implements Exchange {

  private static ResilienceRegistries RESILIENCE_REGISTRIES;

  @Override
  protected void initServices() {
    this.marketDataService = new GateioMarketDataService(this, getResilienceRegistries());
    this.accountService = new GateioAccountService(this, getResilienceRegistries());
    this.tradeService = new GateioTradeService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.gateio.ws");
    exchangeSpecification.setHost("gateio.ws");
    exchangeSpecification.setExchangeName("Gateio");

    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    throw new ExchangeException("Gate.io does not require a nonce factory.");
  }


  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = GateioResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  @Override
  public void remoteInit() throws IOException {
    if (this.exchangeSpecification.isShouldLoadRemoteMetaData()) {
      exchangeMetaData =
          GateioAdapters.adaptToExchangeMetaData((GateioMarketDataServiceRaw) marketDataService);
    }
  }
}
