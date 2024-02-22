package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.knowm.xchange.gateio.service.GateioHmacPostBodyDigest;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
public class GateioSendMessage {

  private String event;
  private String channel;
  private long time;
  private Auth auth;
  private List<String> payload = new ArrayList<>();
  private Long id;

  private GateioSendMessage() {
    this.time = System.currentTimeMillis() / 1000;
    this.id = time;
  }

  public static GateioSendMessage createSubMessage() {
    GateioSendMessage sendMessage = new GateioSendMessage();
    sendMessage.event = "subscribe";
    return sendMessage;
  }

  public static GateioSendMessage createUnSubMessage() {
    GateioSendMessage sendMessage = new GateioSendMessage();
    sendMessage.event = "unsubscribe";
    return sendMessage;
  }

  public static GateioSendMessage createPingMessage() {
    GateioSendMessage sendMessage = new GateioSendMessage();
    sendMessage.channel = "spot.ping";
    return sendMessage;
  }

  public void setAuth(GateioHmacPostBodyDigest digest) {
        String s = digest.socketSign(channel,event,time);
    try {
      this.auth = new ObjectMapper().readValue(s, Auth.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @return depth@btc_usdt,20
   */
  @JsonIgnore
  public String getChannelName() {
    return Joiner.on("@").join(payload);
  }

  @Override
  public String toString() {
    ObjectMapper objectMapper = new ObjectMapper();
    // 设置字段为 null 时不序列化
    objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public void putParam(Object... args) {
    String[] strings = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
    payload.addAll(Lists.newArrayList(strings));
  }


}
