package org.knowm.xchange.hashkey.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

/**
 * 订单变化
 * <p> @Date : 2023/11/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class HashKeyExecutionReport {

  /** 事件类型 */
  @JsonProperty("e")
  private String eventType;

  /** 事件时间 */
  @JsonProperty("E")
  private long eventTime;

  /** 交易对 */
  @JsonProperty("s")
  private String symbol;

  /** 客户端订单 ID */
  @JsonProperty("c")
  private long clientOrderId;

  /** 交易方向 */
  @JsonProperty("S")
  private String side;

  /** 订单类型 */
  @JsonProperty("o")
  private String orderType;

  /** 订单时效类型 */
  @JsonProperty("f")
  private String timeInForce;

  /** 订单数量 */
  @JsonProperty("q")
  private BigDecimal orderQuantity;

  /** 订单价格 */
  @JsonProperty("p")
  private BigDecimal orderPrice;

  /** 请求的现金金额 */
  @JsonProperty("reqAmt")
  private BigDecimal requestedCashAmount;

  /** 当前订单状态 */
  @JsonProperty("X")
  private String currentOrderStatus;

  /** 订单 ID */
  @JsonProperty("i")
  private long orderId;

  /** 最后成交数量 */
  @JsonProperty("l")
  private BigDecimal lastExecutedQuantity;

  /** 累计成交数量 */
  @JsonProperty("z")
  private BigDecimal cumulativeFilledQuantity;

  /** 最后成交价格 */
  @JsonProperty("L")
  private BigDecimal lastExecutedPrice;

  /** 佣金金额 */
  @JsonProperty("n")
  private String commissionAmount;

  /** 佣金币种 */
  @JsonProperty("N")
  private String commissionAsset;

  /** 是否正常交易 */
  @JsonProperty("u")
  private boolean isTradeNormal;

  /** 订单是否在进行中 */
  @JsonProperty("w")
  private boolean isOrderWorking;

  /** 是否为Maker交易方向 */
  @JsonProperty("m")
  private boolean isMakerSide;

  /** 订单创建时间 */
  @JsonProperty("O")
  private long orderCreationTime;

  /** 累计报价币种交易数量 */
  @JsonProperty("Z")
  private BigDecimal cumulativeQuoteAssetTransactedQuantity;

  // Getters and Setters

}
