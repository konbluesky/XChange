package org.knowm.xchange.hashkey;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.hashkey.dto.HashKeyAccountInfo;
import org.knowm.xchange.hashkey.dto.HashKeyBalance;
import org.knowm.xchange.hashkey.service.HashKeyAccountServiceRaw;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyAccountServiceTest extends HashKeyExchangeTest {

  @Test
  public void testGetAccountInfo() throws IOException {
    Exchange rawExchange = createRawExchange();
    HashKeyAccountInfo rawAccountInfo = ((HashKeyAccountServiceRaw) rawExchange.getAccountService()).getRawAccountInfo(
        null);

    log.info("user id:{}", rawAccountInfo.getUserId());
    log.info("balances :{}", rawAccountInfo.getBalances().size());
    for (HashKeyBalance balance : rawAccountInfo.getBalances()) {
      log.info("balance:{}", balance.toString());
    }
  }

  @Test
  public void testGetAccountInfo2() throws IOException {
    Exchange rawExchange = createRawExchange();
    AccountInfo accountInfo = rawExchange.getAccountService().getAccountInfo();
    log.info("accountInfo:{}", accountInfo.toString());
    Wallet wallet = accountInfo.getWallet();
    wallet.getBalances().forEach((k, v) -> {
      log.info("currency:{},balance:{}", k, v.toString());
    });
  }


}
