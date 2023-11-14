package org.knowm.xchange.hashkey.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/11/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */

@ToString
@Getter
public class HashKeyExchangeInfo {

  private String timezone;

  private long serverTime;

  private List<HashKeyExchangeInfoSymbol> symbols;

}
