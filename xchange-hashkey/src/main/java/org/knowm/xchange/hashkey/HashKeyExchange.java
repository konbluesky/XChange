package org.knowm.xchange.hashkey;

import java.io.IOException;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.hashkey.service.HashKeyAccountService;
import org.knowm.xchange.hashkey.service.HashKeyTradeService;
import org.knowm.xchange.hashkey.service.HashKeyWsTokenService;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.TradeService;

/**
 * <p> @Date : 2023/11/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyExchange extends BaseExchange implements Exchange {

  protected HashKeyWsTokenService wsTokenService;

  @Override
  public TradeService getTradeService() {
    return this.tradeService;
  }

  @Override
  protected void initServices() {
//    this.marketDataService = new MEXCMarketDataService(this);
    this.tradeService = new HashKeyTradeService(this);
    this.accountService = new HashKeyAccountService(this);
    this.wsTokenService = new HashKeyWsTokenService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api-pro.hashkey.com");
    exchangeSpecification.setHost("hashkey.com");
    exchangeSpecification.setPort(80);
//    exchangeSpecification.setExchangeName("HashKey");
//    exchangeSpecification.setExchangeDescription("HashKey");
//    exchangeSpecification.setShouldLoadRemoteMetaData(true);
    return exchangeSpecification;
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    String bscIdentity = "BEP20(BSC)";
//    List<MEXCConfig> coinConfig = ((MEXCMarketDataServiceRaw) this.marketDataService).getAll();
//    MEXCExchangeInfo exchangeInfo = ((MEXCMarketDataServiceRaw) this.marketDataService).getExchangeInfo();
//    log.info("Load BEP20(BSC) coin quantity:{}", coinConfig.size());
//
//    this.exchangeMetaData = MEXCAdapters.adaptToExchangeMetaData(exchangeInfo,
//        coinConfig, bscIdentity);

  }

  public HashKeyWsTokenService getWsTokenService() {
    return wsTokenService;
  }

  @Override
  public AccountService getAccountService() {
    return this.accountService;
  }
}
