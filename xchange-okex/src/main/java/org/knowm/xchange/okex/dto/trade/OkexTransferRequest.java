package org.knowm.xchange.okex.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * <p> @Date : 2023/6/20 </p>
 *
 * <p> @author konbluesky </p>
 */
@Builder
public class OkexTransferRequest {

    @JsonProperty("ccy")
    private String currency;

    @JsonProperty("amt")
    private String amount;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("subAcct")
    private String subAccount;

    @JsonProperty("type")
    private String type;

    @JsonProperty("clientId")
    private String clientId;

}
