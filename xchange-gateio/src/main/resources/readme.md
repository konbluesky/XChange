- [x] AccountService#getAccountInfo
    - [x] getWallets
      - > 官方没有对钱包进行区别，但是为了上层应用统一调用，统一放到(WalletFeature.TRADING)
    - [x] cancelWithdrawal(String withdrawalId)
      - > Gateio提现取消有延迟10分钟，
    - [x] withdrawFunds(WithdrawFundsParams params)
    - [ ] AccountService#getFundingHistory
- [ ] MarketDataService#getTicker
- [ ] MarketDataService#getTickers
    - [ ] getTickers(null) - > all
- [ ] TradeService#cancelOrder
    - [ ] cancelOrder(DefaultCancelOrderByInstrumentAndIdParams(instrument, orderId))
- [ ] TradeService#placeMarketOrder
- [ ] TradeService#getOpenOrders
- [ ] TradeService#getOpenOrders
- [ ] TradeService#getOrder
    - [ ] getOrder(DefaultQueryOrderParamInstrument instrument)
- [ ] TradeService#placeLimitOrder
- [ ] Exchange#getExchangeMetaData

### 注意事项
1. 断流机制一定要加
2. limitorder中平均价格需要处理,防止上层应用调用平均价格时为0;
3. 