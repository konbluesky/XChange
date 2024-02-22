package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/7/16 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class GateioTradeDetail {

  @JsonProperty("id")
  private long id;

  @JsonProperty("create_time")
  private String createTime;

  @JsonProperty("create_time_ms")
  private String createTimeMs;

  @JsonProperty("side")
  private String side;

  @JsonProperty("currency_pair")
  private String currencyPair;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("price")
  private BigDecimal price;

  @JsonProperty("range")
  private String range;

}
