package org.knowm.xchange.gateio;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.gateio.dto.GateioBalance;
import org.knowm.xchange.gateio.dto.GateioSendMessage;
import org.knowm.xchange.gateio.service.GateioHmacPostBodyDigest;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioStreamingAccountService implements StreamingAccountService {

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();
  private GateioNewStreamingService streamingService;

  public GateioStreamingAccountService(GateioNewStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  @Override
  public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {
    String topicName = "spot.balances";

    GateioSendMessage sendMessage = GateioSendMessage.createSubMessage();
    sendMessage.setChannel(topicName);
    sendMessage.putParam(args);
    GateioHmacPostBodyDigest hmacPostBodyDigest = streamingService.getHmacPostBodyDigest();
    sendMessage.setAuth(hmacPostBodyDigest);

//    log.info("has Auth message:{}", sendMessage);

    return streamingService.subscribeChannel(topicName, sendMessage)
        .filter(message -> message.has("result"))
        .filter(jsonNode -> !jsonNode.path("result").has("status")).flatMap(jsonNode -> {
          List<GateioBalance> balances =
              mapper.treeToValue(
                  jsonNode.get("result"),
                  mapper
                      .getTypeFactory()
                      .constructCollectionType(List.class, GateioBalance.class));
          return Observable.fromIterable(balances).map(GateioStreamingAdapters::adaptBalance);
        });
  }
}
