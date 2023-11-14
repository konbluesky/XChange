package org.knowm.xchange.hashkey.dto.params;

import lombok.Getter;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParamInstrument;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamInstrument;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamLimit;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class HashKeyOpenOrdersParams extends DefaultOpenOrdersParamInstrument implements
    OpenOrdersParamLimit,
    OpenOrdersParamInstrument {

  private String orderId;

  private Integer limit = 500;

  @Override
  public Integer getLimit() {
    return limit;
  }

  @Override
  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  @Override
  public boolean accept(LimitOrder order) {
    return super.accept(order);
  }

  @Override
  public boolean accept(Order order) {
    return super.accept(order);
  }
}
