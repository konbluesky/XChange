package org.knowm.xchange.okex.dto.trade;

public enum OkexOrderType {
    market,
    limit,
    post_only,
    fok,
    ioc,
    optimal_limit_ioc,
    mmp,
    mmp_and_post_only;
    public static OkexOrderType fromString(String s) {
        return OkexOrderType.valueOf(s.toLowerCase());
    }
}
