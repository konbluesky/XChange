package org.knowm.xchange.binance.dto.account;

import java.math.BigDecimal;
import lombok.Data;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;

/**
 * <p> @Date : 2023/8/23 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
public class BinanceWithdrawFundsParams extends DefaultWithdrawFundsParams {

  private String network;

  public BinanceWithdrawFundsParams(String address, Currency currency, BigDecimal amount,
      String network) {
    super(address, currency, amount);
    this.network = network;
  }

  public BinanceWithdrawFundsParams(AddressWithTag address, Currency currency, BigDecimal amount,
      String network) {
    super(address, currency, amount);
    this.network = network;
  }

  public BinanceWithdrawFundsParams(String address, Currency currency, BigDecimal amount,
      BigDecimal commission, String network) {
    super(address, currency, amount, commission);
    this.network = network;
  }

  public BinanceWithdrawFundsParams(AddressWithTag address, Currency currency, BigDecimal amount,
      BigDecimal commission, String network) {
    super(address, currency, amount, commission);
    this.network = network;
  }

  public BinanceWithdrawFundsParams(String address, String addressTag, Currency currency,
      BigDecimal amount, BigDecimal commission, String network) {
    super(address, addressTag, currency, amount, commission);
    this.network = network;
  }
}
