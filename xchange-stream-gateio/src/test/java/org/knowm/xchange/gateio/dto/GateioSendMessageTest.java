package org.knowm.xchange.gateio.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.gateio.GateioBase;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioSendMessageTest extends GateioBase {

  @Test
  public void test(){
    GateioSendMessage gateioSendMessage = GateioSendMessage.createPingMessage();
    gateioSendMessage.setChannel("hehe");
    log.info("{}",gateioSendMessage);
    gateioSendMessage.putParam("ijiji","sdfasd","dfasdfa");
    log.info("{}",gateioSendMessage);
    log.info("{}",gateioSendMessage.getChannelName());
  }

}