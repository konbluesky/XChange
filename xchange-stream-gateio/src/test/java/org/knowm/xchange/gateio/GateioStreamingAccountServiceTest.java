package org.knowm.xchange.gateio;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioStreamingAccountServiceTest extends GateioBase {

  @Test
  public void testBalanceChange() throws InterruptedException {
    streamingExchange.getStreamingAccountService().getBalanceChanges(Currency.USDT)
        .subscribe(balance -> {
          log.info("余额变化:{}", balance);
        });
    TimeUnit.MINUTES.sleep(10);
  }


}