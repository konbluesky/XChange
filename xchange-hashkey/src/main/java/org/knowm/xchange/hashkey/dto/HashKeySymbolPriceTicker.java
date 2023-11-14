package org.knowm.xchange.hashkey.dto;

import lombok.Getter;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class HashKeySymbolPriceTicker {

  private String symbol; // 货币对
  private String latestPrice; // 最新价格


}