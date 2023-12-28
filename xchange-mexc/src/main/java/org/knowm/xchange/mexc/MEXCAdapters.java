package org.knowm.xchange.mexc;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.FundingRecord.Status;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.account.Wallet.WalletFeature;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.dto.OrderEnum;
import org.knowm.xchange.mexc.dto.account.MEXCBalance;
import org.knowm.xchange.mexc.dto.account.MEXCConfig;
import org.knowm.xchange.mexc.dto.account.MEXCDepositHistory;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfo;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfoSymbol;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;
import org.knowm.xchange.mexc.dto.account.MEXCWithDrawHistory;
import org.knowm.xchange.mexc.dto.market.MEXC24Ticker;
import org.knowm.xchange.mexc.dto.market.MEXCCurrencyMetaData;
import org.knowm.xchange.mexc.dto.market.MEXCExchangeMetaData;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderDetail;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;

@Slf4j
public class MEXCAdapters {


  public static Wallet adaptMEXCBalances(List<MEXCBalance> rawBalances) {
    List<Balance> balances = new ArrayList<>(rawBalances.size());
    for (MEXCBalance mexcBalance : rawBalances) {
      BigDecimal available = new BigDecimal(mexcBalance.getFree());
      BigDecimal frozen = new BigDecimal(mexcBalance.getLocked());
      balances.add(new Balance(new Currency(mexcBalance.getAsset()),
          frozen.add(available),
          available
      ));
    }
    return Wallet.Builder.from(balances).features(Collections.singleton(WalletFeature.TRADING))
        .build();
  }


  public static String convertToMEXCSymbol(String instrumentName) {
    return instrumentName.replace("/", "").toUpperCase();
  }

  public static List<Ticker> adaptTickers(List<MEXC24Ticker> tickers) {
    return tickers.stream().map(ticker -> {
      return new Ticker.Builder()
          .instrument(extractOneCurrencyPairs(ticker.getSymbol()))
          .last(new BigDecimal(ticker.getLastPrice()))
          .volume(new BigDecimal(ticker.getVolume()))
          .quoteVolume(new BigDecimal(ticker.getQuoteVolume()))
          .timestamp(new Date())
          .build();
    }).collect(Collectors.toList());
  }

  public static Ticker adaptTicker(MEXCPricePair tickerPair) {
    return new Ticker.Builder()
        .instrument(extractOneCurrencyPairs(tickerPair.getSymbol()))
        .last(new BigDecimal(tickerPair.getPrice()))
        .timestamp(new Date())
        .build();
  }

  public static ExchangeMetaData adaptToExchangeMetaData(MEXCExchangeInfo info,
      List<MEXCConfig> configs, Set<String> identities) {

    Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
    Map<Instrument, InstrumentMetaData> instruments = new HashMap<>();
    Table<Currency, String, MEXCNetwork> currencyNetworks = HashBasedTable.create();

    for (MEXCConfig config : configs) {
      for (MEXCNetwork networkConfig : config.getNetworkList()) {
        if (identities.contains(networkConfig.getNetwork())) {
          currencyNetworks.put(Currency.getInstance(config.getCoin()), networkConfig.getNetwork(),
              networkConfig);
        }
      }
    }

    for (MEXCExchangeInfoSymbol symbol : info.getSymbols()) {

      Instrument pair = extractOneCurrencyPairs(symbol.getSymbol());
      Currency base = pair.getBase();
      // // warn me 默认将bsc链的信息放入currencyMeta中
      // currencyNetworks.row(base).keySet().containsAll(identities)
      // 过滤状态开启，支持现货交易，支持市价单
      //
      if (!"ENABLED".equals(symbol.getStatus())
          || !symbol.getOrderTypes().containsAll(Arrays.asList("MARKET", "LIMIT_MAKER", "LIMIT"))
          || !symbol.isSpotTradingAllowed()
          || currencyNetworks.row(base).isEmpty()
          || Sets.intersection(identities, currencyNetworks.row(base).keySet()).isEmpty()
      ) {
        continue;
      }

      instruments.put(pair, new InstrumentMetaData.Builder()
          .minimumAmount(new BigDecimal(symbol.getBaseSizePrecision()))
          .marketOrderEnabled(true)
          .build());
      // 包装类,使用默认网络(BSC),将可提现,可充值等信息存入实体
      MEXCCurrencyMetaData currencyMetaData = new MEXCCurrencyMetaData(
          symbol.getBaseAssetPrecision(),
          null,
          null,
          null
      );
      currencyMetaData.setNetworks(currencyNetworks.row(base).values());
      currencies.put(pair.getBase(), currencyMetaData);
    }

    return new MEXCExchangeMetaData(instruments, currencies, null, null, null, currencyNetworks);
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

  public static List<FundingRecord> adaptWithFundingRecords(
      List<MEXCWithDrawHistory> withdrawApplies) {
    List<FundingRecord> fundingRecords = new ArrayList<>();
    for (MEXCWithDrawHistory withDrawHistory : withdrawApplies) {
      FundingRecord fundingRecord = new FundingRecord(
          withDrawHistory.getAddress(),
          new Date(withDrawHistory.getApplyTime()),
          Currency.getInstance(withDrawHistory.getCoin()),
          new BigDecimal(withDrawHistory.getAmount()),
          withDrawHistory.getId(),
          withDrawHistory.getTxId(),
          FundingRecord.Type.WITHDRAWAL,
          withDrawStatus(withDrawHistory.getStatus()),
          null, null, withDrawHistory.getMemo() + ";" + withDrawHistory.getRemark()
      );
      fundingRecords.add(fundingRecord);
    }
    return fundingRecords;
  }

  public static List<FundingRecord> adaptDepositFundingRecords(
      List<MEXCDepositHistory> withdrawApplies) {
    List<FundingRecord> fundingRecords = new ArrayList<>();
    for (MEXCDepositHistory depositHistory : withdrawApplies) {
      FundingRecord fundingRecord = new FundingRecord(
          depositHistory.getAddress(),
          new Date(depositHistory.getInsertTime()),
          Currency.getInstance(depositHistory.getCoin()),
          new BigDecimal(depositHistory.getAmount()),
          depositHistory.getTxId(),
          depositHistory.getTxId(),
          FundingRecord.Type.DEPOSIT,
          depositStatus(depositHistory.getStatus()),
          null, null, depositHistory.getMemo()
      );
      fundingRecords.add(fundingRecord);
    }
    return fundingRecords;
  }


  /**
   * 充值状态，1:小额充值，2:延遲到賬，3:大額充值，4:等待中，5:入账成功，6:审核中，7:驳回
   *
   * @param status
   * @return
   */
  public static FundingRecord.Status depositStatus(String status) {
    switch (status) {
      case "1":
      case "2":
      case "3":
      case "4":
      case "6":
        return FundingRecord.Status.PROCESSING;
      case "5":
        return FundingRecord.Status.COMPLETE;
      case "7":
        return FundingRecord.Status.FAILED;
      default:
        return Status.FAILED;
    }
  }

  /**
   * 提币状态，1:提交申请，2:审核中，3:等待处理，4:处理中，5:等待打包，6:等待确认，7:提现成功，8:提现失败，9:已取消，10:手动入账
   *
   * @param status
   * @return
   */
  public static FundingRecord.Status withDrawStatus(String status) {
    switch (status) {
      case "1":
      case "2":
      case "3":
      case "4":
      case "5":
      case "6":
        return FundingRecord.Status.PROCESSING;
      case "7":
        return FundingRecord.Status.COMPLETE;
      case "8":
        return FundingRecord.Status.FAILED;
      case "9":
        return FundingRecord.Status.CANCELLED;
      case "10":
        return FundingRecord.Status.COMPLETE;
      default:
        return Status.FAILED;
    }

  }


  public static Instrument extractOneCurrencyPairs(String symbol) {
    return extractCurrencyPairs(symbol).get(0);
  }

  public static MEXCOrderRequestPayload adaptOrder(LimitOrder limitOrder) {
    MEXCOrderRequestPayload.MEXCOrderRequestPayloadBuilder builder = MEXCOrderRequestPayload.builder()
        .symbol(convertToMEXCSymbol(limitOrder.getInstrument().toString()))
        .price(limitOrder.getLimitPrice().toString())
        .quantity(limitOrder.getOriginalAmount().toString())
        .side(limitOrder.getType() == OrderType.BID ? "BUY" : "SELL")
        .type("LIMIT");
    if (limitOrder.hasFlag(OrderEnum.OrderType.LIMIT_MAKER)) {
      builder.type("LIMIT_MAKER");
    }
    return builder.build();
  }

  public static MEXCOrderRequestPayload adaptMarketOrder(MarketOrder marketOrder) {
    MEXCOrderRequestPayload payload = MEXCOrderRequestPayload.builder()
        .symbol(convertToMEXCSymbol(marketOrder.getInstrument().toString()))
        .quantity(marketOrder.getOriginalAmount().toString())
        .side(marketOrder.getType() == OrderType.BID ? "BUY" : "SELL")
        .type("MARKET")
        .build();
    return payload;
  }

  public static Order adaptOrder(MEXCOrderDetail mexcOrder) {

    LimitOrder limitOrder = new LimitOrder(
        mexcOrder.getSide().equalsIgnoreCase("buy") ? OrderType.BID : OrderType.ASK,
        new BigDecimal(mexcOrder.getOrigQty()),
        new BigDecimal(mexcOrder.getExecutedQty()),
        extractOneCurrencyPairs(mexcOrder.getSymbol()),
        String.valueOf(mexcOrder.getOrderId()),
        new Date(mexcOrder.getUpdateTime()),
        new BigDecimal(mexcOrder.getPrice()));
    //MEXC不返回平均价格, 手动计算
    limitOrder.setAveragePrice(limitOrder.getCumulativeAmount().divide(limitOrder.getOriginalAmount(),new MathContext(8, RoundingMode.HALF_DOWN)));
    limitOrder.setOrderStatus(Order.OrderStatus.valueOf(mexcOrder.getStatus()));
    return limitOrder;
  }

  public static List<LimitOrder> adaptOpenOrders(List<MEXCOrderDetail> orderDetails) {
    List<LimitOrder> limitOrders = new ArrayList<>();
    for (MEXCOrderDetail orderDetail : orderDetails) {
      limitOrders.add((LimitOrder) adaptOrder(orderDetail));
    }
    return limitOrders;
  }


  private static BigDecimal getAveragePrice(BigDecimal dealQuantity, BigDecimal dealAmount) {
    if (dealQuantity.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return dealAmount.divide(dealQuantity, RoundingMode.HALF_EVEN);
  }

}
