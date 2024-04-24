### Xt 交易所CheckList

#### Restful 需要实现的接口

- [x] AccountService#getAccountInfo
    - [x] getWallets
        - > 官方没有对钱包进行区别，但是为了上层应用统一调用，统一放到(WalletFeature.TRADING)
    - [x] cancelWithdrawal(String withdrawalId)
        - > Gateio提现取消有延迟10分钟，
    - [x] withdrawFunds(WithdrawFundsParams params)
    - [x] getFundingHistory
- [x] MarketDataService
    - [x] getTicker(Instrument instrument, Object... args)
    - [x] getTickers(null) - > all
      - 获取最近24h的交易量， 用来筛选活跃币种
- [x] TradeService#cancelOrder
    - [x] cancelOrder(DefaultCancelOrderByInstrumentAndIdParams(instrument, orderId))
      - DefaultCancelOrderByInstrumentAndIdParams 重载,因上层接口都是使用该参数
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
- [x] Exchange#getExchangeMetaData
    - 根据isShouldLoadRemoteMetaData判断是否需要加载远程数据

#### Websocket 需要实现的接口

- [x] StreamingAccountService
    - [x] getBalanceChanges
- [x] StreamingMarketDataService
  - [x] getOrderBook
  - [x] getTicker
- [x] StreamingTradeService
  - [x] getOrderChanges(Instrument instrument, Object... args)
    - 主要实现这个接口
  - [x] getOrderChanges(CurrencyPair currencyPair, Object... args)
    - 重载
- [x] StreamServicePool
  - 不同交易所对一个websocket链接的有订阅和连接数的要求， 因此需要实现pool
  - 非官方统计过 gateio 单链接最大50订阅topic，最多50链接数
### 注意事项

1. 断流机制一定要加
    2. 初始化位置GateioExchange#getResilienceRegistries
    3. BaseService 使用BaseResilientExchangeService 基类
2. limitorder中平均价格需要处理,防止上层应用调用平均价格时为0;
3. Order对象，ticker对象中时间戳一定要放；
3. 签名BaseParamsDigest 要仔细确认
4. gateio 序列化enum需要注意， enum都要小写
5. 注意异常处理，gateio返回错误时，使用400错误码
   > 2024-03-04 14:51:17.967 ERROR[pool-3-thread-9]com.block.trade.swap.core.swap.job.CleanBalance1mJob.call:240 CleanBalance5mJob error
   si.mazi.rescu.HttpStatusIOException: HTTP status code was not OK: 400