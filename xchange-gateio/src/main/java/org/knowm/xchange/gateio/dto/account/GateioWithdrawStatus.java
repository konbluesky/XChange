package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2024/2/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class GateioWithdrawStatus {

  /**
   * 币种 示例: "GT"
   */
  @JsonProperty("currency")
  private String currency;

  /**
   * 币种名称 示例: "GateToken"
   */
  @JsonProperty("name")
  private String name;

  /**
   * 币种中文名称 示例: "GateToken"
   */
  @JsonProperty("name_cn")
  private String nameCn;

  /**
   * 充值手续费 示例: "0"
   */
  @JsonProperty("deposit")
  private String deposit;

  /**
   * 提现手续费率百分比 示例: "0%"
   */
  @JsonProperty("withdraw_percent")
  private String withdrawPercent;

  /**
   * 固定提现手续费用 示例: "0.01"
   */
  @JsonProperty("withdraw_fix")
  private String withdrawFix;

  /**
   * 日提现额度 示例: "20000"
   */
  @JsonProperty("withdraw_day_limit")
  private String withdrawDayLimit;

  /**
   * 最少提现额度 示例: "0.11"
   */
  @JsonProperty("withdraw_amount_mini")
  private String withdrawAmountMini;

  /**
   * 剩余日提现额度 示例: "20000"
   */
  @JsonProperty("withdraw_day_limit_remain")
  private String withdrawDayLimitRemain;

  /**
   * 单次最多提现额度 示例: "20000"
   */
  @JsonProperty("withdraw_eachtime_limit")
  private String withdrawEachTimeLimit;

  /**
   * 多链的固定提现手续费用 示例: {"BTC": "20", "ETH": "15", "TRX": "0", "EOS": "2.5"}
   */
  @JsonProperty("withdraw_fix_on_chains")
  private Map<String, String> withdrawFixOnChains;

  /**
   * 多链的百分比提现手续费用 示例: {"ETH": "0%", "GTEVM": "0%"}
   */
  @JsonProperty("withdraw_percent_on_chains")
  private Map<String, String> withdrawPercentOnChains;


}
