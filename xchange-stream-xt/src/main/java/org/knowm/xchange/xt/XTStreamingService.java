package org.knowm.xchange.xt;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.xt.dto.XTSendMessage;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTStreamingService extends JsonNettyStreamingService {

    private final Observable<Long> pingPongSrc = Observable.interval(10, 15, TimeUnit.SECONDS);
    private Disposable pingPongSubscription;

    private String listenKey;

    public XTStreamingService(String apiUrl) {
        super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 20);
    }

    public XTStreamingService(String apiUrl, String listenKey) {
        super(apiUrl, Integer.MAX_VALUE, Duration.ofSeconds(5), Duration.ofSeconds(20), 20);
        this.listenKey = listenKey;
    }

    @Override
    public Completable connect() {
        Completable conn = super.connect();
        return conn.andThen((CompletableSource) observer -> {
            try {
                if (pingPongSubscription != null && !pingPongSubscription.isDisposed()) {
                    pingPongSubscription.dispose();
                }
                pingPongSubscription = pingPongSrc.subscribe(o -> this.sendMessage("ping"));
                observer.onComplete();
            } catch (Exception e) {
                observer.onError(e);
            }
        });
    }

    private boolean needGetToken() {
        return this.uri.getRawPath().contains("private");
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
        if (message.has("event")) {
            channelName = message.get("event").asText();
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
     * @param args        args[0]存放消息对象XTSendMessage
     * @return
     * @throws IOException
     */
    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        XTSendMessage sendMessages = (XTSendMessage) args[0];
        if (needGetToken()) {
            sendMessages.setListenKey(listenKey);
        }
        return sendMessages.toString();
    }

    @Override
    public String getUnsubscribeMessage(String channelName, Object... args) throws IOException {
        XTSendMessage sendMessage = XTSendMessage.createUnSubMessage();
        sendMessage.putParam(channelName, args);
        return sendMessage.toString();
    }

    public void pingPongDisconnectIfConnected() {
        if (pingPongSubscription != null && !pingPongSubscription.isDisposed()) {
            pingPongSubscription.dispose();
        }
    }

}
