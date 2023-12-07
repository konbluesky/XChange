package org.knowm.xchange.mexc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.mexc.dto.MEXCBalance;
import org.knowm.xchange.mexc.dto.MEXCSendMessage;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCStreamingAccountService implements StreamingAccountService {

  // private final MEXCStreamingService streamingService;
  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  private final MEXCStreamingPool pool;

  public MEXCStreamingAccountService(MEXCStreamingPool pool) {
    this.pool = pool;
  }
  // public MEXCStreamingAccountService(MEXCStreamingService streamingService) {
  //   this.streamingService = streamingService;
  // }

  @Override
  public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {
    String topicName = "spot@private.account.v3.api";
    MEXCSendMessage sendMessage = MEXCSendMessage.createSubMessage();
    sendMessage.putParam(topicName);

    return pool.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("d"))
        .flatMap(
            jsonNode -> {
              long timestamp = jsonNode.get("t").asLong();
              JsonNode data = jsonNode.get("d");
              MEXCBalance balanceRaw = mapper.treeToValue(data, MEXCBalance.class);
              Balance balance = MEXCStreamingAdapters.adaptBalance(balanceRaw, timestamp);
              return Observable.just(balance);
            });
  }
}
