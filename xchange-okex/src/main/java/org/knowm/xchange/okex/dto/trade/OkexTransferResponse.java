package org.knowm.xchange.okex.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/6/20 </p>
 * <a href="https://www.okx.com/docs-v5/zh/#rest-api-funding-get-account-asset-valuation">...</a>
 * <p> @author konbluesky </p>
 */
@Getter
public class OkexTransferResponse {

    /**
     * transId	String	划转 ID
     * ccy	String	划转币种
     * from	String	转出账户
     * amt	String	划转量
     * to	String	转入账户
     * clientId	String	客户自定义ID
     */
    @JsonProperty("transId")
    private String transId;

    @JsonProperty("ccy")
    private String currency;

    @JsonProperty("from")
    private String from;

    @JsonProperty("amt")
    private String amount;

    @JsonProperty("to")
    private String to;

    @JsonProperty("clientId")
    private String clientId;

}
