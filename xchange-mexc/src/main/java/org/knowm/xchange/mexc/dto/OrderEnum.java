package org.knowm.xchange.mexc.dto;

/**
 * <p> @Date : 2023/9/6 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class OrderEnum {

  /**
   * 订单方向
   * BUY 买入
   * SELL 卖出
   */
  public enum SIDE{
    BUY,SELL
  }

  /**
   * 订单类型
   * LIMIT 限价单
   * MARKET 市价单
   * LIMIT_MAKER 限价只挂单
   * IMMEDIATE_OR_CANCEL IOC单 (无法立即成交的部分就撤销,订单在失效前会尽量多的成交。)
   * FILL_OR_KILL FOK单 (无法全部立即成交就撤销,如果无法全部成交,订单会失效。)
   */
  public enum OrderType{
    LIMIT,MARKET,LIMIT_MAKER,IMMEDIATE_OR_CANCEL,FILL_OR_KILL
  }

  /**
   * 订单状态
   * NEW 未成交
   * FILLED 已成交
   * PARTIALLY_FILLED 部分成交
   * CANCELED 已撤销
   * PARTIALLY_CANCELED 部分撤销
   */
  public enum OrderStatus {
    NEW, FILLED, PARTIALLY_FILLED, CANCELED, PARTIALLY_CANCELED
  }

  /**
   * 变动类型
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
  public enum ChangeType {
    WITHDRAW, WITHDRAW_FEE, DEPOSIT, DEPOSIT_FEE, ENTRUST, ENTRUST_PLACE, ENTRUST_CANCEL, TRADE_FEE, ENTRUST_UNFROZEN, SUGAR, ETF_INDEX
  }

}
