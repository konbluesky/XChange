package org.knowm.xchange.mexc.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class MEXCOrderRequestPayload {

  /**
   * 交易对
   */
  @JsonProperty("symbol")
  private String symbol;

  /**
   * 订单方向，枚举类型，详见定义
   */
  @JsonProperty("side")
  private String side;

  /**
   * 订单类型，枚举类型，详见定义
   */
  @JsonProperty("type")
  private String type;

  /**
   * 委托数量
   */
  @JsonProperty("quantity")
  private String quantity;

  /**
   * 委托总额
   */
  @JsonProperty("quoteOrderQty")
  private String quoteOrderQty;

  /**
   * 委托价格
   */
  @JsonProperty("price")
  private String price;

  /**
   * 客户自定义的唯一订单ID
   */
  @JsonProperty("newClientOrderId")
  private String newClientOrderId;

  /**
   * 赋值不能大于 60000
   */
  @JsonProperty("recvWindow")
  private Long recvWindow;

  /**
   * 时间戳
   */
  @JsonProperty("timestamp")
  private Long timestamp;

}
