package org.knowm.xchange.gateio.service;

import java.util.concurrent.TimeUnit;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.gateio.Gateio;
import org.knowm.xchange.gateio.GateioAuthenticated;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.dto.GateioBaseResponse;
import org.knowm.xchange.service.BaseResilientExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * 包含resilienceRegistries的基类
 * <p> @Date : 2024/2/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioBaseResilientExchangeService extends
    BaseResilientExchangeService<GateioExchange> implements BaseService {

  protected final String apiKey;
  protected final Gateio gateio;
  protected final GateioAuthenticated gateioAuthenticated;
  protected final SynchronizedValueFactory<Long> nonceFactory = new CurrentTimeIncrementalNonceFactory(
      TimeUnit.SECONDS);
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  protected GateioBaseResilientExchangeService(GateioExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);

    gateio =
        ExchangeRestProxyBuilder.forInterface(Gateio.class, exchange.getExchangeSpecification())
            .build();
    gateioAuthenticated =
        ExchangeRestProxyBuilder.forInterface(
                GateioAuthenticated.class, exchange.getExchangeSpecification())
            .build();
    apiKey = exchange.getExchangeSpecification().getApiKey();
    signatureCreator =
        GateioHmacPostBodyDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }

  protected <R extends GateioBaseResponse> R handleResponse(R response) {

    if (!response.isResult()) {
      throw new ExchangeException(response.getMessage());
    }

    return response;
  }
  public String getApiKey() {
    return apiKey;
  }

}
