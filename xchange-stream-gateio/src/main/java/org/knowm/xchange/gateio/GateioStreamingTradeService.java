package org.knowm.xchange.gateio;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingTradeService;
import io.reactivex.Observable;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.gateio.dto.ChannelNameHelper;
import org.knowm.xchange.gateio.dto.GateioOrderDetail;
import org.knowm.xchange.gateio.dto.GateioSendMessage;
import org.knowm.xchange.gateio.service.GateioHmacPostBodyDigest;

/**
 * <p> @Date : 2023/7/15 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioStreamingTradeService implements StreamingTradeService {

  private final ObjectMapper mapper = GateioUtils.getObjectMapper();
  private GateioNewStreamingService streamingService;

  public GateioStreamingTradeService(GateioNewStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  @Override
  public Observable<Order> getOrderChanges(CurrencyPair currencyPair, Object... args) {
    String topic = "spot.orders";
    String channelName = ChannelNameHelper.getChannelName(topic,
        GateioUtils.toPairString(currencyPair));

    GateioSendMessage sendMessage = GateioSendMessage.createSubMessage();
    sendMessage.setChannel(topic);
    sendMessage.putParam(GateioUtils.toPairString(currencyPair).toUpperCase());
    GateioHmacPostBodyDigest hmacPostBodyDigest = streamingService.getHmacPostBodyDigest();
    sendMessage.setAuth(hmacPostBodyDigest);

    return streamingService.subscribeChannel(channelName, sendMessage)
        .filter(message -> message.has("result")).flatMap(jsonNode -> {
          List<GateioOrderDetail> orders =
              mapper.treeToValue(
                  jsonNode.get("result"),
                  mapper
                      .getTypeFactory()
                      .constructCollectionType(List.class, GateioOrderDetail.class));
          return Observable.fromIterable(orders).map(GateioStreamingAdapters::adaptOrder);
        });
  }

}
