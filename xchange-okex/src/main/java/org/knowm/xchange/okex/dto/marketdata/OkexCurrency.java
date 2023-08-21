package org.knowm.xchange.okex.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Author: Max Gao (gaamox@tutanota.com) Created: 08-06-2021
 */

/** https://www.okex.com/docs-v5/en/#rest-api-funding-get-currencies * */
@Getter
@NoArgsConstructor
@ToString
public class OkexCurrency {

    @JsonProperty("ccy")
    private String currency;

    @JsonProperty("name")
    private String name;

    @JsonProperty("chain")
    private String chain;

    @JsonProperty("canDep")
    private boolean canDep;

    @JsonProperty("canWd")
    private boolean canWd;

    @JsonProperty("canInternal")
    private boolean canInternal;

    @JsonProperty("minWd")
    private String minWd;

    @JsonProperty("maxWd")
    private String maxWd;

    @JsonProperty("minFee")
    private String minFee;

    @JsonProperty("maxFee")
    private String maxFee;

    @JsonProperty("wdTickSz")
    private String wdTickSz;

    @JsonProperty("mainNet")
    private boolean mainNet;

    @JsonProperty("logoLink")
    private String logoLink;
}
