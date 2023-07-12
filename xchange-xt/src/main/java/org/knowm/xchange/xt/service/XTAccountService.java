package org.knowm.xchange.xt.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.account.AccountService;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTAccountService extends XTAccountServiceRaw implements AccountService {
    /**
     * Constructor
     *
     * @param exchange
     */
    public XTAccountService(Exchange exchange) {
        super(exchange);
    }
}
