package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseResilientExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.xt.XT;
import org.knowm.xchange.xt.XTAuthenticated;
import org.knowm.xchange.xt.XTExchange;
import si.mazi.rescu.ParamsDigest;

/**
 * <p> @Date : 2024/6/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTBaseResilientExchangeService extends
    BaseResilientExchangeService<XTExchange> implements BaseService {

  protected final String apiKey;
  protected final String secretKey;
  protected final ParamsDigest signatureCreator;
  protected final XT xt;
  protected final String RECV_WINDOW = "6000";
  protected final XTAuthenticated xtAuthenticated;
  protected ObjectMapper mapper = new ObjectMapper();

  protected XTBaseResilientExchangeService(XTExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.secretKey = exchange.getExchangeSpecification().getSecretKey();
    this.xt = ExchangeRestProxyBuilder.forInterface(XT.class, exchange.getExchangeSpecification())
        .build();
    this.xtAuthenticated = ExchangeRestProxyBuilder.forInterface(XTAuthenticated.class,
        exchange.getExchangeSpecification()).build();
    this.signatureCreator = XTDigest.createInstance(apiKey, secretKey);
    this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }


}
