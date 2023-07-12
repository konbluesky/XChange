package org.knowm.xchange.xt.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.dto.ws.WebSocketToken;

import java.io.IOException;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTWsTokenService extends XTBaseService {
    /**
     * Constructor
     *
     * @param exchange
     */
    public XTWsTokenService(Exchange exchange) {
        super(exchange);
    }

    public WebSocketToken getWsToken() throws IOException {
        return xtAuthenticated.getWsToken(
                BaseParamsDigest.HMAC_SHA_256,
                apiKey,
                RECV_WINDOW,
                String.valueOf(System.currentTimeMillis()),
                signatureCreator
        ).getData();
    }

}
