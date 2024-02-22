package org.knowm.xchange.gateio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.knowm.xchange.currency.CurrencyPair;

public class GateioUtils {

  public static String toPairString(CurrencyPair currencyPair) {
    String baseSymbol = currencyPair.base.getCurrencyCode().toLowerCase();
    String counterSymbol = currencyPair.counter.getCurrencyCode().toLowerCase();
    String pair = baseSymbol + "_" + counterSymbol;
    return pair;
  }

  public static CurrencyPair toCurrencyPair(String pair) {
    String[] currencyPairSplit = pair.split("_");
    return new CurrencyPair(currencyPairSplit[0], currencyPairSplit[1]);
  }

  public static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
    objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    return new ObjectMapper();
  }

}
