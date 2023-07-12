package org.knowm.xchange.xt.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class XTTicker {

    /**
     * 交易对
     */
    @JsonProperty("s")
    private String symbol;

    /**
     * 更新时间
     */
    @JsonProperty("t")
    private long time;

    /**
     * 价格变动
     */
    @JsonProperty("cv")
    private String changeValue;

    /**
     * 价格变动百分比
     */
    @JsonProperty("cr")
    private String changeRate;

    /**
     * 最早一笔
     */
    @JsonProperty("o")
    private String open;

    /**
     * 最低价
     */
    @JsonProperty("l")
    private String low;

    /**
     * 最高价
     */
    @JsonProperty("h")
    private String high;

    /**
     * 最后一笔
     */
    @JsonProperty("c")
    private String close;

    /**
     * 成交量
     */
    @JsonProperty("q")
    private String quantity;

    /**
     * 成交额
     */
    @JsonProperty("v")
    private String volume;

    /**
     * 卖一价
     */
    @JsonProperty("ap")
    private String asksPrice;

    /**
     * 卖一量
     */
    @JsonProperty("aq")
    private String asksQty;

    /**
     * 买一价
     */
    @JsonProperty("bp")
    private String bidsPrice;

    /**
     * 买一量
     */
    @JsonProperty("bq")
    private String bidsQty;

    /**
     * 最新价格(price)
     * 完整的ticker接口不返回，只有在【最新价格】接口里才有
     */
    @JsonProperty("p")
    private String price;


}
