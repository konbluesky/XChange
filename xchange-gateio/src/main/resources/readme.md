### Gateio交易所

#### Restful 需要实现的接口

- [x] AccountService#getAccountInfo
    - [x] getWallets
        - > 官方没有对钱包进行区别，但是为了上层应用统一调用，统一放到(WalletFeature.TRADING)
    - [x] cancelWithdrawal(String withdrawalId)
        - > Gateio提现取消有延迟10分钟，
    - [x] withdrawFunds(WithdrawFundsParams params)
    - [ ] getFundingHistory
- [ ] MarketDataService
    - [ ] getTicker
    - [ ] getTickers(null) - > all
- [ ] TradeService#cancelOrder
    - [x] cancelOrder(DefaultCancelOrderByInstrumentAndIdParams(instrument, orderId))
    - [x] placeMarketOrder
    - [x] placeLimitOrder
    - [x] getOpenOrders
    - [x] getOrder
        - > 实现细则
          > LimitOrder limitOrder = new LimitOrder(
          > mexcOrder.getSide().equalsIgnoreCase("buy") ? OrderType.BID : OrderType.ASK,
          > new BigDecimal(mexcOrder.getOrigQty()),
          > new BigDecimal(mexcOrder.getExecutedQty()),
          > extractOneCurrencyPairs(mexcOrder.getSymbol()),
          > String.valueOf(mexcOrder.getOrderId()),
          > new Date(mexcOrder.getUpdateTime()),
          > new BigDecimal(mexcOrder.getPrice()));
          > //MEXC不返回平均价格, 手动计算
          > limitOrder.setAveragePrice(new BigDecimal(mexcOrder.getCummulativeQuoteQty()).divide(
          limitOrder.getOriginalAmount(),new MathContext(8, RoundingMode.HALF_DOWN)));
          > limitOrder.setOrderStatus(Order.OrderStatus.valueOf(mexcOrder.getStatus()));
    - [x] getOrder(DefaultQueryOrderParamInstrument instrument)
- [ ] Exchange#getExchangeMetaData
    - 根据isShouldLoadRemoteMetaData判断是否需要加载远程数据

#### Websocket 需要实现的接口

### 注意事项

1. 断流机制一定要加
    2. 初始化位置GateioExchange#getResilienceRegistries
    3. BaseService 使用BaseResilientExchangeService 基类
2. limitorder中平均价格需要处理,防止上层应用调用平均价格时为0;
3. 签名BaseParamsDigest 要仔细确认
4. gateio 序列化enum需要注意， enum都要小写