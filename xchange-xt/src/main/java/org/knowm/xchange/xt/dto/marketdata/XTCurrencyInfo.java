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
public class XTCurrencyInfo {

  /**
   * "id": 11,            //币种id "currency": "usdt",   //币种名称 "fullName": "usdt",   //币种全称 "logo":
   * null,         //币种logo "cmcLink": null,      //cmc链接 "weight": 100,        //权重 "maxPrecision":
   * 6,    //精度 "depositStatus": 1,   //充值状态(0关闭 1开放) "withdrawStatus": 1,  //提现状态(0关闭 1开放)
   * "convertEnabled": 1,  //小额资产兑换开关[0=关;1=开] "transferEnabled": 1  //划转开关[0=关;1=开]
   */

  private String id;
  private String currency;
  private String fullName;
  private String displayName;
  private String type;
  private String logo;
  private String cmcLink;
  private String weight;
  private String nominalValue;
  private String maxPrecision;
  private String depositStatus;
  private String withdrawStatus;
  private String convertEnabled;
  private String transferEnabled;
  private String isChainExist;
  private String[] plates;


}
