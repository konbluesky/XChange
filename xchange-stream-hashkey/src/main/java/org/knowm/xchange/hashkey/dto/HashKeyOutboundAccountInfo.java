package org.knowm.xchange.hashkey.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/11/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class HashKeyOutboundAccountInfo {

  /** 事件类型 */
  @JsonProperty("e")
  private String eventType;

  /** 事件时间 */
  @JsonProperty("E")
  private long eventTime;

  /** 是否可以交易 */
  @JsonProperty("T")
  private boolean canTrade;

  /** 是否可以提取 */
  @JsonProperty("W")
  private boolean canWithdraw;

  /** 是否可以存款 */
  @JsonProperty("D")
  private boolean canDeposit;

  /** 账户余额列表 */
  @JsonProperty("B")
  private List<BalanceInfo> balances;

  @Getter
  public static class BalanceInfo {

    /** 资产 */
    @JsonProperty("a")
    private String asset;

    /** 可用余额 */
    @JsonProperty("f")
    private BigDecimal free;

    /** 冻结余额 */
    @JsonProperty("l")
    private BigDecimal locked;

    // Getters and Setters
  }

}
