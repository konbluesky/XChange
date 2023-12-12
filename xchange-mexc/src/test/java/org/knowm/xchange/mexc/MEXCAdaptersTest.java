package org.knowm.xchange.mexc;


import com.google.common.collect.Sets;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p> @Date : 2023/12/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCAdaptersTest {

  @Test
  public void testSets() {
    Set<String> left= Sets.newHashSet("one","true","two");
    Set<String> right = Sets.newHashSet("ok","one","one","admin");

    log.info("{}",!Sets.intersection(left,right).isEmpty());

  }

}