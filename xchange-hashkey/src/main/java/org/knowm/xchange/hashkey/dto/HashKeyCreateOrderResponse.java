package org.knowm.xchange.hashkey.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class HashKeyCreateOrderResponse {

  /** 账户 ID */
  private String accountId;
  /** 交易对 */
  private String symbol;
  /** 交易对名称 */
  private String symbolName;
  /** 客户端订单 ID */
  private String clientOrderId;
  /** 订单 ID */
  private long orderId;
  /** 订单交易时间戳 */
  private long transactTime;
  /** 价格 */
  private double price;
  /** 初始数量 */
  private double origQty;
  /** 请求金额 */
  private double reqAmount;
  /** 成交数量 */
  private double executedQty;
  /** 订单状态 */
  private String status;
  /** 订单有效期 */
  private String timeInForce;
  /** 订单类型 */
  private String type;
  /** 交易方向 */
  private String side;
}
