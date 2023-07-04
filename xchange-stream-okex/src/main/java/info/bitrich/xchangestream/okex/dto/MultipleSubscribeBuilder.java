package info.bitrich.xchangestream.okex.dto;

import com.google.common.collect.Lists;
import org.knowm.xchange.okex.dto.OkexInstType;

import static info.bitrich.xchangestream.okex.OkexStreamingService.*;

/**
 * <p> @Date : 2023/5/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MultipleSubscribeBuilder {

    public static final String MULTIPLE = "multiple";

    private OkexSubscribeMessage message;

    private MultipleSubscribeBuilder(String op) {
        message = new OkexSubscribeMessage(op, Lists.newArrayList());
    }

    public static MultipleSubscribeBuilder subscribe() {
        return new MultipleSubscribeBuilder("subscribe");
    }

    public static MultipleSubscribeBuilder unsubscribe() {
        return new MultipleSubscribeBuilder("unsubscribe");
    }

    public MultipleSubscribeBuilder addOrderBook5(String instId) {
        add(ORDERBOOK5, null, null, instId);
        return this;
    }

    public MultipleSubscribeBuilder addOrderBook(String instId) {
        add(ORDERBOOK, null, null, instId);
        return this;
    }

    public MultipleSubscribeBuilder addTrades(String instId) {
        add(TRADES, null, null, instId);
        return this;
    }

    public MultipleSubscribeBuilder addTickers(String instId) {
        add(TICKERS, null, null, instId);
        return this;
    }

    public MultipleSubscribeBuilder addUserTrades(String instId) {
        message.getArgs()
               .add(new OkexSubscribeMessage.SubscriptionTopic(USERORDERS, OkexInstType.ANY, null, instId));
        return this;
    }

    public MultipleSubscribeBuilder addFundingRate(String instId) {
        add(FUNDING_RATE, null, null, instId);
        return this;
    }

    public MultipleSubscribeBuilder addPositions(String instId) {
        add(POSITIONS, null, null, instId);
        return this;
    }

    public MultipleSubscribeBuilder addAccount(String ccy) {
        add(ACCOUNT, null, null, null, ccy);
        return this;
    }

    public MultipleSubscribeBuilder add(String channel, OkexInstType instType, String uly, String instId) {
        message.getArgs()
               .add(new OkexSubscribeMessage.SubscriptionTopic(channel, instType, uly, instId));
        return this;
    }

    public MultipleSubscribeBuilder add(String channel, OkexInstType instType, String uly, String instId, String ccy) {
        message.getArgs()
               .add(new OkexSubscribeMessage.SubscriptionTopic(channel, instType, uly, instId, ccy));
        return this;
    }

    public OkexSubscribeMessage build() {
        return message;
    }

    public static void main(String[] args) {
        OkexSubscribeMessage message = MultipleSubscribeBuilder.subscribe()
                                                               .addOrderBook5("BTC-USDT")
                                                               .addOrderBook5("ETH-USDT")
                                                               .addOrderBook5("ETH-BTC")
                                                               .addTrades("BTC-USDT")
                                                               .addTrades("ETH-USDT")
                                                               .addTrades("ETH-BTC")
                                                               .addTickers("BTC-USDT")
                                                               .addTickers("ETH-USDT")
                                                               .addTickers("ETH-BTC")
                                                               .addUserTrades("BTC-USDT")
                                                               .addUserTrades("ETH-USDT")
                                                               .addUserTrades("ETH-BTC")
                                                               .addFundingRate("BTC-USDT")
                                                               .addFundingRate("ETH-USDT")
                                                               .addFundingRate("ETH-BTC")
                                                               .addPositions("BTC-USDT")
                                                               .addPositions("ETH-USDT")
                                                               .addPositions("ETH-BTC")
                                                               .addAccount("BTC")
                                                               .addAccount("ETH")
                                                               .addAccount("USDT")
                                                               .build();
        System.out.println(message.getArgs());
    }

}
