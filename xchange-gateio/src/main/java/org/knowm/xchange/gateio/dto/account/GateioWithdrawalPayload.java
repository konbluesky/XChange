package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GateioWithdrawalPayload {

  @JsonProperty("withdraw_order_id")
  private String withdraw_orderId;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("address")
  private String address;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("memo")
  private String memo;

  @JsonProperty("chain")
  private String chain;
}
