package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.InternalServerException;
import org.knowm.xchange.exceptions.RateLimitExceededException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.xt.XT;
import org.knowm.xchange.xt.XTAuthenticated;
import org.knowm.xchange.xt.dto.XTException;
import si.mazi.rescu.ParamsDigest;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTBaseService extends BaseExchangeService {


    protected final String apiKey;
    protected final String secretKey;
    protected final ParamsDigest signatureCreator;
    protected final XT xt;
    protected final String RECV_WINDOW = "6000";
    protected final XTAuthenticated xtAuthenticated;
    protected ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor
     *
     * @param exchange
     */
    protected XTBaseService(Exchange exchange) {
        super(exchange);
        this.apiKey = exchange.getExchangeSpecification().getApiKey();
        this.secretKey = exchange.getExchangeSpecification().getSecretKey();

        this.xt = ExchangeRestProxyBuilder.forInterface(XT.class, exchange.getExchangeSpecification())
                                          .build();
        this.xtAuthenticated = ExchangeRestProxyBuilder.forInterface(XTAuthenticated.class, exchange.getExchangeSpecification())
                                                       .build();

        this.signatureCreator = XTDigest.createInstance(apiKey, secretKey);
        init();
    }


    private void init() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
              .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    protected ExchangeException handleError(XTException exception) {
        if (exception.getMessage().contains("Requests too frequent")) {
            return new RateLimitExceededException(exception);
        } else if (exception.getMessage().contains("System error")) {
            return new InternalServerException(exception);
        } else {
            return new ExchangeException(exception);
        }
    }
}
