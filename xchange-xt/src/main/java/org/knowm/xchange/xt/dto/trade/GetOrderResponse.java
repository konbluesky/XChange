package org.knowm.xchange.xt.dto.trade;

import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class GetOrderResponse {
    /**
     * 交易对
     */
    private String symbol;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 客户端订单号
     */
    private String clientOrderId;

    /**
     * 标的币种
     */
    private String baseCurrency;

    /**
     * 报价币种
     */
    private String quoteCurrency;

    /**
     * 订单方向 BUY-买,SELL-卖
     */
    private String side;

    /**
     * 订单类型 LIMIT-现价,MARKET-市价
     */
    private String type;

    /**
     * 有效方式 GTC,IOC,FOK,GTX
     */
    private String timeInForce;

    /**
     * 价格
     */
    private String price;

    /**
     * 原始数量
     */
    private String origQty;

    /**
     * 原始金额
     */
    private String origQuoteQty;

    /**
     * 已执行数量
     */
    private String executedQty;

    /**
     * 待执行数量（若撤单或下单拒绝，该值为0）
     */
    private String leavingQty;

    /**
     * 成交标的(成交数量)
     */
    private String tradeBase;

    /**
     * 成交报价(成交金额)
     */
    private String tradeQuote;

    /**
     * 成交均价
     */
    private String avgPrice;

    /**
     * 手续费
     */
    private String fee;

    /**
     * 手续费币种
     */
    private String feeCurrency;

    /**
     * 订单状态 NEW-新建,PARTIALLY_FILLED-部分成交,FILLED-全部成交,CANCELED-用户撤单,
     * REJECTED-下单失败,EXPIRED-过期(time_in_force撤单或溢价撤单)
     */
    private String state;

    /**
     * 订单时间
     */
    private long time;

    /**
     * 订单更新时间
     */
    private long updatedTime;

}
