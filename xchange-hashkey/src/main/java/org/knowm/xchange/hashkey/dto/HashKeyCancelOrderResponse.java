package org.knowm.xchange.hashkey.dto;

import lombok.Getter;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class HashKeyCancelOrderResponse {

  /** 账户编号 */
  private long accountId;

  /** 交易对 */
  private String symbol;

  /** 客户端订单 ID */
  private String clientOrderId;

  /** 系统生成的订单 ID */
  private long orderId;

  /** 交易时间戳 */
  private long transactTime;

  /** 价格 */
  private String price;

  /** 下单数量 */
  private String origQty;

  /** 已成交数量 */
  private String executedQty;

  /** 订单状态 */
  private String status;

  /** 订单有效期类型 */
  private String timeInForce;

  /** 订单类型 */
  private String type;

  /** 订单方向 */
  private String side;
}