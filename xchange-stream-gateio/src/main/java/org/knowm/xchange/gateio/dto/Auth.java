package org.knowm.xchange.gateio.dto;

import lombok.Data;

/**
 * <p> @Date : 2024/2/21 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
public class Auth {

  private String method;
  private String key;
  private String sign;
}
