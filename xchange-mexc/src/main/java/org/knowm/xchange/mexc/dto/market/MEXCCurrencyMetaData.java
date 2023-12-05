package org.knowm.xchange.mexc.dto.market;

import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;

/**
 * <p> @Date : 2023/12/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Data
public class MEXCCurrencyMetaData extends CurrencyMetaData {

  private boolean canWithdraw;
  private boolean canDeposit;
  private String network;


  public MEXCCurrencyMetaData(Integer scale, BigDecimal withdrawalFee) {
    super(scale, withdrawalFee);
  }

  public MEXCCurrencyMetaData(Integer scale, BigDecimal withdrawalFee,
      BigDecimal minWithdrawalAmount) {
    super(scale, withdrawalFee, minWithdrawalAmount);
  }

  public MEXCCurrencyMetaData(Integer scale, BigDecimal withdrawalFee,
      BigDecimal minWithdrawalAmount,
      WalletHealth walletHealth) {
    super(scale, withdrawalFee, minWithdrawalAmount, walletHealth);
  }
}
