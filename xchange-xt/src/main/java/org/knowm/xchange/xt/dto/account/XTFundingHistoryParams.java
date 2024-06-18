package org.knowm.xchange.xt.dto.account;

import lombok.Builder;
import lombok.Getter;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;

/**
 * <p> @Date : 2023/7/16 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@Builder
public class XTFundingHistoryParams implements TradeHistoryParams {
    /**
     * 币种名称，可从'获取XT可充提的币种'接口中获取
     */
    private String currency;

    /**
     * 转账网络名称，可从'获取XT可充提的币种'接口中获取
     */
    private String chain;

    /**
     * 提现记录的状态，字符串类型（含义见公共模块-充值/提现记录状态码及含义）
     * SUBMIT、REVIEW、AUDITED、AUDITED_AGAIN、PENDING、SUCCESS、FAIL、CANCEL
     */
    private String status;

    /**
     * 上次开始分页的Id，即记录的主键id
     */
    private Long fromId;

    /**
     * 分页方向
     * NEXT：下一页，PREV：上一页
     */
    private String direction;

    /**
     * 每页记录数，最大不超过200
     * 1 <= limit <= 200
     */
    private int limit;

    /**
     * 查询范围开始边界，毫秒级时间戳
     */
    private Long startTime;

    /**
     * 查询范围结束边界，毫秒级时间戳
     */
    private Long endTime;


}
