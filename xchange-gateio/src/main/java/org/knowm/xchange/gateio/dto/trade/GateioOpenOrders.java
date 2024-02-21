package org.knowm.xchange.gateio.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GateioOpenOrders {

  @JsonProperty("currency_pair")
  private String currencyPair;
  @JsonProperty("total")
  private int total;
  @JsonProperty("orders")
  private List<GateioOrder> orders;

}
