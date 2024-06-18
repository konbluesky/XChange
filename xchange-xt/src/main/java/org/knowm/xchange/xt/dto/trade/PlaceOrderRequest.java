package org.knowm.xchange.xt.dto.trade;

import lombok.Builder;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Builder
@Getter
public class PlaceOrderRequest {

  /**
   * 交易对
   */
  private String symbol;

  /**
   * 客户端ID正则：^[a-zA-Z0-9_]{4,32}$
   */
  private String clientOrderId;
  /**
   * 买卖方向  BUY-买,SELL-卖
   */
  private String side;

  /**
   * 订单类型  LIMIT-现价,MARKET-市价
   */
  private String type;

  /**
   * 有效方式 GTC, FOK, IOC, GTX
   */
  private String timeInForce;

  /**
   * 业务类型 SPOT-现货, LEVER-杠杆
   */
  private String bizType;

  /**
   * 价格。现价必填; 市价不填
   */
  private String price;

  /**
   * 数量。现价必填；市价按数量下单时必填
   */
  private String quantity;

  /**
   * 金额。现价不填；市价按金额下单时必填
   */
  private String quoteQty;


}
