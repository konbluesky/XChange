package org.knowm.xchange.hashkey.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.hashkey.dto.HashKeyAccountInfo;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyAccountServiceRaw extends HashKeyBaseService {

  public HashKeyAccountServiceRaw(Exchange exchange) {
    super(exchange);
  }


  public HashKeyAccountInfo getRawAccountInfo(String accountId)
      throws IOException {
    return hashKeyAuthenticated.getAccountInformation(apiKey, nonceFactory, signatureCreator,
        accountId);
  }

}
