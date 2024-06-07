package org.knowm.xchange.xt.dto.trade;

import org.knowm.xchange.dto.Order.IOrderFlags;

/**
 * <p> @Date : 2024/6/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTOrderEnum {

  public enum TimeInForce implements IOrderFlags {
    GTC, FOK, IOC, GTX
  }

}
