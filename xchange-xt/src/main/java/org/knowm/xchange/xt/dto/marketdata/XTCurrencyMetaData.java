package org.knowm.xchange.xt.dto.marketdata;

import java.math.BigDecimal;
import lombok.ToString;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;

/**
 * <p> @Date : 2024/7/1 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
public class XTCurrencyMetaData extends CurrencyMetaData {

  private XTSymbol symbolConfig;

  public XTCurrencyMetaData(Integer scale, BigDecimal withdrawalFee) {
    super(scale, withdrawalFee);
  }

  public XTCurrencyMetaData(Integer scale, BigDecimal withdrawalFee, XTSymbol symbolConfig) {
    super(scale, withdrawalFee);
    this.symbolConfig = symbolConfig;
  }

  public XTCurrencyMetaData(Integer scale, BigDecimal withdrawalFee, BigDecimal minWithdrawalAmount,
      XTSymbol symbolConfig) {
    super(scale, withdrawalFee, minWithdrawalAmount);
    this.symbolConfig = symbolConfig;
  }

  public XTCurrencyMetaData(Integer scale, BigDecimal withdrawalFee, BigDecimal minWithdrawalAmount,
      WalletHealth walletHealth, XTSymbol symbolConfig) {
    super(scale, withdrawalFee, minWithdrawalAmount, walletHealth);
    this.symbolConfig = symbolConfig;
  }
}
