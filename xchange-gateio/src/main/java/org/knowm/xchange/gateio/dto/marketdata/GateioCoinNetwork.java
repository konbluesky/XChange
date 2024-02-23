package org.knowm.xchange.gateio.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class GateioCoinNetwork {

  /**
   * 链名称
   */
  @JsonProperty("chain")
  private String chain;

  /**
   * 链的中文名称
   */
  @JsonProperty("name_cn")
  private String nameCn;

  /**
   * 链的英文名称
   */
  @JsonProperty("name_en")
  private String nameEn;

  /**
   * 币种智能合约地址，如果没有地址则为空字串
   */
  @JsonProperty("contract_address")
  private String contractAddress;

  /**
   * 是否禁用，0 表示未禁用
   */
  @JsonProperty("is_disabled")
  private int isDisabled;

  /**
   * 充值是否禁用，0 表示未禁用
   */
  @JsonProperty("is_deposit_disabled")
  private int isDepositDisabled;

  /**
   * 提现是否禁用，0 表示未禁用
   */
  @JsonProperty("is_withdraw_disabled")
  private int isWithdrawDisabled;

  /**
   * 提币精度
   */
  @JsonProperty("decimal")
  private String decimal;

}
