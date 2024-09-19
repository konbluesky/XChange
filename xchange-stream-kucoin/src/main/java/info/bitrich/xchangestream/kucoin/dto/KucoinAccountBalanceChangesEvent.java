package info.bitrich.xchangestream.kucoin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.ToString;

@ToString
public class KucoinAccountBalanceChangesEvent extends KucoinWebSocketEvent {
  @JsonProperty("data")
  public KucoinAccountBalanceChangesEventData data;
}
