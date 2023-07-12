package org.knowm.xchange.xt.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.dto.BalanceResponse;

import java.io.IOException;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTAccountServiceRaw extends XTBaseService {
    /**
     * Constructor
     *
     * @param exchange
     */
    public XTAccountServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public BalanceResponse balance(String currency) throws IOException {
        return xtAuthenticated.balance(
                BaseParamsDigest.HMAC_SHA_256,
                apiKey,
                RECV_WINDOW,
                String.valueOf(System.currentTimeMillis()),
                signatureCreator,
                currency).getData();
    }

    public String time() throws IOException {
        return xt.time().getData().get("serverTime").toString();
    }
}
