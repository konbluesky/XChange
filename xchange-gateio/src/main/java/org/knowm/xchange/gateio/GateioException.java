package org.knowm.xchange.gateio;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

/**
 * <p> @Date : 2024/2/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioException extends HttpStatusExceptionSupport {

  private final String message;
  private final String label;

  public GateioException(@JsonProperty("message") String message,
      @JsonProperty("label") String label) {
    super(message);
    this.message = message;
    this.label = label;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return label + ":" + message;
  }
}
