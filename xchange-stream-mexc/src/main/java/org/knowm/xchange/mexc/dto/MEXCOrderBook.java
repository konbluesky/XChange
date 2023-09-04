package org.knowm.xchange.mexc.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MEXCOrderBook {

  private final List<MEXCOrderBookItem> asks;
  private final List<MEXCOrderBookItem> bids;
  private final String version;

  @JsonCreator
  public MEXCOrderBook(
      @JsonProperty("r") String version,
      @JsonProperty("asks") List<MEXCOrderBookItem> asks,
      @JsonProperty("bids") List<MEXCOrderBookItem> bids
  ) {
    this.asks = asks;
    this.bids = bids;
    this.version = version;
  }
}
