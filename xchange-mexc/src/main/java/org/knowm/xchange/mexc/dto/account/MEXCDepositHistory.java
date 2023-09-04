package org.knowm.xchange.mexc.dto.account;

import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXCDepositHistory {

  /**
   * { "amount": "50000", "coin": "EOS", "network": "EOS", "status": 5, "address":
   * "0x20b7cf77db93d6ef1ab979c49142ec168427fdee", "txId":
   * "01391d1c1397ef0a3cbb3c7f99a90846f7c8c2a8dddcdcf84f46b530dede203e1bc804", "insertTime":
   * 1659513342000, "unlockConfirm": "10", "confirmTimes": "241", "memo": "xxyy1122" }
   */
  /**
   * 数量
   */
  private String amount;
  /**
   * 币种
   */
  private String coin;
  /**
   * 链类型
   */
  private String network;
  /**
   * 充值状态，1:小额充值，2:延遲到賬，3:大額充值，4:等待中，5:入账成功，6:审核中，7:驳回
   */
  private String status;
  /**
   * 地址
   */
  private String address;
  /**
   * 交易；编号
   */
  private String txId;
  private Long insertTime;
  private String unlockConfirm;
  private String confirmTimes;
  private String memo;


}
