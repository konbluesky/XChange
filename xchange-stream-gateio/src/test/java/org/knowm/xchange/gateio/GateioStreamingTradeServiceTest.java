package org.knowm.xchange.gateio;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioStreamingTradeServiceTest extends GateioBase {

  @Test
  public void testGetOrderChanges() throws InterruptedException {
    streamingExchange.getStreamingTradeService().getOrderChanges(new CurrencyPair("GT","USDT"))
        .subscribe(order->{
          log.info("subscribe order:{}", order);
        });
    TimeUnit.MINUTES.sleep(10);
  }

}