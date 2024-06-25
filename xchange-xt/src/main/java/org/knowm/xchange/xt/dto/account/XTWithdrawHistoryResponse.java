package org.knowm.xchange.xt.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/16 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class XTWithdrawHistoryResponse {

  /**
   * 提现记录ID
   */
  @JsonProperty("id")
  private int id;

  /**
   * 币种名称
   */
  @JsonProperty("currency")
  private String currency;

  /**
   * 提现网络
   */
  @JsonProperty("chain")
  private String chain;

  /**
   * 提现目标地址
   */
  @JsonProperty("address")
  private String address;

  /**
   * 备注
   */
  @JsonProperty("memo")
  private String memo;

  /**
   * 状态，含义见公共模块-充值/提现记录状态码及含义
   */
  @JsonProperty("status")
  private String status;

  /**
   * 提现金额
   */
  @JsonProperty("amount")
  private String amount;

  /**
   * 提现手续费
   */
  @JsonProperty("fee")
  private String fee;

  /**
   * 区块确认数
   */
  @JsonProperty("confirmations")
  private int confirmations;

  /**
   * 交易哈希
   */
  @JsonProperty("transactionId")
  private String transactionId;

  /**
   * 创建时间
   */
  @JsonProperty("createdTime")
  private long createdTime;
}
