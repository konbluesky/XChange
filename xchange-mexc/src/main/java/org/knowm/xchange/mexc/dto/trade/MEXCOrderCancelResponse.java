package org.knowm.xchange.mexc.dto.trade;

import lombok.Getter;

/**
 * <p> @Date : 2023/8/30 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class MEXCOrderCancelResponse {

  /**
   * 交易对
   */
  private String symbol;

  /**
   * 原始客户端订单ID
   */
  private String origClientOrderId;

  /**
   * 订单ID
   */
  private long orderId;

  /**
   * OCO订单ID，否则为 -1
   */
  private long orderListId;

  /**
   * 客户端订单ID
   */
  private String clientOrderId;

  /**
   * 价格
   */
  private String price;

  /**
   * 原始数量
   */
  private String origQty;

  /**
   * 已执行数量
   */
  private String executedQty;

  /**
   * 累积报价数量
   */
  private String cummulativeQuoteQty;

  /**
   * 状态
   */
  private String status;

  /**
   * 有效方式
   */
  private String timeInForce;

  /**
   * 订单类型
   */
  private String type;

  /**
   * 买卖方向
   */
  private String side;
}

