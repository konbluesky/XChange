package org.knowm.xchange.okex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <p> @Date : 2023/5/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Builder
@Getter
public class OkexCancelWithdrawalRequest {

    @JsonProperty("wdId")
    private String wdId;
}
