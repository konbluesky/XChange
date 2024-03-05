package org.knowm.xchange.gateio;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.ToString;
import si.mazi.rescu.HttpStatusExceptionSupport;

/**
 * <p> @Date : 2024/2/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
public class GateioException extends HttpStatusExceptionSupport {

  @Setter
  private String message;
  private String label;

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

}
