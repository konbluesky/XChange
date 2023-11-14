package org.knowm.xchange.hashkey.dto;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public enum HashKeyTradeRestrictionType {
  PRICE_FILTER, // 价格限制
  LOT_SIZE, // 交易数量限制
  MIN_NOTIONAL, // 最小名义限制
  TRADE_AMOUNT, // 交易金额限制
  LIMIT_TRADING, // 限价交易规则
  MARKET_TRADING, // 市价交易规则
  OPEN_QUOTE // 开市限制
}