package org.knowm.xchange.xt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.service.XTAccountService;
import org.knowm.xchange.xt.service.XTAccountServiceRaw;
import org.knowm.xchange.xt.service.XTMarketDataService;
import org.knowm.xchange.xt.service.XTMarketDataServiceRaw;
import org.knowm.xchange.xt.service.XTTradeService;
import org.knowm.xchange.xt.service.XTWsTokenService;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTExchange extends BaseExchange {

  private static ResilienceRegistries resilienceRegistries;
  protected XTWsTokenService xtWsTokenService;

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://sapi.xt.com");
    exchangeSpecification.setHost("xt.com");
    exchangeSpecification.setExchangeName("XT");
    exchangeSpecification.setExchangeDescription("Xt Exchange");
    return exchangeSpecification;
  }


  public XTAccountServiceRaw accountServiceRaw() {
    return new XTAccountServiceRaw(this, getResilienceRegistries());
  }

  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (resilienceRegistries == null) {
      resilienceRegistries = XTResilience.createRegistries();
    }
    return resilienceRegistries;
  }

  @Override
  protected void initServices() {
    this.accountService = new XTAccountService(this, getResilienceRegistries());
    this.marketDataService = new XTMarketDataService(this);
    this.tradeService = new XTTradeService(this);
    this.xtWsTokenService = new XTWsTokenService(this);
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    if (this.exchangeSpecification.isShouldLoadRemoteMetaData()) {
      Map<String, XTCurrencyWalletInfo> currencyWalletInfoMap = ((XTMarketDataServiceRaw) marketDataService)
          .getWalletSupportCurrencysMap(null);
      List<XTSymbol> symbols = ((XTMarketDataServiceRaw) marketDataService).getSymbols(null,
          null);
      exchangeMetaData = XTAdapters.adaptToExchangeMetaData(symbols, currencyWalletInfoMap);
    }
  }


  public XTWsTokenService getWsTokenService() {
    return xtWsTokenService;
  }

}
