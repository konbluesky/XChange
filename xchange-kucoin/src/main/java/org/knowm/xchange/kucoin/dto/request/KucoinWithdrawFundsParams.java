package org.knowm.xchange.kucoin.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;


@Getter
public class KucoinWithdrawFundsParams extends DefaultWithdrawFundsParams {

  private String chain;

  public KucoinWithdrawFundsParams(String address, Currency currency, BigDecimal amount,
      String chain) {
    super(address, currency, amount);
    this.chain = chain;
  }

}


