package org.knowm.xchange.mexc.dto.trade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MEXCOrder {

  private final String id;
  private final String symbol;
  private final String price;
  private final String quantity;
  private final String side;
  private final String type;
  private final long transactTime;

  @JsonCreator
  public MEXCOrder(
      @JsonProperty("orderId") String id,
      @JsonProperty("symbol") String symbol,
      @JsonProperty("price") String price,
      @JsonProperty("origQty") String quantity,
      @JsonProperty("side") String side,
      @JsonProperty("type") String type,
      @JsonProperty("transactTime") long transactTime) {
    this.id = id;
    this.symbol = symbol;
    this.price = price;
    this.quantity = quantity;
    this.side = side;
    this.type = type;
    this.transactTime = transactTime;
  }
}


