package org.knowm.xchange.mexc.dto.account;

import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/8/30 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXCPricePair {

  private String symbol;
  private String price;
}
