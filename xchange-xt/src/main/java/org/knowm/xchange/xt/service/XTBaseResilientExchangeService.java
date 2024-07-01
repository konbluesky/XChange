package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.client.ClientConfigCustomizer;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseResilientExchangeService;
import org.knowm.xchange.xt.XT;
import org.knowm.xchange.xt.XTAuthenticated;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.dto.XTResponse;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.serialization.jackson.DefaultJacksonObjectMapperFactory;

/**
 * <p> @Date : 2024/6/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTBaseResilientExchangeService extends BaseResilientExchangeService<XTExchange> {

  protected final String apiKey;
  protected final String secretKey;
  protected final ParamsDigest signatureCreator;
  protected final XT xt;
  protected final String RECV_WINDOW = "60000";
  protected final XTAuthenticated xtAuthenticated;
  protected ObjectMapper mapper = new ObjectMapper();

  protected XTBaseResilientExchangeService(XTExchange exchange,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    ClientConfigCustomizer clientConfigCustomizer = config -> {
      config.setJacksonObjectMapperFactory(new DefaultJacksonObjectMapperFactory() {
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
        exchange.getExchangeSpecification()).clientConfigCustomizer(clientConfigCustomizer).build();
    this.signatureCreator = XTDigest.createInstance(apiKey, secretKey);
    this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  protected <R> R safeGetResponse(XTResponse<R> response){
    if(response.isSuccess()){
      return response.getData();
    }else {
      throw new RuntimeException(response.toString());
    }
  }

}
