package org.knowm.xchange.xt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.xt.dto.XTOrderBook;
import org.knowm.xchange.xt.dto.XTSendMessage;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTStreamingMarketDataService implements StreamingMarketDataService {

  private final XTStreamingService streamingService;

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  public XTStreamingMarketDataService(XTStreamingService streamingService) {
    this.streamingService = streamingService;
  }


  /**
   * args[0]可以传入depth的值，如：5, 10, 20, 50
   *
   * @param currencyPair Currency pair of the order book
   * @param args
   * @return
   */
  @Override
  public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
    String topic = "depth";
    String symbol =
        currencyPair.getBase().getCurrencyCode().toLowerCase() + "_" + currencyPair.getCounter()
            .getCurrencyCode()
            .toLowerCase();
    XTSendMessage sendMessage = XTSendMessage.createSubMessage();
    if (args == null || args.length == 0) {
      args = new Object[]{50};
    }
    sendMessage.putParam(topic, symbol, args[0]);

    return streamingService.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("data")).flatMap(jsonNode -> {
          XTOrderBook orderbook = mapper.treeToValue(jsonNode.get("data"), XTOrderBook.class);
          OrderBook orderBook = XTStreamingAdapters.adaptOrderBook(Lists.newArrayList(orderbook),
              currencyPair);
          return Observable.just(orderBook);
        });
  }

  @Override
  public Observable<OrderBook> getOrderBook(Instrument instrument, Object... args) {
    return getOrderBook(new CurrencyPair(instrument.getBase(), instrument.getCounter()), args);
  }

  @Override
  public Observable<Ticker> getTicker(Instrument instrument, Object... args) {
    String topic = "ticker";
    String symbol =
        instrument.getBase().getCurrencyCode().toLowerCase() + "_" + instrument.getCounter()
            .getCurrencyCode()
            .toLowerCase();
    XTSendMessage sendMessage = XTSendMessage.createSubMessage();
    sendMessage.putParam(topic, symbol);

    return streamingService.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("data")).flatMap(jsonNode -> {
          XTTicker ticker = mapper.treeToValue(jsonNode.get("data"), XTTicker.class);
          Ticker adaptTicker = XTAdapters.adaptTicker(ticker, instrument);
          return Observable.just(adaptTicker);
        });
  }

  @Override
  public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
    return getTicker((Instrument) currencyPair, args);
  }
}
