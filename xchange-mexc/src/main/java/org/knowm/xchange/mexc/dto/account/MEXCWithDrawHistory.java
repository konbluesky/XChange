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
public class MEXCWithDrawHistory {

  /**
   * { "id": "bb17a2d452684f00a523c015d512a341", "txId": null, "coin": "EOS", "network": "EOS",
   * "address": "zzqqqqqqqqqq", "amount": "10", "transferType": 0, "status": 3, "transactionFee":
   * "0", "confirmNo": null, "applyTime": 1665300874000, "remark": "", "memo": "MX10086" }
   */

  private String id;
  private String txId;
  private String coin;
  private String network;
  private String address;
  private String amount;
  private String withdrawOrderId;
  private Integer transferType;
  private String status;
  private String transactionFee;
  private String confirmNo;
  private Long applyTime;
  private String remark;
  private String memo;

}
