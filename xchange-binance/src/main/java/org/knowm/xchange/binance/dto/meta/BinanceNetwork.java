package org.knowm.xchange.binance.dto.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/8/17 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class BinanceNetwork {

  public String addressRegex;
  public String coin;
  public String depositDesc;
  public boolean depositEnable;
  public boolean isDefault;
  public String memoRegex;
  public int minConfirm;
  public String name;
  public String network;
  public boolean resetAddressStatus;
  public String specialTips;
  public int unLockConfirm;
  public String withdrawDesc;
  public boolean withdrawEnable;
  public String withdrawFee;
  public String withdrawIntegerMultiple;
  public String withdrawMax;
  public String withdrawMin;
  public boolean sameAddress;
  public int estimatedArrivalTime;
  public boolean busy;

  public BinanceNetwork(
      @JsonProperty("addressRegex") String addressRegex,
      @JsonProperty("coin") String coin,
      @JsonProperty("depositDesc") String depositDesc,
      @JsonProperty("depositEnable") boolean depositEnable,
      @JsonProperty("isDefault") boolean isDefault,
      @JsonProperty("memoRegex") String memoRegex,
      @JsonProperty("minConfirm") int minConfirm,
      @JsonProperty("name") String name,
      @JsonProperty("network") String network,
      @JsonProperty("resetAddressStatus") boolean resetAddressStatus,
      @JsonProperty("specialTips") String specialTips,
      @JsonProperty("unLockConfirm") int unLockConfirm,
      @JsonProperty("withdrawDesc") String withdrawDesc,
      @JsonProperty("withdrawEnable") boolean withdrawEnable,
      @JsonProperty("withdrawFee") String withdrawFee,
      @JsonProperty("withdrawIntegerMultiple") String withdrawIntegerMultiple,
      @JsonProperty("withdrawMax") String withdrawMax,
      @JsonProperty("withdrawMin") String withdrawMin,
      @JsonProperty("sameAddress") boolean sameAddress,
      @JsonProperty("estimatedArrivalTime") int estimatedArrivalTime,
      @JsonProperty("busy") boolean busy) {
    this.addressRegex = addressRegex;
    this.coin = coin;
    this.depositDesc = depositDesc;
    this.depositEnable = depositEnable;
    this.isDefault = isDefault;
    this.memoRegex = memoRegex;
    this.minConfirm = minConfirm;
    this.name = name;
    this.network = network;
    this.resetAddressStatus = resetAddressStatus;
    this.specialTips = specialTips;
    this.unLockConfirm = unLockConfirm;
    this.withdrawDesc = withdrawDesc;
    this.withdrawEnable = withdrawEnable;
    this.withdrawFee = withdrawFee;
    this.withdrawIntegerMultiple = withdrawIntegerMultiple;
    this.withdrawMax = withdrawMax;
    this.withdrawMin = withdrawMin;
    this.sameAddress = sameAddress;
    this.estimatedArrivalTime = estimatedArrivalTime;
    this.busy = busy;
  }

}