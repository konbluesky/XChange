package org.knowm.xchange.binance.config.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.knowm.xchange.binance.dto.trade.TimeInForce;

/**
 * <p> @Date : 2024/8/9 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class StringToTimeInForceConverter extends StdConverter<String, TimeInForce> {

  @Override
  public TimeInForce convert(String value) {
    return TimeInForce.getTimeInForce(value);
  }
}
