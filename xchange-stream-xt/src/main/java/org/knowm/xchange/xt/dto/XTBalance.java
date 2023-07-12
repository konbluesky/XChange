package org.knowm.xchange.xt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class XTBalance {
    /**
     * 账号ID
     */
    @JsonProperty("a")
    private String accountId;

    /**
     * 发生时间
     */
    @JsonProperty("t")
    private long time;

    /**
     * 币种
     */
    @JsonProperty("c")
    private String currency;

    /**
     * 可用资产
     */
    @JsonProperty("b")
    private String balance;

    /**
     * 冻结资产
     */
    @JsonProperty("f")
    private String frozen;

    /**
     * 业务类型 [SPOT, LEVER]
     */
    @JsonProperty("z")
    private String bizType;

    /**
     * 交易市场
     */
    @JsonProperty("s")
    private String symbol;

}
