package org.knowm.xchange.xt;

import com.google.common.base.Strings;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.xt.dto.account.BalanceResponse;
import org.knowm.xchange.xt.dto.account.WithdrawHistoryResponse;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyChainInfo;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;
import org.knowm.xchange.xt.dto.trade.GetOrderResponse;
import org.knowm.xchange.xt.dto.trade.PlaceOrderRequest;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.knowm.xchange.xt.dto.trade.XTOrderEnum;
import org.knowm.xchange.xt.dto.trade.XTOrderEnum.TimeInForce;

import static org.knowm.xchange.xt.service.XTMarketDataServiceRaw.BNB_SMART_CHAIN;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTAdapters {

    public static Ticker adaptTicker(XTTicker ticker, Instrument instrument) {
        return new Ticker.Builder()
                .instrument(instrument)
                .ask(new BigDecimal(ticker.getAsksPrice() == null ? "0" : ticker.getAsksPrice()))
                .bid(new BigDecimal(ticker.getBidsPrice() == null ? "0" : ticker.getBidsPrice()))
                .last(new BigDecimal(ticker.getPrice() == null ?
                        ticker.getClose() == null ? "0" : ticker.getClose() :
                        ticker.getPrice()))
                .volume(new BigDecimal(ticker.getVolume() == null ? "0" : ticker.getVolume()))
                .timestamp(new Date(ticker.getTime()))
                .build();
    }

    public static List<Ticker> adaptTickers(List<XTTicker> tickers) {
        return tickers.stream().map(t -> adaptTicker(t, adaptInstrumentId(t.getSymbol()))).collect(Collectors.toList());
    }

    public static Wallet adaptWallet(List<BalanceResponse> walletSupportCurrencies) {
        if (walletSupportCurrencies == null || walletSupportCurrencies.isEmpty()) {
            return Wallet.Builder.from(Collections.emptyList())
                                 .id("TRADING_WALLET_ID")
                                 .features(new HashSet<>(Collections.singletonList(Wallet.WalletFeature.TRADING)))
                                 .build();
        }
        List<Balance> balances = walletSupportCurrencies.stream()
                                                        .map(balanceResponse -> new Balance.Builder()
                                                                .currency(new Currency(balanceResponse.getCurrency()))
                                                                .total(new BigDecimal(balanceResponse.getTotalAmount()))
                                                                .frozen(new BigDecimal(balanceResponse.getFrozenAmount()))
                                                                .available(new BigDecimal(balanceResponse.getAvailableAmount()))
                                                                .timestamp(new Date())
                                                                .build())
                                                        .collect(Collectors.toList());

        return Wallet.Builder.from(balances)
                             .id("TRADING_WALLET_ID")
                             .features(new HashSet<>(Collections.singletonList(Wallet.WalletFeature.TRADING)))
                             .build();
    }


    public static ExchangeMetaData adaptToExchangeMetaData(List<XTSymbol> symbols,
                                                           Map<String, XTCurrencyWalletInfo> walletSupportCurrencys) {
        Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
        Map<Instrument, InstrumentMetaData> instruments = new HashMap<>();

        for (XTSymbol symbol : symbols) {

            if (!"ONLINE".equals(symbol.getState())) {
                continue;
            }

            Instrument pair = adaptInstrumentId(symbol.getSymbol());

            XTCurrencyWalletInfo xtCurrencyWalletInfo = walletSupportCurrencys.get(pair.getBase().toString()
                                                                                       .toLowerCase());
            if (xtCurrencyWalletInfo == null) {
                continue;
            }
            XTCurrencyChainInfo walletInfo = xtCurrencyWalletInfo.getWalletInfo(BNB_SMART_CHAIN);

            instruments.put(pair, new InstrumentMetaData.Builder()
                    .minimumAmount(new BigDecimal(walletInfo.getWithdrawMinAmount()))
                    .marketOrderEnabled(true)
                    .build());
            currencies.put(pair.getBase(), new CurrencyMetaData(null,
                    new BigDecimal(walletInfo.getWithdrawFeeAmount()),
                    new BigDecimal(walletInfo.getWithdrawMinAmount()),
                    walletInfo.isWithdrawEnabled() && walletInfo.isDepositEnabled() ? WalletHealth.ONLINE : WalletHealth.OFFLINE
            ));
        }
        return new ExchangeMetaData(instruments, currencies, null, null, null);
    }

    public static Instrument adaptInstrumentId(String instrumentId) {
        String[] tokens = instrumentId.split("_");
        if (tokens.length == 2) {
            // SPOT or Margin
            return new CurrencyPair(tokens[0], tokens[1]);
        }
        return null;
    }

    public static String adaptInstrument(Instrument instrument) {
        return instrument.toString()
                         .replace('/', '_');
    }


    public static PlaceOrderRequest adaptOrder(Order order, boolean isLimit) {
        if (isLimit) {
            LimitOrder limitOrder = (LimitOrder) order;
            PlaceOrderRequest.PlaceOrderRequestBuilder builder = PlaceOrderRequest.builder()
                                    .symbol(adaptInstrument(order.getInstrument()))
                                    .side(order.getType() == Order.OrderType.BID ? "BUY" : "SELL")
                                    .clientOrderId(order.getUserReference())
                                    .type(isLimit ? "LIMIT" : "MARKET")
                                    //GTC,FOK,IOC,GTX
                                    .timeInForce("GTC")
                                    .bizType("SPOT")
                                    .quantity(order.getOriginalAmount().toPlainString())
                                    .price(limitOrder.getLimitPrice()
                                                     .toString());
            if(order.hasFlag(TimeInForce.IOC)){
                builder.timeInForce("IOC");
            }
            return builder.build();
        } else {
          MarketOrder marketOrder = (MarketOrder) order;
          PlaceOrderRequest.PlaceOrderRequestBuilder builder = PlaceOrderRequest.builder();
          builder.symbol(adaptInstrument(order.getInstrument()))
              .side(order.getType() == Order.OrderType.BID ? "BUY" : "SELL")
              .clientOrderId(order.getUserReference())
              .type(isLimit ? "LIMIT" : "MARKET")
              //GTC,FOK,IOC,GTX
              .timeInForce("GTC")
              .bizType("SPOT")
              .quantity(order.getOriginalAmount().toPlainString());

          if (order.hasFlag(TimeInForce.IOC)) {
            builder.timeInForce("IOC");
          }
          return builder.build();
        }
    }

    public static LimitOrder adaptOrder(GetOrderResponse order, ExchangeMetaData exchangeMetaData) {
        Instrument instrument = adaptInstrumentId(order.getSymbol());
        return new LimitOrder(
                "buy".equalsIgnoreCase(order.getSide()) ? Order.OrderType.BID : Order.OrderType.ASK,
                new BigDecimal(order.getOrigQty()),
                instrument,
                order.getOrderId(),
                new Date(order.getTime()),
                new BigDecimal(order.getPrice()),
                Strings.isNullOrEmpty(order.getAvgPrice()) ? BigDecimal.ZERO : new BigDecimal(order.getAvgPrice()),
                Strings.isNullOrEmpty(order.getExecutedQty()) ? BigDecimal.ZERO : new BigDecimal(order.getExecutedQty()),
                Strings.isNullOrEmpty(order.getFee()) ? BigDecimal.ZERO : new BigDecimal(order.getFee()),
                Order.OrderStatus.valueOf(order.getState().toUpperCase(Locale.ENGLISH)), order.getClientOrderId());
    }


    public static OpenOrders adaptOpenOrders(List<GetOrderResponse> orders, ExchangeMetaData exchangeMetaData) {
        List<LimitOrder> openOrders = orders.stream()
                                            .map(order -> adaptOrder(order, exchangeMetaData))
                                            .collect(Collectors.toList());
        return new OpenOrders(openOrders);
    }

    public static List<FundingRecord> adaptWithdraws(List<WithdrawHistoryResponse> depositHistoryResponses) {
        return depositHistoryResponses.stream()
                                      .map(XTAdapters::adaptWithdrawalHistoryResponse)
                                      .collect(Collectors.toList());
    }

    public static FundingRecord adaptWithdrawalHistoryResponse(WithdrawHistoryResponse response) {
        //https://doc.xt.com/#deposit_withdrawal_cnwithdrawHistory
        return new FundingRecord.Builder().setAddress(response.getAddress())
                                          .setAmount(new BigDecimal(response.getAmount()))
                                          .setCurrency(Currency.getInstance(response.getCurrency()))
                                          .setDate(new Date(Long.valueOf(response.getCreatedTime())))
                                          .setFee(new BigDecimal(response.getFee()))
                                          .setDescription(response.getMemo())
                                          //internalId is wdId
                                          .setInternalId(String.valueOf(response.getId()))
                                          .setStatus(convertWithdrawalStatus(response.getStatus()))
                                          .setBlockchainTransactionHash(response.getTransactionId())
                                          .setType(FundingRecord.Type.WITHDRAWAL)
                                          .build();
    }

    /**
     * 充值/提现记录状态码及含义
     * Status	说明
     * SUBMIT	提现: 未冻结
     * REVIEW	提现: 已冻结,待审核
     * AUDITED	提现: 已审核,发送钱包,待上链
     * AUDITED_AGAIN	复审中
     * PENDING	充值/提现: 已上链
     * SUCCESS	完成
     * FAIL	失败
     * CANCEL	已取消
     */
    private static FundingRecord.Status convertWithdrawalStatus(String status) {
        switch (status) {
            case "SUBMIT":
            case "REVIEW":
            case "AUDITED":
            case "AUDITED_AGAIN":
            case "PENDING":
                return FundingRecord.Status.PROCESSING;
            case "SUCCESS":
                return FundingRecord.Status.COMPLETE;
            case "FAIL":
                return FundingRecord.Status.FAILED;
            case "CANCEL":
                return FundingRecord.Status.CANCELLED;
            default:
                return FundingRecord.Status.FAILED;

        }
    }
}
