package org.knowm.xchange.mexc.dto.account;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/9/6 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXCExchangeInfo {

  private String timezone;

  private long serverTime;

  private List<MEXCExchangeInfoSymbol> symbols;

}
