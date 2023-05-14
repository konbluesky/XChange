package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;

/**
 * wss://ws.okx.com:8443/ws/v5/business，wss://wsaws.okx.com:8443/ws/v5/business
 * 该service用来处理 这两个地址的订阅服务;
 * <p> @Date : 2023/5/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class OkexStreamingBusinessService {

    private final OkexStreamingService service;

    private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

    public OkexStreamingBusinessService(OkexStreamingService service) {
        this.service = service;
    }

    /**
     * https://www.okx.com/docs-v5/zh/#websocket-api-private-channel-positions-channel
     *
     * @return
     */
    public Observable<JsonNode> getBalanceAndPositionChanges() {
        return service.subscribeChannel(OkexStreamingService.BALANCE_AND_POSITION)
                      .filter(message -> message.has("data"))
                      .flatMap(jsonNode -> {
                          log.debug("getBalanceAndPositionChanges: {}", jsonNode);
                          return Observable.just(jsonNode);
                      });
    }

    /**
     * https://www.okx.com/docs-v5/zh/#websocket-api-private-channel-withdrawal-info-channel
     */
    public Observable<JsonNode> getWithdrawalInfoChanges() {
        return service.subscribeChannel(OkexStreamingService.WITHDRAWAL_INFO)
                      .filter(message -> message.has("data"))
                      .flatMap(jsonNode -> {
                          log.debug("getWithdrawalInfoChanges: {}", jsonNode);
                          return Observable.just(jsonNode);
                      });
    }

    /**
     * https://www.okx.com/docs-v5/zh/#websocket-api-private-channel-deposit-info-channel
     */
    public Observable<JsonNode> getDepositInfoChanges() {
        return service.subscribeChannel(OkexStreamingService.DEPOSIT_INFO)
                      .filter(message -> message.has("data"))
                      .flatMap(jsonNode -> {
                          log.debug("getDepositInfoChanges: {}", jsonNode);
                          return Observable.just(jsonNode);
                      });
    }

}
