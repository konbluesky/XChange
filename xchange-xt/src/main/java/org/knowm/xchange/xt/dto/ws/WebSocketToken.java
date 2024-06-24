package org.knowm.xchange.xt.dto.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class WebSocketToken {

  @JsonProperty("accessToken")
  private String accessToken;

  @JsonProperty("refreshToken")
  private String refreshToken;
}
