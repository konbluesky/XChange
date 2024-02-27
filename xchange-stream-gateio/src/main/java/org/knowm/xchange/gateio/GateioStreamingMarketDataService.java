package org.knowm.xchange.gateio;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.gateio.dto.ChannelNameHelper;
import org.knowm.xchange.gateio.dto.GateioOrderBook;
import org.knowm.xchange.gateio.dto.GateioSendMessage;
import org.knowm.xchange.gateio.dto.GateioTicker;
import org.knowm.xchange.gateio.dto.GateioTradeDetail;
import org.knowm.xchange.instrument.Instrument;

/**
 * Author: Max Gao (gaamox@tutanota.com) Created: 05-05-2021
 */
@Slf4j
public class GateioStreamingMarketDataService implements StreamingMarketDataService {

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();
  private final GateioNewStreamingService streamingService;

  public GateioStreamingMarketDataService(GateioNewStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  /**
   * Uses the limited-level snapshot method:
   * https://www.gate.io/docs/apiv4/ws/index.html#limited-level-full-order-book-snapshot
   *
   * @param currencyPair Currency pair of the order book
   * @param args         Optional maxDepth, Optional msgInterval
   * @return
   */
  @Override
  public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
    //["BTC_USDT", "5", "100ms"]
    String topicName = GateioConst.SPOT_ORDER_BOOK;
    String channelName = ChannelNameHelper.getChannelName(topicName,
        GateioUtils.toPairString(currencyPair));

    GateioSendMessage sendMessage = GateioSendMessage.createSubMessage();
    sendMessage.setChannel(topicName);
    sendMessage.putParam(args);

    return streamingService.subscribeChannel(channelName, sendMessage)
        .filter(message -> message.has("result"))
        .filter(jsonNode -> !jsonNode.path("result").has("status")).flatMap(jsonNode -> {
          GateioOrderBook orderBookRaw = mapper.treeToValue(jsonNode.get("result"),
              GateioOrderBook.class);
          Instrument s = GateioUtils.toCurrencyPair(orderBookRaw.getSymbol());
          OrderBook orderBook = GateioStreamingAdapters.adaptOrderBook(orderBookRaw, s);
          return Observable.just(orderBook);
        });
  }

  @Override
  public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
    String topicName = GateioConst.SPOT_TICKERS;
    String channelName = ChannelNameHelper.getChannelName(topicName,
        GateioUtils.toPairString(currencyPair));

    GateioSendMessage sendMessage = GateioSendMessage.createSubMessage();
    sendMessage.setChannel(topicName);
//    sendMessage.putParam(args);
    sendMessage.putParam(GateioUtils.toPairString(currencyPair).toUpperCase());

    return streamingService.subscribeChannel(channelName, sendMessage)
        .filter(message -> message.has("result"))
        .filter(jsonNode -> !jsonNode.path("result").has("status")).flatMap(jsonNode -> {
          GateioTicker gateioTicker = mapper.treeToValue(jsonNode.get("result"),
              GateioTicker.class);
          Ticker ticker = GateioStreamingAdapters.adaptTicker(gateioTicker);
          return Observable.just(ticker);
        });
  }

  @Override
  public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
    String topicName = GateioConst.SPOT_TRADES;
    String channelName = ChannelNameHelper.getChannelName(topicName,
        GateioUtils.toPairString(currencyPair));

    GateioSendMessage sendMessage = GateioSendMessage.createSubMessage();
    sendMessage.setChannel(topicName);
    sendMessage.putParam(args);

    return streamingService.subscribeChannel(channelName, sendMessage)
        .filter(message -> message.has("result"))
        .filter(jsonNode -> !jsonNode.path("result").has("status")).flatMap(jsonNode -> {
          GateioTradeDetail tradeDetail = mapper.treeToValue(jsonNode.get("result"),
              GateioTradeDetail.class);
          Trade trade = GateioStreamingAdapters.adaptTrade(tradeDetail);
          return Observable.just(trade);
        });
  }
}
