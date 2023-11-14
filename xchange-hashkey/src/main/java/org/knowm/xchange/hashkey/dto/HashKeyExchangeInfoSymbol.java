package org.knowm.xchange.hashkey.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/11/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class HashKeyExchangeInfoSymbol {

  /** 交易对标识 */
  private String symbol;

  /** 交易对名称 */
  private String symbolName;

  /** 交易对状态 */
  private HashKeySymbolStatus status;

  /** 基础资产 */
  private String baseAsset;

  /** 基础资产名称 */
  private String baseAssetName;

  /** 基础资产精度 */
  private String baseAssetPrecision;

  /** 报价资产 */
  private String quoteAsset;

  /** 报价资产名称 */
  private String quoteAssetName;

  /** 报价资产价格精度 */
  private String quotePrecision;

  /** 是否允许零售 */
  private boolean retailAllowed;

  /** 是否允许PI */
  private boolean piAllowed;

  /** 是否允许公司交易 */
  private boolean corporateAllowed;

  /** 是否允许总账交易 */
  private boolean omnibusAllowed;

  /** 是否允许冰山订单 */
  private boolean icebergAllowed;

  /** 是否为聚合 */
  private boolean isAggregate;

  /** 是否允许保证金交易 */
  private boolean allowMargin;

  /** 交易对的过滤器列表 */
  private List<Filter> filters;


  /**
   * 表示交易对的过滤器。
   */
  public static class Filter {

    private String minPrice; // 最小价格
    private String maxPrice; // 最大价格
    private String tickSize; // 价格步进
    private HashKeyTradeRestrictionType filterType; // 过滤器类型
    private String minQty; // 最小数量
    private String maxQty; // 最大数量
    private String stepSize; // 数量步进
    private String minNotional; // 最小名义金额
    private String minAmount; // 最小金额
    private String maxAmount; // 最大金额
    private String minBuyPrice; // 最小买价
    private String maxSellPrice; // 最大卖价
    private String buyPriceUpRate; // 买价上浮比例
    private String sellPriceDownRate; // 卖价下跌比例
    private String noAllowMarketStartTime; // 不允许市价开始时间
    private String noAllowMarketEndTime; // 不允许市价结束时间
    private String limitOrderStartTime; // 限价订单开始时间
    private String limitOrderEndTime; // 限价订单结束时间
    private String limitMinPrice; // 限价最小价格
    private String limitMaxPrice; // 限价最大价格


  }
}