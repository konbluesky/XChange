package org.knowm.xchange.mexc.dto.market;

import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/12/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXC24Ticker {
  /**
   * 参数名	说明
   * symbol	交易对
   * priceChange	价格变化
   * priceChangePercent	价格变化比
   * prevClosePrice	前一收盘价
   * lastPrice	最新价
   * lastQty	最新量
   * bidPrice	买盘价格
   * bidQty	买盘数量
   * askPrice	卖盘价格
   * askQty	卖盘数量
   * openPrice	开始价
   * highPrice	最高价
   * lowPrice	最低价
   * volume	成交量
   * quoteVolume	成交额
   * openTime	开始时间
   * closeTime	结束时间
   * count
   */
  // 交易对
  private String symbol;
  // 价格变化
  private String priceChange;
  // 价格变化比
  private String priceChangePercent;
  // 前一收盘价
  private String prevClosePrice;
  // 最新价
  private String lastPrice;
  // 最新量
  private String lastQty;
  // 买盘价格
  private String bidPrice;
  // 买盘数量
  private String bidQty;
  // 卖盘价格
  private String askPrice;
  // 卖盘数量
  private String askQty;
  // 开始价
  private String openPrice;
  // 最高价
  private String highPrice;
  // 最低价
  private String lowPrice;
  // 成交量
  private String volume;
  // 成交额
  private String quoteVolume;

}
