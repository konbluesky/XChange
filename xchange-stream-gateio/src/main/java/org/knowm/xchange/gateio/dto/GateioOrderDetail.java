package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class GateioOrderDetail {

  @JsonProperty("id")
  private String id;

  @JsonProperty("user")
  private Integer user;

  @JsonProperty("text")
  private String text;

  @JsonProperty("create_time")
  private String createTime;

  @JsonProperty("create_time_ms")
  private String createTimeMs;

  @JsonProperty("update_time")
  private String updateTime;

  @JsonProperty("update_time_ms")
  private String updateTimeMs;

  @JsonProperty("event")
  private String event;

  @JsonProperty("currency_pair")
  private String currencyPair;

  @JsonProperty("type")
  private String type;

  @JsonProperty("account")
  private String account;

  @JsonProperty("side")
  private String side;

  @JsonProperty("amount")
  private String amount;

  @JsonProperty("price")
  private String price;

  @JsonProperty("time_in_force")
  private String timeInForce;

  @JsonProperty("left")
  private String left;

  @JsonProperty("filled_total")
  private String filledTotal;

  @JsonProperty("avg_deal_price")
  private String avgDealPrice;

  @JsonProperty("fee")
  private String fee;

  @JsonProperty("fee_currency")
  private String feeCurrency;

  @JsonProperty("point_fee")
  private String pointFee;

  @JsonProperty("gt_fee")
  private String gtFee;

  @JsonProperty("gt_discount")
  private Boolean gtDiscount;

  @JsonProperty("rebated_fee")
  private String rebatedFee;

  @JsonProperty("rebated_fee_currency")
  private String rebatedFeeCurrency;

  @JsonProperty("auto_repay")
  private Boolean autoRepay;

  @JsonProperty("auto_borrow")
  private Boolean autoBorrow;

  @JsonProperty("stp_id")
  private Integer stpId;

  @JsonProperty("stp_act")
  private String stpAct;

  @JsonProperty("finish_as")
  private String finishAs;

  @JsonProperty("amend_text")
  private String amendText;

  @JsonProperty("biz_info")
  private String bizInfo;

}
