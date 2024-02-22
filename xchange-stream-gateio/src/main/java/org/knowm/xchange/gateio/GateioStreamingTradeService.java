package org.knowm.xchange.gateio;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingTradeService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.gateio.dto.GateioOrderDetail;
import org.knowm.xchange.gateio.dto.GateioSendMessage;

/**
 * <p> @Date : 2023/7/15 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioStreamingTradeService implements StreamingTradeService {

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();
  private GateioNewStreamingService streamingService;

  public GateioStreamingTradeService(GateioNewStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  @Override
  public Observable<Order> getOrderChanges(CurrencyPair currencyPair, Object... args) {
    String topic = "spot.orders";
    GateioSendMessage sendMessage = GateioSendMessage.createSubMessage();
    sendMessage.setChannel(topic);
    sendMessage.putParam(args);

    return streamingService.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("result")).flatMap(jsonNode -> {
          GateioOrderDetail xtOrderDetail = mapper.treeToValue(jsonNode.get("result"),
              GateioOrderDetail.class);
          Order order = GateioStreamingAdapters.adaptOrder(xtOrderDetail);
          return Observable.just(order);
        });
  }

}
