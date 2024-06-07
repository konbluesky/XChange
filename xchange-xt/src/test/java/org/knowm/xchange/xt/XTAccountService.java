package org.knowm.xchange.xt;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.dto.account.AccountInfo;

/**
 * <p> @Date : 2024/6/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTAccountService extends XTExchangeBase {

  @Test
  public void testBalance() throws IOException {
    AccountInfo accountInfo = exchange.getAccountService().getAccountInfo();
    log.info("{}",accountInfo);

  }

}
