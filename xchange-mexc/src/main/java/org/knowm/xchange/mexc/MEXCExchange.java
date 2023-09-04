package org.knowm.xchange.mexc;


import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.mexc.service.MEXCAccountService;
import org.knowm.xchange.mexc.service.MEXCMarketDataService;
import org.knowm.xchange.mexc.service.MEXCTradeService;
import org.knowm.xchange.mexc.service.MEXCWsTokenService;

public class MEXCExchange extends BaseExchange implements Exchange {

  protected MEXCWsTokenService wsTokenService;

  @Override
  protected void initServices() {
    this.marketDataService = new MEXCMarketDataService(this);
    this.tradeService = new MEXCTradeService(this);
    this.accountService = new MEXCAccountService(this);
    this.wsTokenService = new MEXCWsTokenService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.mexc.com");
    exchangeSpecification.setHost("mexc.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("MEXC");
    exchangeSpecification.setExchangeDescription("MEXC");
    return exchangeSpecification;
  }


  public MEXCWsTokenService getWsTokenService() {
    return wsTokenService;
  }
}
