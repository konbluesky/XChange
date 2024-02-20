package org.knowm.xchange.gateio.dto.marketdata;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;

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

  public GateioCurrencyMetaData(Integer scale, BigDecimal withdrawalFee, String chain) {
    super(scale, withdrawalFee);
    this.chain = chain;
  }

  public GateioCurrencyMetaData(Integer scale, BigDecimal withdrawalFee,
      BigDecimal minWithdrawalAmount, String chain) {
    super(scale, withdrawalFee, minWithdrawalAmount);
    this.chain = chain;
  }

  public GateioCurrencyMetaData(Integer scale, BigDecimal withdrawalFee,
      BigDecimal minWithdrawalAmount, WalletHealth walletHealth,
      String chain) {
    super(scale, withdrawalFee, minWithdrawalAmount, walletHealth);
    this.chain = chain;
  }
}
