package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class GateioBalance {

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("timestamp_ms")
  private String timestampMs;

  @JsonProperty("user")
  private String user;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("change")
  private String change;

  @JsonProperty("total")
  private String total;

  @JsonProperty("available")
  private String available;

  @JsonProperty("freeze")
  private String freeze;

  @JsonProperty("freeze_change")
  private String freezeChange;

  @JsonProperty("change_type")
  private String changeType;


}
