package org.knowm.xchange.gateio.params;

import java.math.BigDecimal;
import lombok.Getter;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;

/**
 * <p> @Date : 2024/2/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class GateioDefaultWithdrawFundsParams extends DefaultWithdrawFundsParams {

  private String chain;
  private String memo;

  public GateioDefaultWithdrawFundsParams(String address, Currency currency, BigDecimal amount,
      String chain, String memo) {
    super(address, currency, amount);
    this.chain = chain;
    this.memo = memo;
  }

  public GateioDefaultWithdrawFundsParams(AddressWithTag address, Currency currency,
      BigDecimal amount, String chain, String memo) {
    super(address, currency, amount);
    this.chain = chain;
    this.memo = memo;
  }

  public GateioDefaultWithdrawFundsParams(String address, Currency currency, BigDecimal amount,
      BigDecimal commission, String chain, String memo) {
    super(address, currency, amount, commission);
    this.chain = chain;
    this.memo = memo;
  }

  public GateioDefaultWithdrawFundsParams(AddressWithTag address, Currency currency,
      BigDecimal amount, BigDecimal commission, String chain, String memo) {
    super(address, currency, amount, commission);
    this.chain = chain;
    this.memo = memo;
  }

  public GateioDefaultWithdrawFundsParams(String address, String addressTag, Currency currency,
      BigDecimal amount, BigDecimal commission, String chain, String memo) {
    super(address, addressTag, currency, amount, commission);
    this.chain = chain;
    this.memo = memo;
  }
}
