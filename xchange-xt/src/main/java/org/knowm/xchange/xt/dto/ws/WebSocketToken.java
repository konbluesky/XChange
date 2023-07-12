package org.knowm.xchange.xt.dto.ws;

import lombok.Getter;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class WebSocketToken {
    private String accessToken;
    private String refreshToken;
}
