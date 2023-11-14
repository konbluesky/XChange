package org.knowm.xchange.hashkey.dto;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyEnum {

  public enum Side {
    BUY, SELL, UNKNOW;

    public static Side fromString(String s) {
      for (Side side : Side.values()) {
        if (side.name().equalsIgnoreCase(s)) {
          return side;
        }
      }
      return UNKNOW;
    }
  }

  public enum OrderType {
    LIMIT, MARKET, LIMIT_MARKER, UNKNOW;

    public static OrderType fromString(String s) {
      for (OrderType type : OrderType.values()) {
        if (type.name().equalsIgnoreCase(s)) {
          return type;
        }
      }
      return UNKNOW;
    }
  }

  /**
   * Order Status NEW - New order, pending to be filled PARTIALLY_FILLED - Partially filled
   * PARTIALLY_CANCELED - Partially filled and Partially cancelled FILLED - Completed filled
   * CANCELED - Order cancelled PARTIALLY_CANCELED - Partially cancelled PENDING_CANCEL - Pending
   * order to be cancelled REJECTED - Order request is rejected Order Types LIMIT - Limit order
   * MARKET - Market order LIMIT_MAKER - maker limit order Order Direction BUY - Buy order SELL -
   * Sell Order TimeInForce GTC（Currently only supports limit orders) IOC（Currently only supports
   * market orders)
   */
  public enum OrderStatus {
    NEW, PARTIALLY_FILLED, FILLED, CANCELED, PENDING_CANCEL, REJECTED, EXPIRED, UNKNOW;

    public static OrderStatus fromString(String s) {
      for (OrderStatus status : OrderStatus.values()) {
        if (status.name().equalsIgnoreCase(s)) {
          return status;
        }
      }
      return UNKNOW;
    }
  }

  public enum TimeInForce {
    GTC, IOC;

    public static TimeInForce fromString(String s) {
      for (TimeInForce force : TimeInForce.values()) {
        if (force.name().equalsIgnoreCase(s)) {
          return force;
        }
      }
      return null;
    }
  }
}
