package org.knowm.xchange.okex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * https://www.okx.com/docs-v5/zh/#rest-api-funding-get-withdrawal-history
 * <p> @Date : 2023/5/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class OkexWithdrawalHistoryResponse {

    /**
     * ccy	String	币种
     * chain	String	币种链信息
     * 有的币种下有多个链，必须要做区分，如USDT下有USDT-ERC20，USDT-TRC20，USDT-Omni多个链
     * nonTradableAsset	Boolean	是否为不可交易资产
     * true：不可交易资产，false：可交易资产
     * amt	String	数量
     * ts	String	提币申请时间，Unix 时间戳的毫秒数格式，如 1655251200000
     * from	String	提币账户
     * 可以是邮箱/手机号
     * areaCodeFrom	String	如果from为手机号，该字段为该手机号的区号
     * to	String	收币地址
     * areaCodeTo	String	如果to为手机号，该字段为该手机号的区号
     * tag	String	部分币种提币需要标签，若不需要则不返回此字段
     * pmtId	String	部分币种提币需要此字段（payment_id），若不需要则不返回此字段
     * memo	String	部分币种提币需要此字段，若不需要则不返回此字段
     * addrEx	Object	提币地址备注，部分币种提币需要，若不需要则不返回此字段。如币种TONCOIN的提币地址备注标签名为comment,则该字段返回：{'comment':'123456'}
     * txId	String	提币哈希记录
     * 内部转账该字段返回""
     * fee	String	提币手续费数量
     * feeCcy	String	提币手续费币种，如 USDT
     * state	String	提币状态
     * -3：撤销中
     * -2：已撤销
     * -1：失败
     * 0：等待提币
     * 1：提币中
     * 2：提币成功
     * 7: 审核通过
     * 10: 等待划转
     * 4, 5, 6, 8, 9, 12: 等待客服审核
     * wdId	String	提币申请ID
     * clientId	String	客户自定义ID
     */
    @JsonProperty("ccy")
    private String ccy;

    @JsonProperty("chain")
    private String chain;

    @JsonProperty("nonTradableAsset")
    private Boolean nonTradableAsset;

    @JsonProperty("amt")
    private String amt;

    @JsonProperty("ts")
    private String ts;

    @JsonProperty("from")
    private String from;

    @JsonProperty("areaCodeFrom")
    private String areaCodeFrom;

    @JsonProperty("to")
    private String to;

    @JsonProperty("areaCodeTo")
    private String areaCodeTo;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("pmtId")
    private String pmtId;

    @JsonProperty("memo")
    private String memo;

    @JsonProperty("addrEx")
    private Object addrEx;

    @JsonProperty("txId")
    private String txId;

    @JsonProperty("fee")
    private String fee;

    @JsonProperty("feeCcy")
    private String feeCcy;

    @JsonProperty("state")
    private String state;

    @JsonProperty("wdId")
    private String wdId;

    @JsonProperty("clientId")
    private String clientId;

}
