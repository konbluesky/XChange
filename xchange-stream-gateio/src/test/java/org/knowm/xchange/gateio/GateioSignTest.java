package org.knowm.xchange.gateio;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.gateio.service.GateioHmacPostBodyDigest;

/**
 * <p> @Date : 2024/2/21 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioSignTest extends GateioBase{

  @Test
  public void testSign() {

    GateioHmacPostBodyDigest digest = GateioHmacPostBodyDigest.createInstance(apikey, apiSecret);

    String s = digest.socketSign("spot.orders", "subscribe", new Date().getTime());

    log.info("socket sign:{}", s);


  }


}
