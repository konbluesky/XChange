package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.knowm.xchange.dto.Order;

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

  public enum TimeInForce implements Order.IOrderFlags {
    GTC("gtc"),
    IOC("ioc"),
    POC("poc"),
    FOK("fok");

    TimeInForce(String value) {
      this.value = value;
    }
    private String value;
    @JsonCreator
    public static TimeInForce fromValue(String value) {
      for (TimeInForce timeInForce : TimeInForce.values()) {
        if (timeInForce.value.equalsIgnoreCase(value)) {
          return timeInForce;
        }
      }
      throw new IllegalArgumentException("Unknown value: " + value);
    }
    @JsonValue
    public String getValue() {
      return value;
    }
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

  /**
   * 订单结束方式，包括：
   *
   * - open: 等待处理
   * - filled: 完全成交
   * - cancelled: 用户撤销
   * - ioc: 未立即完全成交，因为 tif 设置为 ioc
   * - stp: 订单发生自成交限制而被撤销
   */
  public enum FinishAs {
    OPEN,
    FILLED,
    CANCELLED,
    IOC,
    STP
  }



}
