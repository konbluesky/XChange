package org.knowm.xchange.mexc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class MEXCOrderDetail {
  /**
   * d	json	账户订单信息
   * > A	bigDecimal	实际剩余金额: remainAmount
   * > O	long	订单创建时间
   * > S	int	交易类型 1:买 2:卖
   * > V	bigDecimal	实际剩余数量: remainQuantity
   * > a	bigDecimal	下单总金额
   * > c	string	用户自定义订单id: clientOrderId
   * > i	string	订单id
   * > m	int	是否是挂单: isMaker
   * > o	int	订单类型LIMIT_ORDER(1),POST_ONLY(2),IMMEDIATE_OR_CANCEL(3),
   * FILL_OR_KILL(4),MARKET_ORDER(5); 止盈止损（100）
   * > p	bigDecimal	下单价格
   * > s	int	订单状态 1:未成交 2:已成交 3:部分成交 4:已撤单 5:部分撤单
   * > v	bigDecimal	下单数量
   * > ap	bigDecimal	平均成交价
   * > cv	bigDecimal	累计成交数量
   * > ca	bigDecimal	累计成交金额
   * t	long	事件时间
   * s	string	交易对
   */
  /**
   * 实际剩余金额
   */
  @JsonProperty("A")
  private BigDecimal remainAmount;

  /**
   * 订单创建时间
   */
  @JsonProperty("O")
  private Long createTime;

  /**
   * 交易类型 1:买 2:卖
   */
  @JsonProperty("S")
  private int side;

  /**
   * 实际剩余数量
   */
  @JsonProperty("V")
  private BigDecimal remainQuantity;

  /**
   * 下单总金额
   */
  @JsonProperty("a")
  private BigDecimal amount;

  /**
   * 用户自定义订单id
   */
  @JsonProperty("c")
  private String clientOrderId;

  /**
   * 订单id
   */
  @JsonProperty("i")
  private String orderId;

  /**
   * 是否是挂单
   */
  @JsonProperty("m")
  private int isMaker;

  /**
   * 订单类型 LIMIT_ORDER(1),POST_ONLY(2),IMMEDIATE_OR_CANCEL(3), FILL_OR_KILL(4),MARKET_ORDER(5);
   * 止盈止损（100）
   */
  @JsonProperty("o")
  private int orderType;

  /**
   * 下单价格
   */
  @JsonProperty("p")
  private BigDecimal price;

  /**
   * 订单状态
   */
  @JsonProperty("s")
  private int status;

  /**
   * 下单数量
   */
  @JsonProperty("v")
  private BigDecimal quantity;

  /**
   * 平均成交价
   */
  @JsonProperty("ap")
  private BigDecimal avgPrice;

  /**
   * 累计成交数量
   */
  @JsonProperty("cv")
  private BigDecimal cumQuantity;

}
