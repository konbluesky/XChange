package org.knowm.xchange.mexc.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
public class MEXCSendMessage {

  /**
   * { "method": "subscribe", "params": [ "{topic}@{arg},{arg}", "{topic}@{arg}" ], "id": "{id}"
   * //回调ID } 根据这个结构生成一个待构建动作的方法 构建方法要静态的，并且简单易用
   */
  private String method;
  private List<Param> params = new ArrayList<>();
  private String id;
  private String listenKey;

  public static MEXCSendMessage createSubMessage() {
    MEXCSendMessage sendMessage = new MEXCSendMessage();
    sendMessage.method = "SUBSCRIPTION";
    return sendMessage;
  }

  public static MEXCSendMessage createUnSubMessage() {
    MEXCSendMessage sendMessage = new MEXCSendMessage();
    sendMessage.method = "UNSUBSCRIPTION";
    return sendMessage;
  }

  public static MEXCSendMessage createPingMessage() {
    MEXCSendMessage sendMessage = new MEXCSendMessage();
    sendMessage.method = "PING";
    return sendMessage;
  }

  /**
   * @return depth@btc_usdt,20
   */
  public String getChannelName() {
    return this.getParams().toString().replaceAll("\\[|\\]|\\\"| ", "");
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    stringBuilder.append("\"method\":\"").append(method).append("\",");
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

  public void putParam(String topic, Object... args) {
    String[] strings = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
    params.add(new Param(topic, strings));
  }

  public void putParam(String singleTopic) {
    params.add(new Param(singleTopic, null));
  }

  @Data
  @AllArgsConstructor
  class Param {

    private String topic;
    private String[] args;

    @Override
    public String toString() {
      if (args == null || args.length == 0) {
        return "\"" + topic + "\"";
      } else {
        return "\"" + topic + "@" + Arrays.toString(args).replaceAll("\\[|\\]|\\\"| ", "")
            .replaceAll(",", "@") + "\"";
      }
    }
  }

}
