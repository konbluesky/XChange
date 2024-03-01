package org.knowm.xchange.gateio.dto.marketdata;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawStatus;

/**
 * <p> @Date : 2024/2/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class GateioCurrencyMetaData extends CurrencyMetaData {

  private String chain;

  private GateioWithdrawStatus withdrawStatus;

  public GateioCurrencyMetaData(Integer scale, BigDecimal withdrawalFee,
      BigDecimal minWithdrawalAmount, WalletHealth walletHealth,
      String chain, GateioWithdrawStatus withdrawStatus) {
    super(scale, withdrawalFee, minWithdrawalAmount, walletHealth);
    this.chain = chain;
    this.withdrawStatus = withdrawStatus;
  }
}
