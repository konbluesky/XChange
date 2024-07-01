package org.knowm.xchange.xt.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Builder
@Getter
@ToString
public class PlaceOrderRequest {

  /**
   * 交易对
   */
  private String symbol;

  /**
   * 客户端ID正则：^[a-zA-Z0-9_]{4,32}$
   */
  @JsonProperty("clientOrderId")
  private String clientOrderId;
  /**
   * 买卖方向  BUY-买,SELL-卖
   */
  @JsonProperty("side")
  private String side;

  /**
   * 订单类型  LIMIT-现价,MARKET-市价
   */
  @JsonProperty("type")
  private String type;

  /**
   * 有效方式 GTC, FOK, IOC, GTX
   */
  @JsonProperty("timeInForce")
  private String timeInForce;

  /**
   * 业务类型 SPOT-现货, LEVER-杠杆
   */
  @JsonProperty("bizType")
  private String bizType;

  /**
   * 价格。现价必填; 市价不填
   */
  @JsonProperty("price")
  private String price;

  /**
   * 数量。现价必填；市价按数量下单时必填
   */
  @JsonProperty("quantity")
  private String quantity;

  /**
   * 金额。现价不填；市价按金额下单时必填
   */
  @JsonProperty("quoteQty")
  private String quoteQty;


}
