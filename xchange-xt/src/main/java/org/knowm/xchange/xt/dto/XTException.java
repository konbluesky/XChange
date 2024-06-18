package org.knowm.xchange.xt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTException extends HttpStatusExceptionSupport {

  private final String message;
  private final int code;

  public XTException(@JsonProperty("mc") String message, @JsonProperty("rc") int code) {
    super(message);
    this.message = message;
    this.code = code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return code + ":" + message;
  }
}
