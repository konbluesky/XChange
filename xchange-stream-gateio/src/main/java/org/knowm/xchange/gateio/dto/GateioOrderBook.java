package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GateioOrderBook {

  private final List<GateioOrderBookItem> asks;
  private final List<GateioOrderBookItem> bids;
  private final String ts;
  private final String symbol;

  @JsonCreator
  public GateioOrderBook(
      @JsonProperty("s") String symbol,
      @JsonProperty("t") String ts,
      @JsonProperty("asks") List<GateioOrderBookItem> asks,
      @JsonProperty("bids") List<GateioOrderBookItem> bids
  ) {
    this.symbol = symbol;
    this.ts = ts;
    this.asks = asks;
    this.bids = bids;
  }
}
