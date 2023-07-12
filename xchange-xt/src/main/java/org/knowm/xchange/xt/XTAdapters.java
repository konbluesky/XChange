package org.knowm.xchange.xt;

import com.google.common.base.Strings;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyInfo;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;
import org.knowm.xchange.xt.dto.trade.GetOrderResponse;
import org.knowm.xchange.xt.dto.trade.PlaceOrderRequest;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
                .last(new BigDecimal(ticker.getPrice() == null ? "0" : ticker.getPrice()))
                .volume(new BigDecimal(ticker.getVolume() == null ? "0" : ticker.getVolume()))
                .timestamp(new Date(ticker.getTime()))
                .build();
    }

    public static List<Ticker> adaptTickers(List<XTTicker> tickers) {
        return tickers.stream().map(t -> adaptTicker(t, adaptInstrumentId(t.getSymbol()))).collect(Collectors.toList());
    }


    public static ExchangeMetaData adaptToExchangeMetaData(List<XTSymbol> symbols,
                                                           List<XTCurrencyInfo> currencyInfos,
                                                           List<XTCurrencyWalletInfo> walletSupportCurrencys) {
        Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
        Map<Instrument, InstrumentMetaData> instrumentMetaData = new HashMap<>();

        for (XTSymbol symbol : symbols) {

            if (!"ONLINE".equals(symbol.getState())) {
                continue;
            }

            Instrument pair = adaptInstrumentId(symbol.getSymbol());

            instrumentMetaData.put(pair, new InstrumentMetaData.Builder()

                    .build());

        }

        return new ExchangeMetaData(null, null, null, null, null);
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


    public static PlaceOrderRequest adaptOrder(LimitOrder order, boolean isLimit) {
        return PlaceOrderRequest.builder()
                                .symbol(adaptInstrument(order.getInstrument()))
                                .side(order.getType() == Order.OrderType.BID ? "BUY" : "SELL")
                                .clientOrderId(order.getUserReference())
                                .type(isLimit ? "LIMIT" : "MARKET")
                                //GTC,FOK,IOC,GTX
                                .timeInForce("GTC")
                                .bizType("SPOT")
                                .quantity(order.getOriginalAmount().toPlainString())
                                .price(order.getLimitPrice()
                                            .toString())
                                .build();
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


}
