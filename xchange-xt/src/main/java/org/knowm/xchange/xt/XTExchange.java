package org.knowm.xchange.xt;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.xt.service.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTExchange extends BaseExchange {

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
        return new XTAccountServiceRaw(this);
    }


    @Override
    protected void initServices() {
        this.accountService = new XTAccountService(this);
        this.marketDataService = new XTMarketDataService(this);
        this.tradeService = new XTTradeService(this);
        this.xtWsTokenService = new XTWsTokenService(this);
    }

    @Override
    protected void loadExchangeMetaData(InputStream is) {
    }

    @Override
    public void remoteInit() throws IOException, ExchangeException {
//        exchangeMetaData = XTAdapters.adaptToExchangeMetaData(null,null);
    }


    public XTWsTokenService getWsTokenService() {
        return xtWsTokenService;
    }

}
