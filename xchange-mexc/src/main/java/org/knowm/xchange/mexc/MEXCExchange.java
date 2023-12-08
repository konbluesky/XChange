package org.knowm.xchange.mexc;


import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfo;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;
import org.knowm.xchange.mexc.service.MEXCAccountService;
import org.knowm.xchange.mexc.service.MEXCMarketDataService;
import org.knowm.xchange.mexc.service.MEXCMarketDataServiceRaw;
import org.knowm.xchange.mexc.service.MEXCTradeService;
import org.knowm.xchange.mexc.service.MEXCWsTokenService;

@Slf4j
public class MEXCExchange extends BaseExchange implements Exchange {

  private static ResilienceRegistries RESILIENCE_REGISTRIES;
  protected MEXCWsTokenService wsTokenService;

  @Override
  protected void initServices() {
    this.marketDataService = new MEXCMarketDataService(this, getResilienceRegistries());
    this.tradeService = new MEXCTradeService(this,getResilienceRegistries());
    this.accountService = new MEXCAccountService(this,getResilienceRegistries());
    this.wsTokenService = new MEXCWsTokenService(this,getResilienceRegistries());
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    super.applySpecification(exchangeSpecification);
    if (this.exchangeSpecification == null) {
      exchangeSpecification = new ExchangeSpecification(this.getClass());
      exchangeSpecification.setSslUri("https://api.mexc.com");
      exchangeSpecification.setHost("mexc.com");
      exchangeSpecification.setPort(80);
      exchangeSpecification.setExchangeName("MEXC");
      exchangeSpecification.setExchangeDescription("MEXC");
      exchangeSpecification.setShouldLoadRemoteMetaData(true);
    }
    return exchangeSpecification;
  }

  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = MEXCResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    // warn manually specify
    Set<String> identites = Sets.newHashSet(MEXCNetwork.NETWORK_BSC1, MEXCNetwork.NETWORK_BSC2,
        MEXCNetwork.NETWORK_ARB);
    List<MEXCConfig> coinConfig = ((MEXCMarketDataServiceRaw) this.marketDataService).getAll();
    MEXCExchangeInfo exchangeInfo = ((MEXCMarketDataServiceRaw) this.marketDataService).getExchangeInfo();
    log.info("Load BEP20(BSC) coin quantity:{}", coinConfig.size());

    this.exchangeMetaData = MEXCAdapters.adaptToExchangeMetaData(exchangeInfo,
        coinConfig, identites);

  }

  public MEXCWsTokenService getWsTokenService() {
    return wsTokenService;
  }
}
