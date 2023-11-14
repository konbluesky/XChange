package org.knowm.xchange.hashkey.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * 查询订单实体返回类，单一查询和列表查询公共实体，字段的差异见注释；
 * <link>https://hashkeypro-apidoc.readme.io/reference/get-all-orders</link>
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class HashKeyOrderDetailResponse {

  /** 账户编号 */
  private long accountId;

  /** 交易对 */
  private String symbol;

  /** 交易对名称 */
  private String symbolName;

  /** 客户端订单 ID */
  private String clientOrderId;

  /** 系统生成的订单 ID */
  private long orderId;

  /** 价格 */
  private String price;

  /** 下单数量 */
  private String origQty;

  /** 已成交数量 */
  private String executedQty;

  /** 累计报价数量 */
  private String cumulativeQuoteQty;

  /** 平均成交价格 */
  private String avgPrice;

  /** 订单状态 */
  private String status;

  /** 订单有效期类型 */
  private String timeInForce;

  /** 订单类型 */
  private String type;

  /** 订单方向 */
  private String side;

  /** 触发价格 */
  private String stopPrice;

  /** 冰山订单数量 */
  private String icebergQty;

  /** 当前时间戳 */
  private long time;

  /** 更新时间戳 */
  private long updateTime;

  /** 是否正在运行 */
  private boolean isWorking;

  /** 交易所编号 */
  private long exchangeId;

  /** 请求的现金金额 (仅在 HashKeyQueryOrderResponse 中) */
  private String reqAmount;

}
