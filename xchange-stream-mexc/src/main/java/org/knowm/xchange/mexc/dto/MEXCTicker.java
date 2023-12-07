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
   *   "d":
   *    {"s":"BTCUSDT",
   *     "p":"36474.74",
   *     "r":"0.0354",
   *     "tr":"0.0354",
   *     "h":"36549.72",
   *     "l":"35101.68",
   *     "v":"375173478.65",
   *     "q":"10557.72895",
   *     "lastRT":"-1",
   *     "MT":"0",
   *     "NV":"--",
   *     "t":"1699502456050"},
   *   "c":"spot@public.miniTicker.v3.api@BTCUSDT@UTC+8",
   *   "t":1699502456051,
   *   "s":"BTCUSDT"
   * }
   */

  /**
   * 交易类型
   */
  @JsonProperty("s")
  private String symbol;

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
}
