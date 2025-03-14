package org.knowm.xchange.mexc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.dto.MEXCOrderBook;
import org.knowm.xchange.mexc.dto.MEXCSendMessage;
import org.knowm.xchange.mexc.dto.MEXCTicker;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCStreamingMarketDataService implements StreamingMarketDataService {

  // private final MEXCStreamingService streamingService;
  private final MEXCStreamingPool pool;
  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  public MEXCStreamingMarketDataService(MEXCStreamingPool pool) {
    this.pool = pool;
  }

  // public MEXCStreamingMarketDataService(MEXCStreamingService streamingService) {
  //   this.streamingService = streamingService;
  // }

  @Override
  public Observable<OrderBook> getOrderBook(Instrument instrument, Object... args) {
    //spot@public.limit.depth.v3.api@<symbol>@<level>
    String topicName = "spot@public.limit.depth.v3.api";
    String symbol = MEXCAdapters.convertToMEXCSymbol(instrument.toString());

    if (args == null || args.length == 0) {
      args = new Object[]{20};
    }

    MEXCSendMessage sendMessage = MEXCSendMessage.createSubMessage();
    sendMessage.putParam(topicName, symbol, args[0]);

    return pool.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("d"))
        .flatMap(
            jsonNode -> {
              MEXCOrderBook orderBookRaw = mapper.treeToValue(jsonNode.get("d"),
                  MEXCOrderBook.class);
              Instrument s = MEXCAdapters.extractOneCurrencyPairs(jsonNode.get("s").asText());
              OrderBook orderBook = MEXCStreamingAdapters.adaptOrderBook(orderBookRaw, s);
              return Observable.just(orderBook);
            });
  }

  @Override
  public Observable<Ticker> getTicker(Instrument instrument, Object... args) {
    String topicName = "spot@public.miniTicker.v3.api";
    String symbol = MEXCAdapters.convertToMEXCSymbol(instrument.toString());

    if (args == null || args.length == 0) {
      args = new Object[]{"UTC-8"};
    }
    MEXCSendMessage sendMessage = MEXCSendMessage.createSubMessage();
    sendMessage.putParam(topicName, symbol, args[0]);

    return pool.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("d"))
        .flatMap(
            jsonNode -> {
              JsonNode deal = jsonNode.get("d");
              long timestamp = deal.get("t").asLong();
              MEXCTicker ticker = mapper.treeToValue(deal, MEXCTicker.class);
              String asset = jsonNode.get("s").asText();
              Ticker t = MEXCStreamingAdapters.adaptTicker(ticker, asset, timestamp);
              return Observable.just(t);
            });
  }

}
