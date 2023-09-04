package org.knowm.xchange.mexc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXCTicker {

  /**
   * {
   *                     "S":2,                             //交易类型tradeType
   *                     "p":"20233.84",                    //成交价格price
   *                     "t":1661927587825,                         //成交时间dealTime
   *                     "v":"0.001028"}
   */

  /**
   * 交易类型
   */
  @JsonProperty("S")
  private int tradeType;

  /**
   * 成交价格
   */
  @JsonProperty("p")
  private String price;

  /**
   * 成交时间
   */
  @JsonProperty("t")
  private long dealTime;

  /**
   * 成交量
   */
  @JsonProperty("v")
  private String quantity;

}
