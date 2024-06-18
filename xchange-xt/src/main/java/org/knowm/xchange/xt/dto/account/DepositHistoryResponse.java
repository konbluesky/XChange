package org.knowm.xchange.xt.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2024/6/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class DepositHistoryResponse {
  /**
   * "id": 169669597,       //提现记录id
   *          "currency": "xlm2",    //币种名称
   *          "chain": "XLM",        //转账网络名称
   *          "memo": "441824256",   //memo
   *          "status": "SUCCESS",   //充值状态
   *          "amount": "0.1",       //充值金额
   *          "confirmations": 12,   //区块确认数
   *          "transactionId": "28dd15b5c119e00886517f129e5e1f8283f0286b277bcd3cd1f95f7fd4a1f7fc",   //交易哈希
   *          "address": "GBY6UIYEYLAAXRQXVO7X5I4BSSCS54EAHTUILXWMW6ONPM3PNEA3LWEC",     //充值目标地址
   *          "fromAddr": "GBTISB3JK65DG6LEEYYFW33RMMDHBQ65AEUPE5VDBTCLYYFS533FTG6Q",    //来源地址
   *          "createdTime": 1667260957000   //充值时间，毫秒级时间戳
   */
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
   * 充值网络
   */
  @JsonProperty("chain")
  private String chain;

  /**
   * 充值目标地址
   */
  @JsonProperty("address")
  private String address;

  /**
   * 充值来源地址
   */
  @JsonProperty("fromAddr")
  private String fromAddr;

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
