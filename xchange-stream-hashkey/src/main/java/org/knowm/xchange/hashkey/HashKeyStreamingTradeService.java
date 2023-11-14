package org.knowm.xchange.hashkey;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingTradeService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import java.util.List;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.hashkey.dto.HashKeyExecutionReport;
import org.knowm.xchange.instrument.Instrument;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyStreamingTradeService implements StreamingTradeService {


  private final HashKeyStreamingService streamingService;
  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  public HashKeyStreamingTradeService(HashKeyStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  @Override
  public Observable<Order> getOrderChanges(Instrument instrument, Object... args) {
    String topic = "executionReport";
    return streamingService.subscribeChannel(topic).filter(message -> message.has("e"))
        .flatMap(jsonNode -> {
          List<HashKeyExecutionReport> orders = mapper.treeToValue(jsonNode, mapper.getTypeFactory()
              .constructCollectionType(List.class, HashKeyExecutionReport.class));
          return Observable.fromIterable(HashKeyStreamingAdapters.adaptOrders(orders));
        });
  }
}
