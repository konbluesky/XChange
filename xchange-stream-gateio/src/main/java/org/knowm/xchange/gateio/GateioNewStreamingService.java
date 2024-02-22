package org.knowm.xchange.gateio;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import info.bitrich.xchangestream.service.netty.WebSocketClientCompressionAllowClientNoContextAndServerNoContextHandler;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandler;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.gateio.dto.ChannelNameHelper;
import org.knowm.xchange.gateio.dto.GateioSendMessage;
import org.knowm.xchange.gateio.service.GateioHmacPostBodyDigest;

/**
 * Author: Max Gao (gaamox@tutanota.com) Created: 05-05-2021
 */
@Slf4j
public class GateioNewStreamingService extends JsonNettyStreamingService {
  private static final String SUBSCRIBE = "subscribe";
  private static final String UNSUBSCRIBE = "unsubscribe";
  private static final String CHANNEL_NAME_DELIMITER = "-";
  private static final int MAX_DEPTH_DEFAULT = 5;
  private static final int UPDATE_INTERVAL_DEFAULT = 100;
  private final Observable<Long> pingPongSrc = Observable.interval(5, 15, TimeUnit.SECONDS);
//  private BiMap<String, Long> channelIdMap = HashBiMap.create();
  private Disposable pingPongSubscription;
  @Getter
  private GateioHmacPostBodyDigest hmacPostBodyDigest;
  public GateioNewStreamingService(String apiUrl) {
    super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 60);
  }

  /**
   * private 订阅的构造 优雅的通过BeforeConnectionHandler重置token
   *
   * @param apiUrl
   * @param apiKey
   * @param apiSecret
   */
  public GateioNewStreamingService(String apiUrl, String apiKey, String apiSecret) {
    super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 60);
    this.hmacPostBodyDigest = GateioHmacPostBodyDigest.createInstance(apiKey, apiSecret);
  }

  /**
   * beforeConnectionHandler.run(); return openConnection();
   *
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
        pingPongSubscription = pingPongSrc.subscribe(
            o -> this.sendMessage(GateioSendMessage.createPingMessage().toString()));
        observer.onComplete();
      } catch (Exception e) {
        observer.onError(e);
      }
    });
  }

  @Override
  public void messageHandler(String message) {
    log.debug("Received message: {}", message);
    JsonNode jsonNode;

    // Parse incoming message to JSON
    try {
      jsonNode = objectMapper.readTree(message);
      if(jsonNode.has("channel")){
        if (jsonNode.get("channel").asText().equalsIgnoreCase("spot.pong")) {
          return;
        }
      }
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
    return ChannelNameHelper.getChannelName(message);
  }

  /**
   * 包装message信息
   * <pre>
   *     NettyStreamingService.subscribeChannel.sendMessage(getSubscribeMessage(channelName, args));
   * </pre>
   *
   * @param channelName 拼接好后的channelName，
   * @param args        args[0]存放消息对象GateioSendMessage
   * @return
   * @throws IOException
   */
  @Override
  public String getSubscribeMessage(String channelName, Object... args) throws IOException {
    GateioSendMessage sendMessages = (GateioSendMessage) args[0];
    return sendMessages.toString();
  }

  @Override
  public String getUnsubscribeMessage(String channelName, Object... args) throws IOException {
    GateioSendMessage sendMessage = GateioSendMessage.createUnSubMessage();
    sendMessage.putParam(channelName, args);
    return sendMessage.toString();
  }

  public void pingPongDisconnectIfConnected() {
    if (pingPongSubscription != null && !pingPongSubscription.isDisposed()) {
      pingPongSubscription.dispose();
    }
  }

  @Override
  protected WebSocketClientExtensionHandler getWebSocketClientExtensionHandler() {
    return WebSocketClientCompressionAllowClientNoContextAndServerNoContextHandler.INSTANCE;
  }

}
