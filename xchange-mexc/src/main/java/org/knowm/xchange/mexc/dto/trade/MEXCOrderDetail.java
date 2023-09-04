package org.knowm.xchange.mexc.dto.trade;

import lombok.Getter;

/**
 * <p> @Date : 2023/8/30 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class MEXCOrderDetail {

  /**
   * 交易对
   */
  private String symbol;

  /**
   * 系统的订单ID
   */
  private String orderId;

  /**
   * OCO订单的ID，不然就是-1
   */
  private long orderListId;

  /**
   * 客户自己设置的ID
   */
  private String clientOrderId;

  /**
   * 订单价格
   */
  private String price;

  /**
   * 用户设置的原始订单数量
   */
  private String origQty;

  /**
   * 交易的订单数量
   */
  private String executedQty;

  /**
   * 累计交易的金额
   */
  private String cummulativeQuoteQty;

  /**
   * 订单状态
   */
  private String status;

  /**
   * 订单的时效方式
   */
  private String timeInForce;

  /**
   * 订单类型，比如市价单，现价单等
   */
  private String type;

  /**
   * 订单方向，买还是卖
   */
  private String side;

  /**
   * 止损价格
   */
  private String stopPrice;

  /**
   * 冰山数量
   */
  private String icebergQty;

  /**
   * 订单时间
   */
  private long time;

  /**
   * 最后更新时间
   */
  private long updateTime;

  /**
   * 订单是否出现在orderbook中
   */
  private boolean isWorking;

  /**
   * 原始的交易金额
   */
  private String origQuoteOrderQty;
}
