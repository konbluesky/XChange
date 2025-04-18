package org.knowm.xchange.bybit.dto.marketdata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class BybitCoinInfoResponse {

  @JsonProperty("rows")
  private List<BybitCoinInfo> rows;

  public static class BybitCoinInfo {

    @JsonProperty("name")
    private String name;

    @JsonProperty("coin")
    private String coin;

    @JsonProperty("remainAmount")
    private String remainAmount;

    @JsonProperty("chains")
    private List<BybitCoinNetwork> chains;
  }

  public static class BybitCoinNetwork {

    @JsonProperty("chainType")
    private String chainType;

    @JsonProperty("confirmation")
    private String confirmation;

    @JsonProperty("withdrawFee")
    private String withdrawFee;

    @JsonProperty("depositMin")
    private String depositMin;

    @JsonProperty("withdrawMin")
    private String withdrawMin;

    @JsonProperty("chain")
    private String chain;

    @JsonProperty("chainDeposit")
    private String chainDeposit;

    @JsonProperty("chainWithdraw")
    private String chainWithdraw;

    @JsonProperty("minAccuracy")
    private int minAccuracy;

    @JsonProperty("withdrawPercentageFee")
    private Float withdrawPercentageFee;

    @JsonProperty("contractAddress")
    private String contractAddress;

  }

}
