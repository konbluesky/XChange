package org.knowm.xchange.gateio;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderStatus;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.FundingRecord.Status;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.account.Wallet.WalletFeature;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.marketdata.Trades.TradeSortType;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.gateio.dto.GateioEnums;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderSide;
import org.knowm.xchange.gateio.dto.GateioOrderType;
import org.knowm.xchange.gateio.dto.account.GateioBalanceItem;
import org.knowm.xchange.gateio.dto.account.GateioDepositResponse;
import org.knowm.xchange.gateio.dto.account.GateioFunds;
import org.knowm.xchange.gateio.dto.account.GateioSpotBalanceResponse;
import org.knowm.xchange.gateio.dto.account.GateioUnifiedAccount;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawStatus;
import org.knowm.xchange.gateio.dto.account.GateioWithdrawalResponse;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoin;
import org.knowm.xchange.gateio.dto.marketdata.GateioCurrencyMetaData;
import org.knowm.xchange.gateio.dto.marketdata.GateioDepth;
import org.knowm.xchange.gateio.dto.marketdata.GateioPair;
import org.knowm.xchange.gateio.dto.marketdata.GateioTicker;
import org.knowm.xchange.gateio.dto.marketdata.GateioTradeHistory;
import org.knowm.xchange.gateio.dto.trade.GateioOpenOrder;
import org.knowm.xchange.gateio.dto.trade.GateioOpenOrders;
import org.knowm.xchange.gateio.dto.trade.GateioOrder;
import org.knowm.xchange.gateio.dto.GateioEnums.FinishAs;
import org.knowm.xchange.gateio.dto.trade.GateioTrade;
import org.knowm.xchange.gateio.service.GateioMarketDataServiceRaw;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.DateUtils;

/**
 * Various adapters for converting from Bter DTOs to XChange DTOs
 */
public final class GateioAdapters {

  /**
   * private Constructor
   */
  private GateioAdapters() {
  }

  public static CurrencyPair adaptCurrencyPair(String pair) {
    final String[] currencies = pair.toUpperCase().split("_");
    return new CurrencyPair(currencies[0], currencies[1]);
  }

  public static Ticker adaptTicker(CurrencyPair currencyPair, GateioTicker gateioTicker) {

    BigDecimal ask = gateioTicker.getLowestAsk();
    BigDecimal bid = gateioTicker.getHighestBid();
    BigDecimal last = gateioTicker.getLast();
    BigDecimal low = gateioTicker.getLow24h();
    BigDecimal high = gateioTicker.getHigh24h();
    // Looks like gate.io vocabulary is inverted...
    BigDecimal baseVolume = gateioTicker.getQuoteVolume();
    BigDecimal quoteVolume = gateioTicker.getBaseVolume();
    BigDecimal percentageChange = gateioTicker.getChangePercentage();

    return new Ticker.Builder()
        .currencyPair(currencyPair)
        .ask(ask)
        .bid(bid)
        .last(last)
        .low(low)
        .high(high)
        .volume(baseVolume)
        .quoteVolume(quoteVolume)
        .percentageChange(percentageChange)
        .timestamp(new Date())
        .build();
  }

  public static LimitOrder adaptLimitOrder(GateioOrder order) {
    LimitOrder limitOrder = new LimitOrder(
        order.getSide() == OrderSide.BUY ? OrderType.BID : OrderType.ASK,
        order.getAmount(),
        order.getAmount().subtract(order.getLeft()),
        GateioUtils.toCurrencyPair(order.getCurrencyPair()),
        order.getId(),
        new Date(order.getUpdateTimeMs()),
        order.getPrice());
    limitOrder.setAveragePrice(
        order.getAvgDealPrice() == null ? BigDecimal.ZERO : order.getAvgDealPrice());
    limitOrder.setOrderStatus(convertOrderStatus(order.getStatus(), order.getFinishAs()));
    return limitOrder;
  }


  /**
   * cexStatus 订单状态。 - open: 等待处理 - closed: 全部成交 - cancelled: 订单撤销
   * <p>
   * finishAs 订单结束方式，包括： - open: 等待处理 - filled: 完全成交 - cancelled: 用户撤销 - ioc: 未立即完全成交，因为 tif 设置为 io
   * - stp: 订单发生自成交限制而被撤销
   *
   * @param cexStatus
   * @param finishAs
   * @return
   */
  private static OrderStatus convertOrderStatus(GateioEnums.OrderStatus cexStatus,
      FinishAs finishAs) {
    switch (cexStatus) {
      case OPEN:
        switch (finishAs) {
          case OPEN:
            return OrderStatus.PENDING_NEW;
          case FILLED:
            return OrderStatus.FILLED;
          case CANCELLED:
            return OrderStatus.CANCELED;
          case IOC:
            return OrderStatus.PARTIALLY_FILLED; // Assuming IOC means partially filled
          case STP:
            return OrderStatus.CANCELED;
          default:
            return OrderStatus.UNKNOWN;
        }

      case CLOSED:
        switch (finishAs) {
          case OPEN:
            return OrderStatus.PENDING_CANCEL;
          case FILLED:
            return OrderStatus.CLOSED;
          case CANCELLED:
            return OrderStatus.CANCELED;
          case IOC:
            return OrderStatus.PARTIALLY_CANCELED;
          case STP:
            return OrderStatus.CANCELED;
          default:
            return OrderStatus.UNKNOWN;
        }

      case CANCELLED:
        return OrderStatus.CANCELED;

      default:
        return OrderStatus.UNKNOWN;
    }
  }

  public static List<LimitOrder> adaptOpenOrders(List<GateioOpenOrders> orderList) {
    List<LimitOrder> limitOrders = new ArrayList<>();
    for (GateioOpenOrders openOrders : orderList) {
      limitOrders.addAll(adaptOrders(openOrders.getOrders()));
    }
    return limitOrders;
  }


  public static List<LimitOrder> adaptOrders(
      List<GateioOrder> orders) {
    List<LimitOrder> limitOrders = new ArrayList<>();
    for (GateioOrder order : orders) {
      limitOrders.add(adaptLimitOrder(order));
    }
    return limitOrders;
  }

  public static OrderBook adaptOrderBook(GateioDepth depth, CurrencyPair currencyPair) {

//    List<LimitOrder> asks =
//        GateioAdapters.adaptOrders(depth.getAsks(), currencyPair, OrderType.ASK);
//    Collections.reverse(asks);
//    List<LimitOrder> bids =
//        GateioAdapters.adaptOrders(depth.getBids(), currencyPair, OrderType.BID);

//    return new OrderBook(null, asks, bids);
    return null;
  }

  public static LimitOrder adaptOrder(GateioOpenOrder order, Collection<Instrument> currencyPairs) {

    String[] currencyPairSplit = order.getCurrencyPair().split("_");
    CurrencyPair currencyPair = new CurrencyPair(currencyPairSplit[0], currencyPairSplit[1]);
    return new LimitOrder(
        order.getType().equals("sell") ? OrderType.ASK : OrderType.BID,
        order.getAmount(),
        currencyPair,
        order.getOrderNumber(),
        null,
        order.getRate());
  }

  public static OrderType adaptOrderType(GateioOrderType cryptoTradeOrderType) {

    return (cryptoTradeOrderType.equals(GateioOrderType.BUY)) ? OrderType.BID : OrderType.ASK;
  }

  public static Trade adaptTrade(
      GateioTradeHistory.GateioPublicTrade trade, CurrencyPair currencyPair) {

    OrderType orderType = adaptOrderType(trade.getType());
    Date timestamp = DateUtils.fromMillisUtc(trade.getDate() * 1000);

    return new Trade.Builder()
        .type(orderType)
        .originalAmount(trade.getAmount())
        .currencyPair(currencyPair)
        .price(trade.getPrice())
        .timestamp(timestamp)
        .id(trade.getTradeId())
        .build();
  }

  public static Trades adaptTrades(GateioTradeHistory tradeHistory, CurrencyPair currencyPair) {

    List<Trade> tradeList = new ArrayList<>();
    long lastTradeId = 0;
    for (GateioTradeHistory.GateioPublicTrade trade : tradeHistory.getTrades()) {
      String tradeIdString = trade.getTradeId();
      if (!tradeIdString.isEmpty()) {
        long tradeId = Long.valueOf(tradeIdString);
        if (tradeId > lastTradeId) {
          lastTradeId = tradeId;
        }
      }
      Trade adaptedTrade = adaptTrade(trade, currencyPair);
      tradeList.add(adaptedTrade);
    }

    return new Trades(tradeList, lastTradeId, TradeSortType.SortByTimestamp);
  }

  public static Wallet adaptWalletForUnifiedAccount(GateioUnifiedAccount account) {
    List<Balance> balances = new ArrayList<>();
    for (Entry<String, GateioBalanceItem> item : account.getBalances()
        .entrySet()) {
      Currency currency = Currency.getInstance(item.getKey().toUpperCase());
      GateioBalanceItem gateIoBalanceItem = item.getValue();
      BigDecimal total = gateIoBalanceItem.getAvailable().add(gateIoBalanceItem.getFreeze());
      Balance balance = new Balance(currency, total, gateIoBalanceItem.getAvailable(),
          gateIoBalanceItem.getFreeze());
      balances.add(balance);
    }
    return Wallet.Builder.from(balances).build();
  }

  public static Wallet adaptWalletForSpotAccount(List<GateioSpotBalanceResponse> items) {
    List<Balance> balances = new ArrayList<>();
    for (GateioSpotBalanceResponse item : items) {
      Currency currency = Currency.getInstance(item.getCurrency().toUpperCase());
      Balance balance = new Balance(currency, item.getTotal(), item.getAvailable(),
          item.getLocked());
      balances.add(balance);
    }
    return Wallet.Builder.from(balances).features(Collections.singleton(WalletFeature.TRADING))
        .build();
  }

  public static Wallet adaptWallet(GateioFunds bterAccountInfo) {

    List<Balance> balances = new ArrayList<>();
    for (Entry<String, BigDecimal> funds : bterAccountInfo.getAvailableFunds().entrySet()) {
      Currency currency = Currency.getInstance(funds.getKey().toUpperCase());
      BigDecimal amount = funds.getValue();
      BigDecimal locked = bterAccountInfo.getLockedFunds().get(currency.toString());

      balances.add(new Balance(currency, null, amount, locked == null ? BigDecimal.ZERO : locked));
    }
    for (Entry<String, BigDecimal> funds : bterAccountInfo.getLockedFunds().entrySet()) {
      Currency currency = Currency.getInstance(funds.getKey().toUpperCase());
      if (balances.stream().noneMatch(balance -> balance.getCurrency().equals(currency))) {
        BigDecimal amount = funds.getValue();
        balances.add(new Balance(currency, null, BigDecimal.ZERO, amount));
      }
    }

    return Wallet.Builder.from(balances).build();
  }

  public static UserTrades adaptUserTrades(List<GateioTrade> userTrades) {

    List<UserTrade> trades = new ArrayList<>();
    for (GateioTrade userTrade : userTrades) {
      trades.add(adaptUserTrade(userTrade));
    }

    return new UserTrades(trades, TradeSortType.SortByTimestamp);
  }

  public static UserTrade adaptUserTrade(GateioTrade gateioTrade) {

    OrderType orderType = adaptOrderType(gateioTrade.getType());
    Date timestamp = DateUtils.fromMillisUtc(gateioTrade.getTimeUnix() * 1000);
    CurrencyPair currencyPair = adaptCurrencyPair(gateioTrade.getPair());

    return UserTrade.builder()
        .type(orderType)
        .originalAmount(gateioTrade.getAmount())
        .currencyPair(currencyPair)
        .price(gateioTrade.getRate())
        .timestamp(timestamp)
        .id(gateioTrade.getTradeID())
        .orderId(gateioTrade.getOrderNumber())
        .build();
  }

  public static ExchangeMetaData adaptToExchangeMetaData(
      GateioMarketDataServiceRaw marketDataService) throws IOException {

    Map<Instrument, InstrumentMetaData> currencyPairs = new HashMap<>();
    Map<Currency, CurrencyMetaData> currencies = new HashMap<>();

    Map<String, GateioWithdrawStatus> gateioWithDrawFees = marketDataService.getGateioWithDrawFees(
        null);

    marketDataService.getGateioMarketInfo().forEach((k, v) -> {
      CurrencyPair currencyPair = k;
      GateioPair gateioPair = v;

      if (gateioPair.getMaxQuoteAmount() == null || gateioPair.getMinQuoteAmount() == null) {
        return;
      }

      currencyPairs.put(
          currencyPair,
          new InstrumentMetaData.Builder()
              .tradingFee(new BigDecimal(gateioPair.getFee()))
              .maximumAmount(new BigDecimal(gateioPair.getMaxQuoteAmount()))
              .minimumAmount(new BigDecimal(gateioPair.getMinQuoteAmount()))
              .priceScale(gateioPair.getPrecision())
              .build());
    });

    if (marketDataService.getApiKey() != null) {
      Map<String, GateioCoin> coins = marketDataService.getGateioCoinInfo();
      for (String coin : coins.keySet()) {
        GateioCoin gateioCoin = coins.get(coin);
        GateioWithdrawStatus gateioWithdrawStatus = gateioWithDrawFees.get(coin);
        if (gateioCoin != null && gateioWithdrawStatus != null) {
          currencies.put(new Currency(coin),
              adaptCurrencyMetaData(gateioCoin, gateioWithdrawStatus));
        }
      }
    }

    return new ExchangeMetaData(currencyPairs, currencies, null, null, null);
  }

  private static CurrencyMetaData adaptCurrencyMetaData(
      GateioCoin gateioCoin, GateioWithdrawStatus gateioFeeInfo) {
    WalletHealth walletHealth = WalletHealth.ONLINE;
    if (gateioCoin.isWithdrawDelayed()) {
      walletHealth = WalletHealth.UNKNOWN;
    } else if (gateioCoin.isDelisted()
        || (gateioCoin.isWithdrawDisabled() && gateioCoin.isDepositDisabled())) {
      walletHealth = WalletHealth.OFFLINE;
    } else if (gateioCoin.isDepositDisabled()) {
      walletHealth = WalletHealth.DEPOSITS_DISABLED;
    } else if (gateioCoin.isWithdrawDisabled()) {
      walletHealth = WalletHealth.WITHDRAWALS_DISABLED;
    }
    return new GateioCurrencyMetaData(
        0,
        new BigDecimal(gateioFeeInfo.getWithdrawFix()),
        new BigDecimal(gateioFeeInfo.getWithdrawAmountMini()),
        walletHealth, gateioCoin.getChain(),gateioFeeInfo);
  }


  public static List<FundingRecord> adaptDepositsWithdrawals(
      List<GateioDepositResponse> depositResponses,
      List<GateioWithdrawalResponse> withdrawalResponses){
    List<FundingRecord> result = new ArrayList<>();
    depositResponses.forEach(d -> {
      FundingRecord r = new FundingRecord(
          d.getAddress(),
          new Date(Long.valueOf(d.getTimestamp())),
          Currency.getInstance(d.getCurrency()),
          d.getAmount(),
          d.getId(),
          d.getTransferHash(),
          FundingRecord.Type.DEPOSIT,
          status(d.getStatus()),
          null,
          null,
          null);
      result.add(r);
    });
    withdrawalResponses.forEach(w -> {
      FundingRecord r = new FundingRecord(
          w.getAddress(),
          new Date(Long.valueOf(w.getTimestamp())),
          Currency.getInstance(w.getCurrency()),
          w.getAmount(),
          w.getId(),
          w.getTransferHash(),
          FundingRecord.Type.WITHDRAWAL,
          status(w.getStatus()),
          null,
          null,
          null);
      result.add(r);
    });
    return result;

  }

  /**
   * 交易状态
   *
   * - DONE: 完成
   * - CANCEL: 已取消
   * - REQUEST: 请求中
   * - MANUAL: 待人工审核
   * - BCODE: 充值码操作
   * - EXTPEND: 已经发送等待确认
   * - FAIL: 链上失败等待确认
   * - INVALID: 无效订单
   * - VERIFY: 验证中
   * - PROCES: 处理中
   * - PEND: 处理中
   * - DMOVE: 待人工审核
   * - SPLITPEND: cny提现大于5w,自动分单
   * @param gateioStatus
   * @return
   */
  private static FundingRecord.Status status(String gateioStatus) {

    switch (gateioStatus){
      case "DONE":
        return Status.COMPLETE;
      case "CANCEL":
        return Status.CANCELLED;
      case "REQUEST":
      case "MANUAL":
      case "BCODE":
      case "EXTPEND":
      case "FAIL":
      case "INVALID":
      case "VERIFY":
      case "PROCES":
      case "PEND":
      case "DMOVE":
      case "SPLITPEND":
        return Status.PROCESSING;
    }
    return Status.PROCESSING;
  }
}
