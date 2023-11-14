package org.knowm.xchange.hashkey.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class HashKeyTickerInfo {

  /** 事件类型 */
  @JsonProperty("e")
  private String eventType;

  /** 事件时间 */
  @JsonProperty("E")
  private String eventTime;

  /** 交易对 */
  @JsonProperty("s")
  private String symbol;

  /** 数量 */
  @JsonProperty("q")
  private BigDecimal quantity;

  /** 时间 */
  @JsonProperty("t")
  private String time;

  /** 价格 */
  @JsonProperty("p")
  private BigDecimal price;

  /** Ticket ID */
  @JsonProperty("T")
  private String ticketId;

  /** 订单 ID */
  @JsonProperty("o")
  private String orderId;

  /** 客户端订单 ID */
  @JsonProperty("c")
  private String clientOrderId;

  /** 匹配订单 ID */
  @JsonProperty("O")
  private String matchOrderId;

  /** 账户 ID */
  @JsonProperty("a")
  private String accountId;

  /** 匹配账户 ID */
  @JsonProperty("A")
  private String matchAccountId;

  /** 是否为Maker */
  @JsonProperty("m")
  private boolean isMaker;

  /** 交易方向 */
  @JsonProperty("S")
  private String side; // SELL or BUY

}
