package org.knowm.xchange.hashkey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.account.Wallet.WalletFeature;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.hashkey.dto.HashKeyAccountInfo;
import org.knowm.xchange.hashkey.dto.HashKeyBalance;
import org.knowm.xchange.hashkey.dto.HashKeyCancelOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyCreateOrderResponse;
import org.knowm.xchange.hashkey.dto.HashKeyOrderDetailResponse;
import org.knowm.xchange.instrument.Instrument;

/**
 * <p> @Date : 2023/11/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyAdapters {

  public static String convertToHashKeySymbol(String instrumentName) {
    return instrumentName.replace("/", "").toUpperCase();
  }

  public static Wallet adaptHashKeyBalances(HashKeyAccountInfo accountInfo) {
    List<Balance> balances = new ArrayList<>(accountInfo.getBalances().size());
    for (HashKeyBalance balance : accountInfo.getBalances()) {
      BigDecimal available = new BigDecimal(balance.getFree());
      BigDecimal frozen = new BigDecimal(balance.getLocked());
      balances.add(
          new Balance(new Currency(balance.getAsset()),
              frozen.add(available),
              available
          ));
    }
    return Wallet.Builder.from(balances).features(Collections.singleton(WalletFeature.TRADING))
        .build();
  }

//  public static UserTrades adaptUserTrades(List<HashKeyOrderDetailResponse> tradeHistorys, ExchangeMetaData exchangeMetaData) {
//    List<UserTrade> userTradeList = new ArrayList<>();
//    tradeHistorys.forEach(detailResponse -> {
//      Instrument instrument = extractOneCurrencyPairs(detailResponse.getSymbol());
//      userTradeList.add(new UserTrade.Builder().originalAmount(convertContractSizeToVolume(detailResponse.getAmount(), instrument,
//              exchangeMetaData.getInstruments()
//                  .get(instrument)
//                  .getContractValue()))
//          .instrument(instrument)
//          .price(new BigDecimal(detailResponse.getAverageFilledPrice()))
//          .type(adaptOkexOrderSideToOrderType(detailResponse.getSide()))
//          .id(detailResponse.getOrderId())
//          .orderId(detailResponse.getOrderId())
//          .timestamp(Date.from(Instant.ofEpochMilli(Long.parseLong(detailResponse.getUpdateTime()))))
//          .feeAmount(new BigDecimal(detailResponse.getFee()))
//          .feeCurrency(new Currency(detailResponse.getFeeCurrency()))
//          .orderUserReference(detailResponse.getClientOrderId())
//          .build());
//    });
//    return new UserTrades(userTradeList, Trades.TradeSortType.SortByTimestamp);
//  }


  public static OpenOrders adaptOpenOrders(List<HashKeyOrderDetailResponse> orderDetails) {
    List<LimitOrder> limitOrders = new ArrayList<>();
    for (HashKeyOrderDetailResponse orderDetail : orderDetails) {
      limitOrders.add((LimitOrder) adaptOrderByQueryOrder(orderDetail));
    }
    return new OpenOrders(limitOrders);
  }


  public static Order adaptOrderByCancelOrder(HashKeyCancelOrderResponse hashKeyOrder) {
    LimitOrder limitOrder = new LimitOrder(
        hashKeyOrder.getSide().equalsIgnoreCase("buy") ? OrderType.BID : OrderType.ASK,
        new BigDecimal(hashKeyOrder.getOrigQty()),
        extractOneCurrencyPairs(hashKeyOrder.getSymbol()),
        String.valueOf(hashKeyOrder.getOrderId()),
        new Date(hashKeyOrder.getTransactTime()),
        new BigDecimal(hashKeyOrder.getPrice()),
        new BigDecimal(hashKeyOrder.getPrice()),
        new BigDecimal(hashKeyOrder.getExecutedQty()),
        null,
        Order.OrderStatus.valueOf(hashKeyOrder.getStatus()));
    return limitOrder;
  }


  public static Order adaptOrderByQueryOrder(HashKeyOrderDetailResponse hashKeyOrder) {
    LimitOrder limitOrder = new LimitOrder(
        hashKeyOrder.getSide().equalsIgnoreCase("buy") ? OrderType.BID : OrderType.ASK,
        new BigDecimal(hashKeyOrder.getOrigQty()),
        extractOneCurrencyPairs(hashKeyOrder.getSymbol()),
        String.valueOf(hashKeyOrder.getOrderId()),
        new Date(hashKeyOrder.getUpdateTime()),
        new BigDecimal(hashKeyOrder.getPrice()),
        new BigDecimal(hashKeyOrder.getAvgPrice()),
        new BigDecimal(hashKeyOrder.getCumulativeQuoteQty()),
        null,
        Order.OrderStatus.valueOf(hashKeyOrder.getStatus()));
    return limitOrder;

  }

  public static Order adaptOrder(HashKeyCreateOrderResponse hashKeyOrder) {

    LimitOrder limitOrder = new LimitOrder(
        hashKeyOrder.getSide().equalsIgnoreCase("buy") ? OrderType.BID : OrderType.ASK,
        BigDecimal.valueOf(hashKeyOrder.getOrigQty()),
        BigDecimal.valueOf(hashKeyOrder.getExecutedQty()),
        extractOneCurrencyPairs(hashKeyOrder.getSymbol()),
        String.valueOf(hashKeyOrder.getOrderId()),
        new Date(hashKeyOrder.getTransactTime()),
        BigDecimal.valueOf(hashKeyOrder.getPrice()));
    limitOrder.setOrderStatus(Order.OrderStatus.valueOf(hashKeyOrder.getStatus()));
    return limitOrder;

  }


  public static Instrument extractOneCurrencyPairs(String symbol) {
    return extractCurrencyPairs(symbol).get(0);
  }

  /**
   * 根据/defaultSymbols 统计出目前支持的后缀，手动转换
   *
   * @param symbols
   * @return
   */
  public static List<CurrencyPair> extractCurrencyPairs(String... symbols) {
    List<CurrencyPair> currencyPairs = new ArrayList<>();
    List<String> tokenSuffixes = Arrays.asList("USDC", "USDT", "ETH", "BUSD", "BTC", "TUSD");
    for (String token : symbols) {
      for (String suffix : tokenSuffixes) {
        if (token.contains(suffix)) {
          String baseCurrency = token.replace(suffix, "");
          currencyPairs.add(new CurrencyPair(baseCurrency, suffix));
          break;
        }
      }
    }
    return currencyPairs;
  }

}
