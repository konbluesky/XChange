package org.knowm.xchange.hashkey.dto;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
public class HashKeySendMessage {

  /**
   * { "method": "subscribe", "params": [ "{topic}@{arg},{arg}", "{topic}@{arg}" ], "id": "{id}"
   * //回调ID } 根据这个结构生成一个待构建动作的方法 构建方法要静态的，并且简单易用
   */
  private String event;
  private String topic;
  private String symbol;
  private ObjectNode params = JsonNodeFactory.instance.objectNode();
  //  private List<Param> params = new ArrayList<>();
  private String id;
  private String listenKey;

  public static HashKeySendMessage createSubMessage() {
    HashKeySendMessage sendMessage = new HashKeySendMessage();
    sendMessage.event = "sub";
    return sendMessage;
  }

  public static HashKeySendMessage createUnSubMessage() {
    HashKeySendMessage sendMessage = new HashKeySendMessage();
    sendMessage.event = "cancel";
    return sendMessage;
  }

  public static HashKeySendMessage createSubRealTimesMessage(String symbol) {
    HashKeySendMessage sendMessage = createSubMessage();
    sendMessage.topic = "realtimes";
    sendMessage.symbol = symbol;
    return sendMessage;
  }

  public static HashKeySendMessage createSubDepthMessage(String symbol) {
    HashKeySendMessage sendMessage = createSubMessage();
    sendMessage.topic = "depth";
    sendMessage.symbol = symbol;
    return sendMessage;
  }

  public static String createPingMessage() {
    ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
    jsonObject.put("ping", System.currentTimeMillis() / 1000);
    return jsonObject.toString();
  }

  /**
   * @return depth@btc_usdt,20
   */
  public String getChannelName() {
    return topic + "-" + symbol;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    stringBuilder.append("\"topic\":\"").append(topic).append("\",");
    stringBuilder.append("\"event\":\"").append(event).append("\",");
    if (symbol != null) {
      stringBuilder.append("\"symbol\":\"").append(symbol).append("\",");
    }
    stringBuilder.append("\"params\":").append(params.toString());
    if (id != null) {
      stringBuilder.append("\"id\":\"").append(id).append("\"");
    }
    if (listenKey != null) {
      stringBuilder.append(",\"listenKey\":\"").append(listenKey).append("\"");
    }
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  public void putStringParam(String key, String value) {
    params.put(key, value);
  }

//
//  @Data
//  @AllArgsConstructor
//  class Param {
//
//    private String key;
//    private String value;
//
//    @Override
//    public String toString() {
//      return "\"" + key + "\"" + ":" + "\"" + value + "\"";
//    }
//  }

}
