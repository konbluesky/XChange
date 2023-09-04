package org.knowm.xchange.mexc.dto.account;

/**
 * <p> @Date : 2023/8/30 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MEXCAccount {

  @JsonProperty("makerCommission")
  private int makerCommission;

  @JsonProperty("takerCommission")
  private int takerCommission;

  @JsonProperty("buyerCommission")
  private int buyerCommission;

  @JsonProperty("sellerCommission")
  private int sellerCommission;

  @JsonProperty("canTrade")
  private boolean canTrade;

  @JsonProperty("canWithdraw")
  private boolean canWithdraw;

  @JsonProperty("canDeposit")
  private boolean canDeposit;

  @JsonProperty("updateTime")
  private Long updateTime;

  @JsonProperty("accountType")
  private String accountType;

  @JsonProperty("balances")
  private List<MEXCBalance> balances;

  @JsonProperty("permissions")
  private List<String> permissions;


  @Getter
  public static class Balance {

    @JsonProperty("asset")
    private String asset;

    @JsonProperty("free")
    private String free;

    @JsonProperty("locked")
    private String locked;
  }
}
