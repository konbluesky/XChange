package org.knowm.xchange.okex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * https://www.okx.com/docs-v5/zh/#rest-api-funding-cancel-withdrawal
 * <p> @Date : 2023/5/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class OkexCancelWithdrawalResponse {

    @JsonProperty("wdId")
    private String wdId;

}
