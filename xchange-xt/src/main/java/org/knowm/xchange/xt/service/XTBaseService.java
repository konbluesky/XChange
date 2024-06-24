package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ClientConfigCustomizer;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.InternalServerException;
import org.knowm.xchange.exceptions.RateLimitExceededException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.xt.XT;
import org.knowm.xchange.xt.XTAuthenticated;
import org.knowm.xchange.xt.dto.XTException;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.serialization.jackson.DefaultJacksonObjectMapperFactory;

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
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.secretKey = exchange.getExchangeSpecification().getSecretKey();
    this.xt = ExchangeRestProxyBuilder.forInterface(XT.class, exchange.getExchangeSpecification())
        .build();
    this.xtAuthenticated = ExchangeRestProxyBuilder.forInterface(XTAuthenticated.class,
            exchange.getExchangeSpecification())
        .clientConfigCustomizer(clientConfigCustomizer)
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
