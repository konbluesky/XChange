package org.knowm.xchange.hashkey;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import java.util.List;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.hashkey.dto.HashKeyDepthData;
import org.knowm.xchange.hashkey.dto.HashKeySendMessage;
import org.knowm.xchange.hashkey.dto.HashKeyTickerInfo;
import org.knowm.xchange.instrument.Instrument;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyStreamingMarketDataService implements StreamingMarketDataService {

  private final HashKeyStreamingService streamingService;
  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  public HashKeyStreamingMarketDataService(HashKeyStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  @Override
  public Observable<OrderBook> getOrderBook(Instrument instrument, Object... args) {
    String symbol = HashKeyAdapters.convertToHashKeySymbol(instrument.toString());
    HashKeySendMessage sendMessage = HashKeySendMessage.createSubDepthMessage(symbol);
    sendMessage.putStringParam("binary", Boolean.FALSE.toString());
    return streamingService.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("data"))
        .flatMap(
            jsonNode -> {
              List<HashKeyDepthData> orderBookRaw =
                  mapper.treeToValue(
                      jsonNode.get("data"),
                      mapper
                          .getTypeFactory()
                          .constructCollectionType(List.class, HashKeyDepthData.class));
              Instrument s = HashKeyAdapters.extractOneCurrencyPairs(
                  jsonNode.get("symbol").asText());
              OrderBook orderBook = HashKeyStreamingAdapters.adaptOrderBook(orderBookRaw.get(0), s);
              return Observable.just(orderBook);
            });
  }

  @Override
  public Observable<Ticker> getTicker(Instrument instrument, Object... args) {
    String topicName = "ticketInfo";

    return streamingService.subscribeChannel(topicName)
        .filter(message -> message.has("e"))
        .flatMap(
            jsonNode -> {
              List<HashKeyTickerInfo> tickers =
                  mapper.treeToValue(
                      jsonNode,
                      mapper
                          .getTypeFactory()
                          .constructCollectionType(List.class, HashKeyTickerInfo.class));
              return Observable.fromIterable(HashKeyStreamingAdapters.adaptTickers(tickers));
            });
  }

}
