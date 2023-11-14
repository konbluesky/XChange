package org.knowm.xchange.hashkey;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import info.bitrich.xchangestream.service.exception.NotConnectedException;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.hashkey.dto.HashKeySendMessage;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyStreamingService extends JsonNettyStreamingService {

  private final Observable<Long> pingPongSrc = Observable.interval(10, 15, TimeUnit.SECONDS);
  private Disposable pingPongSubscription;
  /**
   * hashKey 的private频道，订阅后，会自动推送结果回来，不需要再次订阅
   * https://hashkeypro-apidoc.readme.io/reference/websocket-api-1
   */
  private final Set<String> silentSubscribedChannels = Sets.newHashSet("outboundAccountInfo",
      "executionReport", "ticketInfo");

  private String listenKey;

  public HashKeyStreamingService(String apiUrl) {
    super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 20);
  }

  public HashKeyStreamingService(String apiUrl, String listenKey) {
    super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 20);
    this.listenKey = listenKey;
  }

  @Override
  public Observable<JsonNode> subscribeChannel(String channelName, Object... args) {
    if (silentSubscribedChannels.contains(channelName)) {
      final String channelId = getSubscriptionUniqueId(channelName, args);
      return Observable.<JsonNode>create(
              e -> {
                if (!isSocketOpen()) {
                  e.onError(new NotConnectedException());
                }
                channels.computeIfAbsent(
                    channelId,
                    cid -> new Subscription(e, channelName, args)
                );
              })
          .doOnDispose(() -> {
            if (channels.remove(channelId) != null) {
              try {
                sendMessage(getUnsubscribeMessage(channelId));
              } catch (IOException e) {
                log.debug("Failed to unsubscribe channel: {} {}", channelId, e.toString());
              } catch (Exception e) {
                log.warn("Failed to unsubscribe channel: {}", channelId, e);
              }
            }
          })
          .share();
    }
    return super.subscribeChannel(channelName, args);
  }

  @Override
  public Completable connect() {
    Completable conn = super.connect();
    return conn.andThen((CompletableSource) observer -> {
      try {
        if (pingPongSubscription != null && !pingPongSubscription.isDisposed()) {
          pingPongSubscription.dispose();
        }
        pingPongSubscription = pingPongSrc.subscribe(o -> this.sendMessage(
            HashKeySendMessage.createPingMessage()));
        observer.onComplete();
      } catch (Exception e) {
        observer.onError(e);
      }
    });
  }

  private boolean needGetToken() {
    return this.uri.getRawPath().contains("api/v1/ws");
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
    /* private 频道时 直接推送结果回来，字段放在e中 */
    if (message.has("e")) {
      return message.get("e").asText();
    }
    if (message.has("symbol") && message.has("topic")) {
      channelName = message.get("topic").asText() + "-" + message.get("symbol").asText();
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
   * @param args        args[0]存放消息对象HashKeySendMessage
   * @return
   * @throws IOException
   */
  @Override
  public String getSubscribeMessage(String channelName, Object... args) throws IOException {
    HashKeySendMessage sendMessages = (HashKeySendMessage) args[0];
    if (needGetToken()) {
      sendMessages.setListenKey(listenKey);
    }
    return sendMessages.toString();
  }

  @Override
  public String getUnsubscribeMessage(String channelName, Object... args) throws IOException {
    HashKeySendMessage sendMessage = HashKeySendMessage.createUnSubMessage();
//        sendMessage.putParam(channelName, args);
    return sendMessage.toString();
  }

  public void pingPongDisconnectIfConnected() {
    if (pingPongSubscription != null && !pingPongSubscription.isDisposed()) {
      pingPongSubscription.dispose();
    }
  }

}
