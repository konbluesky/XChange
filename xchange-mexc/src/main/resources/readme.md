- [ ] AccountService#getAccountInfo
- [ ] AccountService#withdrawFunds
- [ ] AccountService#getFundingHistory
- [ ] MarketDataService#getTicker
- [ ] MarketDataService#getTickers
- [ ] TradeService#cancelOrder
- [ ] TradeService#placeMarketOrder
- [ ] TradeService#getOpenOrders
- [ ] TradeService#getOpenOrders
- [ ] TradeService#getOrder
- [ ] TradeService#placeLimitOrder


### 注意事项
1. 断流机制一定要加
2. limitorder中平均价格需要处理,防止上层应用调用平均价格时为0;
3. 