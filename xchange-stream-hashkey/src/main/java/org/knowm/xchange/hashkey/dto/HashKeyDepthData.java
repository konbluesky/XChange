package org.knowm.xchange.hashkey.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/11/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class HashKeyDepthData {

  /** 交易对 */
  private final String symbol;

  /** 时间 */
  private final long time;

  /** 基础资产交易量 */
  private final String baseAssetVolume;

  /** 买单列表 */
  private final List<HashKeyDepthItem> bids;

  /** 卖单列表 */
  private final List<HashKeyDepthItem> asks;

  /** 是否为首次返回 */
  private final boolean isFirstReturn;

  /** 发送时间 */
  private final long sendTime;

  /** 是否共享 */
  private final boolean shared;

  /** ID */
  private final String id;

  @JsonCreator
  public HashKeyDepthData(
      @JsonProperty("s") String symbol,
      @JsonProperty("t") long time,
      @JsonProperty("v") String baseAssetVolume,
      @JsonProperty("b") List<HashKeyDepthItem> bids,
      @JsonProperty("a") List<HashKeyDepthItem> asks,
      @JsonProperty("f") boolean isFirstReturn,
      @JsonProperty("sendTime") long sendTime,
      @JsonProperty("shared") boolean shared,
      @JsonProperty("id") String id) {
    this.symbol = symbol;
    this.time = time;
    this.baseAssetVolume = baseAssetVolume;
    this.bids = bids;
    this.asks = asks;
    this.isFirstReturn = isFirstReturn;
    this.sendTime = sendTime;
    this.shared = shared;
    this.id = id;
  }

}
