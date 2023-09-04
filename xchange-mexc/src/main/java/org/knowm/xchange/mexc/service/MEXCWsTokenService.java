package org.knowm.xchange.mexc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.mexc.dto.ws.WebSocketToken;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCWsTokenService extends MEXCBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public MEXCWsTokenService(Exchange exchange) {
    super(exchange);
  }

  public List<String> getWsTokens() throws IOException {

    JsonNode data = mexc.getWsTokens(apiKey, nonceFactory, signatureCreator);
    JsonNode listenKeys = data.get("listenKey");
    ObjectMapper mapper = new ObjectMapper();
    List<String> keys = mapper.treeToValue(listenKeys, mapper.getTypeFactory()
        .constructCollectionType(List.class, String.class));
    return keys;

  }


  public void deleteWsToken(String listenKey) throws IOException {
    mexc.deleteWsToken(
        apiKey,
        nonceFactory,
        signatureCreator,
        listenKey
    );
  }

  public WebSocketToken getWsToken() throws IOException {
    return mexc.getWsToken(
        apiKey,
        nonceFactory,
        signatureCreator
    );
  }

  public WebSocketToken putWsToken(String listenKey) throws IOException {
    return mexc.putWsToken(
        apiKey,
        nonceFactory,
        signatureCreator,
        listenKey
    );
  }

  public JsonNode time() throws IOException {
    return mexc.time().getData();
  }
}
