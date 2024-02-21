package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2024/2/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class GateioUnifiedAccount {

  /**
   * 总资产
   */
  @JsonProperty("total")
  private String total;

  /**
   * 已借贷资产
   */
  @JsonProperty("borrowed")
  private String borrowed;

  /**
   * 总初始保证金
   */
  @JsonProperty("total_initial_margin")
  private String totalInitialMargin;

  /**
   * 总保证金余额
   */
  @JsonProperty("total_margin_balance")
  private String totalMarginBalance;

  /**
   * 总维持保证金
   */
  @JsonProperty("total_maintenance_margin")
  private String totalMaintenanceMargin;

  /**
   * 总初始保证金率
   */
  @JsonProperty("total_initial_margin_rate")
  private String totalInitialMarginRate;

  /**
   * 总维持保证金率
   */
  @JsonProperty("total_maintenance_margin_rate")
  private String totalMaintenanceMarginRate;

  /**
   * 总可用保证金
   */
  @JsonProperty("total_available_margin")
  private String totalAvailableMargin;

  /**
   * 统一账户总额
   */
  @JsonProperty("unified_account_total")
  private String unifiedAccountTotal;

  /**
   * 统一账户总负债
   */
  @JsonProperty("unified_account_total_liab")
  private String unifiedAccountTotalLiability;

  /**
   * 统一账户总权益
   */
  @JsonProperty("unified_account_total_equity")
  private String unifiedAccountTotalEquity;

  /**
   * 杠杆倍数
   */
  @JsonProperty("leverage")
  private String leverage;

  /**
   * 用户ID
   */
  @JsonProperty("user_id")
  private int userId;

  /**
   * 是否锁定
   */
  @JsonProperty("locked")
  private boolean locked;

  @JsonProperty("balances")
  private Map<String,GateioBalanceItem> balances;

}
