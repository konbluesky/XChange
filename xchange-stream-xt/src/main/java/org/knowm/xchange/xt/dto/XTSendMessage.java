package org.knowm.xchange.xt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Data
public class XTSendMessage {
    /**
     * {
     * "method": "subscribe",
     * "params": [
     * "{topic}@{arg},{arg}",
     * "{topic}@{arg}"
     * ],
     * "id": "{id}"    //回调ID
     * }
     * 根据这个结构生成一个待构建动作的方法
     * 构建方法要静态的，并且简单易用
     */
    private String method;
    private List<Param> params = new ArrayList<>();
    private String id;
    private String listenKey;

    public static XTSendMessage createSubMessage() {
        XTSendMessage sendMessage = new XTSendMessage();
        sendMessage.method = "subscribe";
        return sendMessage;
    }

    public static XTSendMessage createUnSubMessage() {
        XTSendMessage sendMessage = new XTSendMessage();
        sendMessage.method = "unsubscribe";
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
//        return "{" +
//                "\"method\":\"" + method + "\"," +
//                "\"params\":" + params.toString() + "," +
//                "\"id\":\"" + id + "\"" +
//                "}";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"method\":\"").append(method).append("\",");
        stringBuilder.append("\"params\":").append(params.toString()).append(",");
        stringBuilder.append("\"id\":\"").append(id).append("\"");
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
                return "\"" + topic + "@" + Arrays.toString(args).replaceAll("\\[|\\]|\\\"| ", "") + "\"";
            }
        }
    }

}
