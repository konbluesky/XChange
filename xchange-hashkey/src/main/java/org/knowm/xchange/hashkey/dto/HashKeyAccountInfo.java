package org.knowm.xchange.hashkey.dto;

import java.util.List;
import lombok.Getter;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class HashKeyAccountInfo {

  private List<HashKeyBalance> balances;
  private String userId;

}
