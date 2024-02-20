package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2024/2/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class GateioSpotBalanceResponse {

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("available")
  private BigDecimal available;

  @JsonProperty("locked")
  private BigDecimal locked;

  public BigDecimal getTotal() {
    if (available == null) {
      available = BigDecimal.ZERO;
    }
    if (locked == null) {
      locked = BigDecimal.ZERO;
    }

    return available.add(locked);
  }

}
