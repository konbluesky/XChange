package org.knowm.xchange.gateio.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.concurrent.TimeUnit;
import org.knowm.xchange.client.ClientConfigCustomizer;
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
import si.mazi.rescu.ClientConfigUtil;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;
import si.mazi.rescu.serialization.jackson.DefaultJacksonObjectMapperFactory;

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
  protected final SynchronizedValueFactory<Long> timestampFactory = new CurrentTimeIncrementalNonceFactory(
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
    ClientConfigCustomizer clientConfigCustomizer =
        config -> {
          config.setJacksonObjectMapperFactory(
              new DefaultJacksonObjectMapperFactory() {
                @Override
                public void configureObjectMapper(ObjectMapper objectMapper) {
                  super.configureObjectMapper(objectMapper);
                  // 设置字段为 null 时不序列化
                  objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//                  objectMapper.setPropertyNamingStrategy(LowerCaseStrategy.INSTANCE);
                  objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
//                  objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
                  // 反序列化 允许小写命中
                  objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
                }
              });
        };
    gateio =
        ExchangeRestProxyBuilder.forInterface(Gateio.class, exchange.getExchangeSpecification())
            .build();
    gateioAuthenticated =
        ExchangeRestProxyBuilder.forInterface(
                GateioAuthenticated.class, exchange.getExchangeSpecification())
            .clientConfigCustomizer(clientConfigCustomizer)
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
