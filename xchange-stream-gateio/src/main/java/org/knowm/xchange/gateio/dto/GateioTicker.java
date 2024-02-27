package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class GateioTicker {

  /**
   * 交易对
   */
  @JsonProperty("currency_pair")
  private String currencyPair;

  /**
   * 最新成交价
   */
  @JsonProperty("last")
  private BigDecimal last;

  /**
   * 最新卖方最低价
   */
  @JsonProperty("lowest_ask")
  private BigDecimal lowestAsk;

  /**
   * 最新买方最高价
   */
  @JsonProperty("highest_bid")
  private BigDecimal highestBid;

  /**
   * 最近24小时涨跌百分比
   */
  @JsonProperty("change_percentage")
  private BigDecimal changePercentage;

  /**
   * 最近24小时交易货币成交量
   */
  @JsonProperty("base_volume")
  private BigDecimal baseVolume;

  /**
   * 最近24小时计价货币成交量
   */
  @JsonProperty("quote_volume")
  private BigDecimal quoteVolume;

  /**
   * 24小时最高价
   */
  @JsonProperty("high_24h")
  private BigDecimal high24h;

  /**
   * 24小时最低价
   */
  @JsonProperty("low_24h")
  private BigDecimal low24h;

  @JsonProperty("time_ms")
  private long timeMs;

  @JsonProperty("time")
  private String time;


}
