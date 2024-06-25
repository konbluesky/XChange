package org.knowm.xchange.xt.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class XTCurrencyInfo {

  private String id;
  private String currency;
  @JsonProperty("fullName")
  private String fullName;
  @JsonProperty("displayName")
  private String displayName;
  private String type;
  private String logo;
  @JsonProperty("cmcLink")
  private String cmcLink;
  private String weight;
  @JsonProperty("nominalValue")
  private String nominalValue;
  @JsonProperty("maxPrecision")
  private String maxPrecision;
  @JsonProperty("depositStatus")
  private String depositStatus;
  @JsonProperty("withdrawStatus")
  private String withdrawStatus;
  @JsonProperty("convertEnabled")
  private String convertEnabled;
  @JsonProperty("transferEnabled")
  private String transferEnabled;
  @JsonProperty("isChainExist")
  private String isChainExist;
  private String[] plates;
}
