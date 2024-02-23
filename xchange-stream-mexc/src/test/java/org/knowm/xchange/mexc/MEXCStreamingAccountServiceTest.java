package org.knowm.xchange.mexc;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p> @Date : 2024/2/23 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCStreamingAccountServiceTest extends MEXCBase {

  @Test
  public void testGetBalanceChanges() throws InterruptedException {
    streamingExchange.getStreamingAccountService().getBalanceChanges(null).subscribe(balance -> {
      log.info("balance:{}", balance);
    });
    TimeUnit.MINUTES.sleep(10);
  }

}