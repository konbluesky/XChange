package org.knowm.xchange.hashkey.service;

import java.util.concurrent.TimeUnit;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.hashkey.HashKey;
import org.knowm.xchange.hashkey.HashKeyAuthenticated;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyBaseService implements BaseService {

  protected final HashKey hashKey;
  protected final HashKeyAuthenticated hashKeyAuthenticated;
  protected final ParamsDigest signatureCreator;
  protected final long recWindow = 5000;

  protected final SynchronizedValueFactory<Long> nonceFactory = new CurrentTimeIncrementalNonceFactory(
      TimeUnit.MILLISECONDS);
  protected final String apiKey;

  public HashKeyBaseService(Exchange exchange) {
    hashKey = ExchangeRestProxyBuilder.forInterface(HashKey.class,
            exchange.getExchangeSpecification())
        .build();
    hashKeyAuthenticated = ExchangeRestProxyBuilder.forInterface(HashKeyAuthenticated.class,
        exchange.getExchangeSpecification()).build();
    signatureCreator = HashKeyDigest.createInstance(
        exchange.getExchangeSpecification().getSecretKey());
    apiKey = exchange.getExchangeSpecification().getApiKey();
  }

}
