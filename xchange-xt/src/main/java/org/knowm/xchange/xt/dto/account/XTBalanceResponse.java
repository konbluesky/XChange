package org.knowm.xchange.xt.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
public class XTBalanceResponse {

  /**
   * "currency": "usdt",     //币种 "currencyId": 0,        //币种ID "frozenAmount": 0,      //冻结数量
   * "availableAmount": 0,   //可用数量 "totalAmount": 0,       //总数量 "convertBtcAmount": 0   //折算BTC数量
   */

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("currencyId")
  private String currencyId;

  @JsonProperty("frozenAmount")
  private String frozenAmount;

  @JsonProperty("freeze")
  private String freeze;

  @JsonProperty("lock")
  private String lock;

  @JsonProperty("trade")
  private String trade;

  @JsonProperty("copyTrade")
  private String copyTrade;

  @JsonProperty("withdraw")
  private String withdraw;

  @JsonProperty("availableAmount")
  private String availableAmount;

  @JsonProperty("totalAmount")
  private String totalAmount;

  @JsonProperty("convertBtcAmount")
  private String convertBtcAmount;

  @JsonProperty("convertUsdtAmount")
  private String convertUsdtAmount;


}
