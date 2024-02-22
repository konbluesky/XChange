package org.knowm.xchange.gateio.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

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
   * 最近24小时涨跌百分比，跌用负数标识，如 -7.45
   */
  @JsonProperty("change_percentage")
  private BigDecimal changePercentage;

  /**
   * UTC0时区，最近24小时涨跌百分比，跌用负数标识，如 -7.45
   */
  @JsonProperty("change_utc0")
  private String changeUtc0;

  /**
   * UTC8时区，最近24小时涨跌百分比，跌用负数标识，如 -7.45
   */
  @JsonProperty("change_utc8")
  private String changeUtc8;

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

  /**
   * ETF净值
   */
  @JsonProperty("etf_net_value")
  private String etfNetValue;

  /**
   * ETF前一再平衡点净值
   */
  @JsonProperty("etf_pre_net_value")
  private String etfPreNetValue;

  /**
   * ETF前一再平衡时间
   */
  @JsonProperty("etf_pre_timestamp")
  private Long etfPreTimestamp;

  /**
   * ETF当前杠杆率
   */
  @JsonProperty("etf_leverage")
  private String etfLeverage;
}
