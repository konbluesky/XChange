package org.knowm.xchange.mexc;

import static org.knowm.xchange.mexc.MEXCStreamingExchange.API_BASE_PRIVATE_URI;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.mexc.dto.MEXCSendMessage;
import org.knowm.xchange.mexc.service.MEXCWsTokenService;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCStreamingService extends JsonNettyStreamingService {

    private final Observable<Long> pingPongSrc = Observable.interval(5, 15, TimeUnit.SECONDS);
    private Disposable pingPongSubscription;
    private MEXCWsTokenService wsTokenService;

    public MEXCStreamingService(String apiUrl) {
        super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 60);
    }

    /**
     * private 订阅的构造
     * 优雅的通过BeforeConnectionHandler重置token
     * @param apiUrl
     * @param wsTokenService
     */
    public MEXCStreamingService(String apiUrl, MEXCWsTokenService wsTokenService) {
        super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 60);
        this.setBeforeConnectionHandler(()->{
          try {
              if(!isSocketOpen()) {
                  this.uri = URI.create(
                      String.format(API_BASE_PRIVATE_URI,
                          wsTokenService.getWsToken().getListenKey()));
              }
          } catch (IOException e) {
              log.error("get wsToken error:{}",e.getMessage());
          }
        });
    }

    /**
     *  beforeConnectionHandler.run();
     *     return openConnection();
     * @return
     */
    @Override
    public Completable connect() {
        Completable conn = super.connect();
        return conn.andThen((CompletableSource) observer -> {
            try {
                if (pingPongSubscription != null && !pingPongSubscription.isDisposed()) {
                    pingPongSubscription.dispose();
                }
                pingPongSubscription = pingPongSrc.subscribe(o -> this.sendMessage(MEXCSendMessage.createPingMessage().toString()));
                observer.onComplete();
            } catch (Exception e) {
                observer.onError(e);
            }
        });
    }

    public int getChannelSize(){
        return this.channels.size();
    }

    @Override
    public void messageHandler(String message) {
        log.debug("Received message: {}", message);
        JsonNode jsonNode;

        // Parse incoming message to JSON
        try {
            jsonNode = objectMapper.readTree(message);
        } catch (IOException e) {
            if ("pong".equals(message)) {
                // ping pong message
                return;
            }
            log.error("Error parsing incoming message to JSON: {}", message);
            return;
        }

        if (processArrayMessageSeparately() && jsonNode.isArray()) {
            // In case of array - handle every message separately.
            for (JsonNode node : jsonNode) {
                handleMessage(node);
            }
        } else {
            handleMessage(jsonNode);
        }
    }

    /**
     * 从服务端接收的消息中提取channel
     *
     * @param message
     * @return
     * @throws IOException
     */
    @Override
    protected String getChannelNameFromMessage(JsonNode message) throws IOException {
        String channelName = "";
        if (message.has("c")) {
            channelName = message.get("c").asText();
        }
        return channelName;
    }

    /**
     * 包装message信息
     * <pre>
     *     NettyStreamingService.subscribeChannel.sendMessage(getSubscribeMessage(channelName, args));
     * </pre>
     *
     * @param channelName 拼接好后的channelName，
     * @param args        args[0]存放消息对象MexcSendMessage
     * @return
     * @throws IOException
     */
    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        MEXCSendMessage sendMessages = (MEXCSendMessage) args[0];
        return sendMessages.toString();
    }

    @Override
    public String getUnsubscribeMessage(String channelName, Object... args) throws IOException {
        MEXCSendMessage sendMessage = MEXCSendMessage.createUnSubMessage();
        sendMessage.putParam(channelName, args);
        return sendMessage.toString();
    }

    public void pingPongDisconnectIfConnected() {
        if (pingPongSubscription != null && !pingPongSubscription.isDisposed()) {
            pingPongSubscription.dispose();
        }
    }

}
