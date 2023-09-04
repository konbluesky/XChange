package org.knowm.xchange.mexc.dto;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCSendMessageTest extends TestCase {

  @Test
  public void testString() {
    MEXCSendMessage sendMessage = MEXCSendMessage.createSubMessage();

    log.info("sub message: {}", sendMessage);

    sendMessage.putParam("spot@public.limit.depth.v3.api","BTCUDST",5);
    sendMessage.putParam("spot@public.limit.depth.v3.api","ETHUDST",20);

    log.info("sub message: {}", sendMessage);


  }

}