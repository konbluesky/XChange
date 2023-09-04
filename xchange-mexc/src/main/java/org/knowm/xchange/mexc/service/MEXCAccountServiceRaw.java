package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.mexc.dto.account.MEXCAccount;
import org.knowm.xchange.mexc.dto.account.MEXCDepositHistory;
import org.knowm.xchange.mexc.dto.account.MEXCWithDrawHistory;
import org.knowm.xchange.mexc.dto.account.MEXCWithdrawApply;

public class MEXCAccountServiceRaw extends MEXCBaseService {

  public MEXCAccountServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public MEXCAccount getWalletBalances() throws IOException {
    return mexcAuthenticated.getWalletBalances(apiKey, nonceFactory, signatureCreator);
  }

  public String withdraw(MEXCWithdrawApply apply) throws IOException {

    return mexcAuthenticated.withdraw(
        apiKey,
        nonceFactory,
        signatureCreator,
        apply.getCoin(),
        apply.getWithdrawOrderId(),
        apply.getNetwork(),
        apply.getAddress(),
        apply.getMemo(),
        apply.getAmount(),
        apply.getRemark()
    );
  }


  public List<MEXCDepositHistory> getDepositHistory(
      String coinId,
      String status,
      String startTime,
      String endTime
  ) throws IOException {
    return mexcAuthenticated.getDepositHistory(
        apiKey,
        nonceFactory,
        signatureCreator,
        coinId,
        status,
        startTime,
        endTime
    );
  }


  public List<MEXCWithDrawHistory> getWithDrawHistory(
      String coinId,
      String status,
      String startTime,
      String endTime
  ) throws IOException {
    return mexcAuthenticated.getWithDrawHistory(
        apiKey,
        nonceFactory,
        signatureCreator,
        coinId,
        status,
        startTime,
        endTime
    );
  }


}
