package org.knowm.xchange.binance.dto.account;

/**
 * <p> @Date : 2023/8/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public enum TransferType {
  // 现货钱包转向U本位合约钱包
  MAIN_UMFUTURE,
  // 现货钱包转向币本位合约钱包
  MAIN_CMFUTURE,
  // 现货钱包转向杠杆全仓钱包
  MAIN_MARGIN,
  // U本位合约钱包转向现货钱包
  UMFUTURE_MAIN,
  // U本位合约钱包转向杠杆全仓钱包
  UMFUTURE_MARGIN,
  // 币本位合约钱包转向现货钱包
  CMFUTURE_MAIN,
  // 杠杆全仓钱包转向现货钱包
  MARGIN_MAIN,
  // 杠杆全仓钱包转向U本位合约钱包
  MARGIN_UMFUTURE,
  // 杠杆全仓钱包转向币本位合约钱包
  MARGIN_CMFUTURE,
  // 币本位合约钱包转向杠杆全仓钱包
  CMFUTURE_MARGIN,
  // 杠杆逐仓钱包转向杠杆全仓钱包
  ISOLATEDMARGIN_MARGIN,
  // 杠杆全仓钱包转向杠杆逐仓钱包
  MARGIN_ISOLATEDMARGIN,
  // 杠杆逐仓钱包转向杠杆逐仓钱包
  ISOLATEDMARGIN_ISOLATEDMARGIN,
  // 现货钱包转向资金钱包
  MAIN_FUNDING,
  // 资金钱包转向现货钱包
  FUNDING_MAIN,
  // 资金钱包转向U本位合约钱包
  FUNDING_UMFUTURE,
  // U本位合约钱包转向资金钱包
  UMFUTURE_FUNDING,
  // 杠杆全仓钱包转向资金钱包
  MARGIN_FUNDING,
  // 资金钱包转向杠杆全仓钱包
  FUNDING_MARGIN,
  // 资金钱包转向币本位合约钱包
  FUNDING_CMFUTURE,
  // 币本位合约钱包转向资金钱包
  CMFUTURE_FUNDING,
  // 现货钱包转向期权钱包
  MAIN_OPTION,
  // 期权钱包转向现货钱包
  OPTION_MAIN,
  // U本位合约钱包转向期权钱包
  UMFUTURE_OPTION,
  // 期权钱包转向U本位合约钱包
  OPTION_UMFUTURE,
  // 杠杆全仓钱包转向期权钱包
  MARGIN_OPTION,
  // 期权全仓钱包转向杠杆钱包
  OPTION_MARGIN,
  // 资金钱包转向期权钱包
  FUNDING_OPTION,
  // 期权钱包转向资金钱包
  OPTION_FUNDING,
  // 现货钱包转向统一账户钱包
  MAIN_PORTFOLIO_MARGIN,
  // 统一账户钱包转向现货钱包
  PORTFOLIO_MARGIN_MAIN,
  // 现货钱包转向逐仓账户钱包
  MAIN_ISOLATED_MARGIN,
  // 逐仓钱包转向现货账户钱包
  ISOLATED_MARGIN_MAIN
}
