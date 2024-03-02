package org.knowm.xchange.gateio.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;
import org.knowm.xchange.gateio.dto.GateioEnums.ActionMode;
import org.knowm.xchange.gateio.dto.GateioEnums.FinishAs;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderSide;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderStatus;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderType;
import org.knowm.xchange.gateio.dto.GateioEnums.StpAct;
import org.knowm.xchange.gateio.dto.GateioEnums.TimeInForce;

/**
 * <p> @Date : 2024/2/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class GateioOrder {

  /**
   * 订单 ID
   */
  @JsonProperty("id")
  private String id;

  /**
   * 订单自定义信息
   */
  @JsonProperty("text")
  private String text;

  /**
   * 用户修改订单时备注的信息
   */
  @JsonProperty("amend_text")
  private String amendText;

  /**
   * 订单创建时间
   */
  @JsonProperty("create_time")
  private String createTime;

  /**
   * 订单最新修改时间
   */
  @JsonProperty("update_time")
  private String updateTime;

  /**
   * 订单创建时间，毫秒精度
   */
  @JsonProperty("create_time_ms")
  private long createTimeMs;

  /**
   * 订单最近修改时间，毫秒精度
   */
  @JsonProperty("update_time_ms")
  private long updateTimeMs;

  /**
   * 订单状态
   */
  @JsonProperty("status")
  private OrderStatus status;

  /**
   * 交易货币对
   */
  @JsonProperty("currency_pair")
  private String currencyPair;

  /**
   * 订单类型
   */
  @JsonProperty("type")
  private OrderType type;

  /**
   * 账户类型
   */
  @JsonProperty("account")
  private String account;

  /**
   * 买单或者卖单
   */
  @JsonProperty("side")
  private OrderSide side;

  /**
   * 交易数量
   */
  @JsonProperty("amount")
  private BigDecimal amount;

  /**
   * 交易价,type=limit时必填
   */
  @JsonProperty("price")
  private BigDecimal price;

  /**
   * Time in force 策略
   */
  @JsonProperty("time_in_force")
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = As.WRAPPER_OBJECT, property = "time_in_force")
  private TimeInForce timeInForce;

  /**
   * 冰山下单显示的数量
   */
  @JsonProperty("iceberg")
  private String iceberg;

  /**
   * 杠杆交易时，是否由系统自动借入不足部分
   */
  @JsonProperty("auto_borrow")
  private boolean autoBorrow;

  /**
   * 全仓杠杆下单是否开启自动还款
   */
  @JsonProperty("auto_repay")
  private boolean autoRepay;

  /**
   * 交易货币未成交数量
   */
  @JsonProperty("left")
  private BigDecimal left;

  /**
   * 已成交的计价币种总额
   */
  @JsonProperty("filled_total")
  private String filledTotal;

  /**
   * 平均成交价
   */
  @JsonProperty("avg_deal_price")
  private BigDecimal avgDealPrice;

  /**
   * 成交扣除的手续费
   */
  @JsonProperty("fee")
  private String fee;

  /**
   * 手续费计价单位
   */
  @JsonProperty("fee_currency")
  private String feeCurrency;

  /**
   * 手续费抵扣使用的点卡数量
   */
  @JsonProperty("point_fee")
  private String pointFee;

  /**
   * 手续费抵扣使用的 GT 数量
   */
  @JsonProperty("gt_fee")
  private String gtFee;

  /**
   * 手续费 maker 抵扣使用的 GT 数量
   */
  @JsonProperty("gt_maker_fee")
  private String gtMakerFee;

  /**
   * 手续费 taker 抵扣使用的 GT 数量
   */
  @JsonProperty("gt_taker_fee")
  private String gtTakerFee;

  /**
   * 是否开启 GT 抵扣
   */
  @JsonProperty("gt_discount")
  private boolean gtDiscount;

  /**
   * 返还的手续费
   */
  @JsonProperty("rebated_fee")
  private String rebatedFee;

  /**
   * 返还手续费计价单位
   */
  @JsonProperty("rebated_fee_currency")
  private String rebatedFeeCurrency;

  /**
   * 订单所属的 STP 用户组 id
   */
  @JsonProperty("stp_id")
  private int stpId;

  /**
   * Self-Trading Prevention Action
   */
  @JsonProperty("stp_act")
  private StpAct stpAct;

  /**
   * 订单结束方式
   */
  @JsonProperty("finish_as")
  private FinishAs finishAs;

  /**
   * 处理模式
   */
  @JsonProperty("action_mode")
  private ActionMode actionMode;


}
