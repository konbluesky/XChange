package org.knowm.xchange.hashkey;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.hashkey.dto.HashKeyOutboundAccountInfo;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyStreamingAccountService implements StreamingAccountService {

  private final HashKeyStreamingService streamingService;
  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  public HashKeyStreamingAccountService(HashKeyStreamingService streamingService) {
    this.streamingService = streamingService;
  }

  /**
   * [ { "e": "outboundAccountInfo", "E": "1699932094871", "T": true, "W": true, "D": true, "B": [ {
   * "a": "HKD", "f": "324.00494", "l": "0" } ] } ]
   *
   * @param currency Currency to monitor.
   * @param args
   * @return
   */
  @Override
  public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {
    String topicName = "outboundAccountInfo";
    return streamingService.subscribeChannel(topicName)
        .filter(message -> message.has("e"))
        .flatMap(
            jsonNode -> {
//              log.info("getBalanceChanges:{}", jsonNode);
              HashKeyOutboundAccountInfo info = mapper.treeToValue(jsonNode,
                  HashKeyOutboundAccountInfo.class);
              return Observable.fromIterable(HashKeyStreamingAdapters.adaptBalance(info));
            });
  }
}
