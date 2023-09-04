package org.knowm.xchange.mexc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class MEXCBalance {
    /**
     * 发生时间
     */
    @JsonProperty("c")
    private long time;

    /**
     * 币种
     */
    @JsonProperty("a")
    private String currency;

    /**
     * 可用资产
     */
    @JsonProperty("f")
    private String balance;

    /**
     * 可用资产变动金额
     */
    @JsonProperty("fd")
    private String balanceChange;

    /**
     * 冻结资产
     */
    @JsonProperty("l")
    private String frozen;

    /**
     * 冻结变动金额
     */
    @JsonProperty("ld")
    private String frozenChange;

    /**
     * 余额变动类型：
     * WITHDRAW 提现
     * WITHDRAW_FEE 提现手续费
     * DEPOSIT 充值
     * DEPOSIT_FEE 充值手续费
     * ENTRUST 委托成交
     * ENTRUST_PLACE 下单
     * ENTRUST_CANCEL 撤单
     * TRADE_FEE 手续费
     * ENTRUST_UNFROZEN 订单冻结资金返还
     * SUGAR 空投
     * ETF_INDEX ETF下单
     */
    @JsonProperty("o")
    private String bizType;


}
