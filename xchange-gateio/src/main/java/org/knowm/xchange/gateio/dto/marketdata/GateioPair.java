package org.knowm.xchange.gateio.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2024/2/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class GateioPair {
  /**
   * {
   *     "id": "ETH_USDT",
   *     "base": "ETH",
   *     "quote": "USDT",
   *     "fee": "0.2",
   *     "min_base_amount": "0.001",
   *     "min_quote_amount": "1.0",
   *     "max_base_amount": "10000",
   *     "max_quote_amount": "10000000",
   *     "amount_precision": 3,
   *     "precision": 6,
   *     "trade_status": "tradable",
   *     "sell_start": 1516378650,
   *     "buy_start": 1516378650
   *   }
   */
  private String id;
  private String base;
  private String quote;
  private String fee;
  @JsonProperty("min_quote_amount")
  private String minQuoteAmount;
  @JsonProperty("max_quote_amount")
  private String maxQuoteAmount;
  @JsonProperty("amount_precision")
  private int amountPrecision;
  private int precision;
  @JsonProperty("trade_status")
  private String tradeStatus;
  @JsonProperty("sell_start")
  private long sellStart;
  @JsonProperty("buy_start")
  private long buyStart;

}
