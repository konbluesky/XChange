package org.knowm.xchange.hashkey.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.hashkey.HashKeyAdapters;
import org.knowm.xchange.hashkey.dto.HashKeyAccountInfo;
import org.knowm.xchange.service.account.AccountService;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyAccountService extends HashKeyAccountServiceRaw implements AccountService {

  public HashKeyAccountService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    HashKeyAccountInfo rawAccountInfo = super.getRawAccountInfo(null);
    Wallet wallet = HashKeyAdapters.adaptHashKeyBalances(rawAccountInfo);
    return new AccountInfo(wallet);
  }


}
