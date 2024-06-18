package org.knowm.xchange.xt.dto.account;

import lombok.Builder;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> author konbluesky </p>
 */
@Builder
@Getter
public class WithdrawRequest {

  /**
   * 币种名称，可从'获取XT可充提的币种'接口中获取
   */
  private String currency;

  /**
   * 转账网络名称，可从'获取XT可充提的币种'接口中获取
   */
  private String chain;

  /**
   * 提现金额，包含手续费部分
   */
  private String amount;

  /**
   * 提现地址
   */
  private String address;

  /**
   * memo，对于EOS类似的需要memo的链必传
   */
  private String memo;
}
