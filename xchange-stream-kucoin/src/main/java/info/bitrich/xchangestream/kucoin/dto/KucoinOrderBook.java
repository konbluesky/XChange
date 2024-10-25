package info.bitrich.xchangestream.kucoin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KucoinOrderBook {

  private final List<KucoinOrderBookItem> asks;
  private final List<KucoinOrderBookItem> bids;
  private final long ts;

  @JsonCreator
  public KucoinOrderBook(
      @JsonProperty("timestamp") long ts,
      @JsonProperty("asks") List<KucoinOrderBookItem> asks,
      @JsonProperty("bids") List<KucoinOrderBookItem> bids
  ) {
    this.ts = ts;
    this.asks = asks;
    this.bids = bids;
  }
}
