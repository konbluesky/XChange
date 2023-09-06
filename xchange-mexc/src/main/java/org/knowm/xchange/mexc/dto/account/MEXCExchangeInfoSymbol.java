package org.knowm.xchange.mexc.dto.account;

import java.util.Collection;
import lombok.Getter;
import lombok.ToString;
import org.knowm.xchange.mexc.dto.OrderEnum;

/**
 * <p> @Date : 2023/9/6 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXCExchangeInfoSymbol {

  /**
   * 参数名	数据类型	说明
   * timezone	string	时区
   * serverTime	long	服务器时间
   * rateLimits	Array	频率限制
   * exchangeFilters	Array	过滤器
   * symbol	String	交易对
   * status	String	状态
   * baseAsset	String	交易币
   * baseAssetPrecision	Int	交易币精度
   * quoteAsset	String	计价币
   * quotePrecision	Int	计价币价格精度
   * quoteAssetPrecision	Int	计价币资产精度
   * baseCommissionPrecision	Int	交易币手续费精度
   * quoteCommissionPrecision	Int	计价币手续费精度
   * orderTypes	Array	订单类型
   * quoteOrderQtyMarketAllowed	Boolean	是否允许市价委托
   * isSpotTradingAllowed	Boolean	是否允许api现货交易
   * isMarginTradingAllowed	Boolean	是否允许api杠杆交易
   * permissions	Array	权限
   * maxQuoteAmount	String	最大下单金额
   * makerCommission	String	marker手续费
   * takerCommission	String	taker手续费
   * quoteAmountPrecision	string	最小下单金额
   * baseSizePrecision	string	最小下单数量
   * quoteAmountPrecisionMarket	string	市价最小下单金额
   * maxQuoteAmountMarket	String	市价最大下单金额
   */

  /**
   * 交易对
   */
  private String symbol;

  /**
   * 状态
   */
  private String status;

  /**
   * 交易币
   */
  private String baseAsset;

  /**
   * 交易币精度
   */
  private Integer baseAssetPrecision;

  /**
   * 计价币
   */
  private String quoteAsset;

  /**
   * 计价币价格精度
   */
  private Integer quotePrecision;

  /**
   * 计价币资产精度
   */
  private Integer quoteAssetPrecision;

  /**
   * 交易币手续费精度
   */
  private Integer baseCommissionPrecision;

  /**
   * 计价币手续费精度
   */
  private Integer quoteCommissionPrecision;

  /**
   * 订单类型
   */
  private Collection<String> orderTypes;

  /**
   * 是否允许市价委托
   */
  private boolean quoteOrderQtyMarketAllowed;

  /**
   * 是否允许api现货交易
   */
  private boolean isSpotTradingAllowed;

  /**
   * 是否允许api杠杆交易
   */
  private boolean isMarginTradingAllowed;

  /**
   * 权限
   */
  private String[] permissions;

  /**
   * 最大下单金额
   */
  private String maxQuoteAmount;

  /**
   * marker手续费
   */
  private String makerCommission;

  /**
   * taker手续费
   */
  private String takerCommission;

  /**
   * 最小下单金额
   */
  private String quoteAmountPrecision;

  /**
   * 最小下单数量
   */
  private String baseSizePrecision;

  /**
   * 市价最小下单金额
   */
  private String quoteAmountPrecisionMarket;

  /**
   * 市价最大下单金额
   */
  private String maxQuoteAmountMarket;


}
