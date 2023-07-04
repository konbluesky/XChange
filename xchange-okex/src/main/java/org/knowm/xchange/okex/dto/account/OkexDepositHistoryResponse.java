package org.knowm.xchange.okex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * https://www.okx.com/docs-v5/zh/#rest-api-funding-get-deposit-address
 * https://www.okx.com/docs-v5/zh/#websocket-api-private-channel-deposit-info-channel
 * 共用一个实体类
 * <p> @Date : 2023/5/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class OkexDepositHistoryResponse {

    /**
     * 参数名	类型	描述
     * ccy	String	币种名称，如 BTC
     * chain	String	币种链信息
     * 有的币种下有多个链，必须要做区分，如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     * amt	String	充值数量
     * from	String	充值账户
     * 如果该笔充值来自于内部转账，则该字段展示内部转账发起者的账户信息，可以是手机号、邮箱、账户名称，其他情况返回""
     * areaCodeFrom	String	如果from为手机号，该字段为该手机号的区号
     * to	String	到账地址
     * 如果该笔充值来自于链上充值，则该字段展示链上地址，其他情况返回""
     * txId	String	区块转账哈希记录
     * ts	String	充值到账时间，Unix 时间戳的毫秒数格式，如 1655251200000
     * state	String	充值状态
     * 0：等待确认
     * 1：确认到账
     * 2：充值成功
     * 8：因该币种暂停充值而未到账，恢复充值后自动到账
     * 11：命中地址黑名单
     * 12：账户或充值被冻结
     * 13：子账户充值拦截
     * 14：KYC限额
     * depId	String	充值记录 ID
     * fromWdId	String	内部转账发起者提币申请 ID
     * 如果该笔充值来自于内部转账，则该字段展示内部转账发起者的提币申请 ID，其他情况返回""
     * actualDepBlkConfirm	String	最新的充币网络确认数
     */
    @JsonProperty("ccy")
    private String ccy;

    @JsonProperty("chain")
    private String chain;

    @JsonProperty("amt")
    private String amt;

    @JsonProperty("from")
    private String from;

    @JsonProperty("areaCodeFrom")
    private String areaCodeFrom;

    @JsonProperty("to")
    private String to;

    @JsonProperty("txId")
    private String txId;

    @JsonProperty("ts")
    private String ts;

    @JsonProperty("state")
    private String state;

    @JsonProperty("depId")
    private String depId;

    @JsonProperty("fromWdId")
    private String fromWdId;

    @JsonProperty("actualDepBlkConfirm")
    private String actualDepBlkConfirm;

    /****************************************************************************************************/
    /**** websocket api private channel deposit info channel **/
    /****************************************************************************************************/
    @JsonProperty("pTime")
    private String pTime;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("subAcct")
    private String subAcct;


}
