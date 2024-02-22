package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GateioWithdrawalResponse {

  @JsonProperty("id")
  private String Id;

  @JsonProperty("txid")
  private String transferHash;

  @JsonProperty("withdraw_order_id")
  private String withDrawOrderId;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("address")
  private String address;

  @JsonProperty("memo")
  private String memo;

  @JsonProperty("status")
  private String status;

  @JsonProperty("chain")
  private String withdrawalChain;
}
