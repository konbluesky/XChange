package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Getter;
import lombok.ToString;

/**
 * 取消提现的结果实体
 */
@Getter
@ToString
public class GateioCancelWithdrawalResponse {

  @JsonProperty("id")
  private String Id;

  @JsonProperty("txid")
  private String transferHash;

  @JsonProperty("withdraw_order_id")
  private String withDrawOrderId;

  @JsonProperty("timestamp")
  private Date timestamp;

  @JsonProperty("amount")
  private String amount;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("address")
  private String address;

  @JsonProperty("memo")
  private String memo;

  @JsonProperty("status")
  private String statusStr;

  @JsonProperty("chain")
  private String chain;

  enum CancelStatusType {
    OK,FAIL,PROCESSING,UNKNOWN
  }

  /**
   * 交易状态
   * DONE: 完成
   * CANCEL: 已取消
   * REQUEST: 请求中
   * MANUAL: 待人工审核
   * BCODE: 充值码操作
   * EXTPEND: 已经发送等待确认
   * FAIL: 链上失败等待确认
   * INVALID: 无效订单
   * VERIFY: 验证中
   * PROCES: 处理中
   * PEND: 处理中
   * DMOVE: 待人工审核
   * SPLITPEND: cny提现大于5w,自动分单
   */
  public CancelStatusType getStatusType(){
    switch (statusStr){
      case "DONE":
        return CancelStatusType.OK;
      case "FAIL":
        return CancelStatusType.FAIL;
      case "PEND":
      case "PROCES":
      case "VERIFY":
      case "INVALID":
      case "EXTPEND":
      case "BCODE":
      case "MANUAL":
      case "REQUEST":
      case "CANCEL":
      case "DMOVE":
      case "SPLITPEND":
        return CancelStatusType.PROCESSING;
    }
    return CancelStatusType.UNKNOWN;
  }


}
