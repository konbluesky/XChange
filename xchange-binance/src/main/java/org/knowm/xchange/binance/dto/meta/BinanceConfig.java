package org.knowm.xchange.binance.dto.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/8/17 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */

@Getter
@ToString
public class BinanceConfig {

  private String coin;
  private boolean depositAllEnable;
  private String free;
  private String freeze;
  private String ipoable;
  private String ipoing;
  private boolean isLegalMoney;
  private String locked;
  private String name;
  private List<BinanceNetwork> networkList;

  public BinanceConfig(
      @JsonProperty("coin") String coin,
      @JsonProperty("depositAllEnable") boolean depositAllEnable,
      @JsonProperty("free") String free,
      @JsonProperty("freeze") String freeze,
      @JsonProperty("ipoable") String ipoable,
      @JsonProperty("ipoing") String ipoing,
      @JsonProperty("isLegalMoney") boolean isLegalMoney,
      @JsonProperty("locked") String locked,
      @JsonProperty("name") String name,
      @JsonProperty("networkList") List<BinanceNetwork> networkList) {
    this.coin = coin;
    this.depositAllEnable = depositAllEnable;
    this.free = free;
    this.freeze = freeze;
    this.ipoable = ipoable;
    this.ipoing = ipoing;
    this.isLegalMoney = isLegalMoney;
    this.locked = locked;
    this.name = name;
    this.networkList = networkList;
  }
}
