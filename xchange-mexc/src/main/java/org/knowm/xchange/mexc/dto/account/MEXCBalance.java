package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MEXCBalance {

  @JsonProperty("asset")
  private String asset;

  @JsonProperty("free")
  private String free;

  @JsonProperty("locked")
  private String locked;
}
