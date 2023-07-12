package org.knowm.xchange.xt.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTMarketDataService extends XTMarketDataServiceRaw implements MarketDataService {
    /**
     * Constructor
     *
     * @param exchange
     */
    public XTMarketDataService(Exchange exchange) {
        super(exchange);
    }



}
