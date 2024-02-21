package org.knowm.xchange.gateio.dto;

/**
 * <p> @Date : 2024/2/21 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioEnums {

  public enum AccountType {
    SPOT,
    MARGIN,
    CROSS_MARGIN,
    UNIFIED
  }

  public enum OrderType {
    LIMIT,
    MARKET
  }

  public enum OrderSide {
    BUY,
    SELL
  }

  public enum TimeInForce {
    GTC,
    IOC,
    POC,
    FOK
  }

  public enum StpAct {
    CN,
    CO,
    CB,
    NONE
  }

  public enum ActionMode {
    ACK,
    RESULT,
    FULL
  }

  /**
   * 订单状态。
   *
   * - open: 等待处理
   * - closed: 全部成交
   * - cancelled: 订单撤销
   */
  public enum OrderStatus {
    OPEN,
    CLOSED,
    CANCELLED
  }
}
