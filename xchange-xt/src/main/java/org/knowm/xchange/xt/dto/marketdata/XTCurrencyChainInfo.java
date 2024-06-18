package org.knowm.xchange.xt.dto.marketdata;

import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class XTCurrencyChainInfo {

  /**
   * "chain": "Bitcon",          //支持的转账网络 "depositEnabled": true,     //是否支持充值，true:支持，false:不支持
   * "withdrawEnabled": true,    //是否支持提现，true:支持，false:不支持 "withdrawFeeAmount": 0.2,   //提现手续费
   * "withdrawMinAmount": 10,    //最小提现数量 "depositFeeRate": 0.2       //充值费率，百分比
   */
  private String chain;
  private boolean depositEnabled;
  private boolean withdrawEnabled;
  private double withdrawFeeAmount;
  private double withdrawMinAmount;
  private double depositFeeRate;

}
