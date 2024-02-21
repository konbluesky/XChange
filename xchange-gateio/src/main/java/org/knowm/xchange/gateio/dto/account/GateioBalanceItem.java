package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
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
public class GateioBalanceItem {
  /**
   * 可用资产
   */
  @JsonProperty("available")
  private BigDecimal available;

  /**
   * 冻结资产
   */
  @JsonProperty("freeze")
  private BigDecimal freeze;

  /**
   * 已借贷资产
   */
  @JsonProperty("borrowed")
  private String borrowed;

  /**
   * 负债资产
   */
  @JsonProperty("negative_liab")
  private String negativeLiability;

  /**
   * 期货持仓负债
   */
  @JsonProperty("futures_pos_liab")
  private String futuresPositionLiability;

  /**
   * 账户净值
   */
  @JsonProperty("equity")
  private String equity;

  /**
   * 总冻结资产
   */
  @JsonProperty("total_freeze")
  private String totalFreeze;

  /**
   * 总负债
   */
  @JsonProperty("total_liab")
  private String totalLiability;
}
