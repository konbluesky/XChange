package org.knowm.xchange.gateio.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GateioCoin {

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("delisted")
  private boolean delisted;

  @JsonProperty("withdraw_disabled")
  private boolean withdrawDisabled;

  @JsonProperty("withdraw_delayed")
  private boolean withdrawDelayed;

  @JsonProperty("deposit_disabled")
  private boolean depositDisabled;

  @JsonProperty("trade_disabled")
  private boolean tradeDisabled;

  @JsonProperty("fixed_rate")
  private String fixedRate;

  @JsonProperty("chain")
  private String chain;



}
