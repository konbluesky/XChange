package org.knowm.xchange.xt.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class XTOrderBook {
    private final List<XTOrderBookItem> asks;
    private final List<XTOrderBookItem> bids;
    private final String ts;

    @JsonCreator
    public XTOrderBook(
            @JsonProperty("s") String symbol,
            @JsonProperty("t") String ts,
            @JsonProperty("a") List<XTOrderBookItem> asks,
            @JsonProperty("b") List<XTOrderBookItem> bids
    ) {

        this.asks = asks;
        this.bids = bids;
        this.ts = ts;
    }
}
