package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GateioDepositResponse {

  /**
   * 交易记录 ID
   */
  @JsonProperty("id")
  private String id;

  /**
   * 区块转账哈希记录
   */
  @JsonProperty("txid")
  private String transferHash;

  /**
   * 用户端订单编号，最长32个，输入内容只能包含数字、字母、下划线(_)、中划线(-) 或者点(.)
   */
  @JsonProperty("withdraw_order_id")
  private String withdrawOrderId;

  /**
   * 操作时间
   */
  @JsonProperty("timestamp")
  private String timestamp;

  /**
   * 币的数量
   */
  @JsonProperty("amount")
  private BigDecimal amount;

  /**
   * 币种名称
   */
  @JsonProperty("currency")
  private String currency;

  /**
   * 提现地址。提现操作必填
   */
  @JsonProperty("address")
  private String address;

  /**
   * 转账 memo 等备注信息
   */
  @JsonProperty("memo")
  private String memo;

  /**
   * 交易状态
   *
   * - DONE: 完成
   * - CANCEL: 已取消
   * - REQUEST: 请求中
   * - MANUAL: 待人工审核
   * - BCODE: 充值码操作
   * - EXTPEND: 已经发送等待确认
   * - FAIL: 链上失败等待确认
   * - INVALID: 无效订单
   * - VERIFY: 验证中
   * - PROCES: 处理中
   * - PEND: 处理中
   * - DMOVE: 待人工审核
   * - SPLITPEND: cny提现大于5w,自动分单
   */
  @JsonProperty("status")
  private String status;

  /**
   * 提现的链名称
   */
  @JsonProperty("chain")
  private String chain;

}
