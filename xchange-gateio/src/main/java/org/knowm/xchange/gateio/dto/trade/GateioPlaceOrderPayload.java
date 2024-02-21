package org.knowm.xchange.gateio.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.knowm.xchange.gateio.dto.GateioEnums.ActionMode;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderSide;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderType;
import org.knowm.xchange.gateio.dto.GateioEnums.StpAct;
import org.knowm.xchange.gateio.dto.GateioEnums.TimeInForce;

/**
 * <p> @Date : 2024/2/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
@ToString
public class GateioPlaceOrderPayload {
  /**
   * 订单自定义信息，用户可以用该字段设置自定义 ID，用户自定义字段必须满足以下条件：
   * - 必须以 t- 开头
   * - 不计算 t- ，长度不能超过 28 字节
   * - 输入内容只能包含数字、字母、下划线(_)、中划线(-) 或者点(.)
   * - 除用户自定义信息以外，以下为内部保留字段，标识订单来源:
   *   101 代表 android 下单
   *   102 代表 IOS 下单
   *   103 代表 IPAD 下单
   *   104 代表 webapp 下单
   *   3 代表 web 下单
   *   2 代表 apiv2 下单
   *   apiv4 代表 apiv4 下单
   */
  @JsonProperty("text")
  private String text;

  /** 交易货币对 */
  @JsonProperty("currency_pair")
  private String currencyPair;

  /**
   * 订单类型:
   * - limit : 限价单
   * - market : 市价单
   */
  @JsonProperty("type")
  private OrderType type;

  /** 买单或者卖单 */
  @JsonProperty("side")
  private OrderSide side;

  /**
   * 账户类型，spot - 现货账户，margin - 杠杆账户，cross_margin - 全仓杠杆账户，unified - 统一账户
   * 统一账户（旧）只能设置 cross_margin
   */
  @JsonProperty("account")
  private String account;

  /** 交易价，type=limit时必填 */
  @JsonProperty("price")
  private String price;

  /**
   * 交易数量:
   * - type为limit时，指交易货币，即需要交易的货币，如BTC_USDT中指BTC。
   * - type为market时，根据买卖不同指代不同:
   *   - side : buy 指代计价货币，BTC_USDT中指USDT
   *   - side : sell 指代交易货币，BTC_USDT中指BTC
   */
  @JsonProperty("amount")
  private String amount;

  /** 冰山下单显示的数量，不指定或传 0 都默认为普通下单。目前不支持全部冰山。 */
  @JsonProperty("iceberg")
  private String iceberg;

  /**
   * Time in force 策略:
   * - gtc: GoodTillCancelled
   * - ioc: ImmediateOrCancelled，立即成交或者取消，只吃单不挂单
   * - poc: PendingOrCancelled，被动委托，只挂单不吃单
   * - fok: FillOrKill，全部成交或者全部取消
   * - type=market时仅支持ioc和fok
   */
  @JsonProperty("time_in_force")
  private TimeInForce timeInForce;


  /** 杠杆(包括逐仓全仓)交易时，如果账户余额不足，是否由系统自动借入不足部分 */
  @JsonProperty("auto_borrow")
  private Boolean autoBorrow;


  /**
   * 全仓杠杆下单是否开启自动还款，默认关闭。需要注意的是:
   * - 此字段仅针对全仓杠杆有效。逐仓杠杆不支持订单级别的自动还款设置，只能通过 POST /margin/auto_repay 修改用户级别的设置
   * - auto_borrow 与 auto_repay 不支持同时开启
   */
  @JsonProperty("auto_repay")
  private Boolean autoRepay;

  /**
   * Self-Trading Prevention Action,用户可以用该字段设置自定义限制自成交策略。
   * - 用户在设置加入STP用户组后，可以通过传递 stp_act 来限制用户发生自成交的策略，没有传递 stp_act 默认按照 cn 的策略。
   * - 用户在没有设置加入STP用户组时，传递 stp_act 参数会报错。
   * - 用户没有使用 stp_act 发生成交的订单，stp_act 返回 -。
   * - cn: Cancel newest,取消新订单，保留老订单
   * - co: Cancel oldest,取消⽼订单，保留新订单
   * - cb: Cancel both,新旧订单都取消
   */
  @JsonProperty("stp_act")
  private StpAct stpAct;

  /**
   * 处理模式: 下单时根据action_mode返回不同的字段, 该字段只在请求时有效，响应结果中不包含该字段
   * - ACK: 异步模式，只返回订单关键字段
   * - RESULT: 无清算信息
   * - FULL: 完整模式（默认）
   */
  @JsonProperty("action_mode")
  private ActionMode actionMode;


}
