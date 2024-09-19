package info.bitrich.xchangestream.kucoin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.ToString;

/**
 * <p> @Date : 2024/9/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
public class KucoinAccountBalanceChangesEventData {

  /**
   * "total": "88", //總額
   *         "available": "88", // 可用餘額
   *         "availableChange": "88", // 可用餘額變化值
   *         "currency": "KCS", // 幣種
   *         "hold": "0", // 凍結金額
   *         "holdChange": "0", // 可用凍結金額變化值
   *         "relationEvent": "trade.setted", // 關聯事件
   *         "relationEventId": "5c21e80303aa677bd09d7dff", // 關聯事件id
   *         "relationContext": {
   *             "tradeId":"5e6a5dca9e16882a7d83b7a4", // 成交了纔會有tradeId
   *             "orderId":"5ea10479415e2f0009949d54",
   *             "symbol":"BTC-USDT"
   *         }, // 交易事件的上下文
   */
  @JsonProperty("total")
  public BigDecimal total;

  @JsonProperty("available")
  public BigDecimal available;

  @JsonProperty("availableChange")
  public BigDecimal availableChange;

  @JsonProperty("currency")
  public String currency;

  @JsonProperty("hold")
  public BigDecimal hold;

  @JsonProperty("holdChange")
  public BigDecimal holdChange;

  @JsonProperty("relationEvent")
  public String relationEvent;

  @JsonProperty("relationEventId")
  public String relationEventId;

  @JsonProperty("time")
  public Date time;

}
