package org.knowm.xchange.xt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTResponse<V> {

  private final String code;
  private final String msg;
  private final V data;

  public XTResponse(
      @JsonProperty("rc") String code,
      @JsonProperty("mc") String msg,
      @JsonProperty("result") V data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public boolean isSuccess() {
    return "0".equals(code);
  }

  public V getData() {
    return data;
  }

  public String getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  @Override
  public String toString() {
    return "XTResponse{" + "code=" + code + ", msg=" + msg + '}';
  }
}
