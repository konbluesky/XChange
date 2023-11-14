package org.knowm.xchange.hashkey.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;
import org.knowm.xchange.hashkey.dto.HashKeyDepthItem.DepthItemDeserializer;

/**
 * <p> @Date : 2023/11/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
@JsonDeserialize(using = DepthItemDeserializer.class)
public class HashKeyDepthItem {

  private final BigDecimal price;
  private final BigDecimal quantity;

  public HashKeyDepthItem(BigDecimal price, BigDecimal quantity) {
    this.price = price;
    this.quantity = quantity;
  }

  static class DepthItemDeserializer extends JsonDeserializer<HashKeyDepthItem> {

    @Override
    public HashKeyDepthItem deserialize(JsonParser jsonParser,
        DeserializationContext deserializationContext) throws IOException {
      ObjectCodec oc = jsonParser.getCodec();
      JsonNode node = oc.readTree(jsonParser);
      if (node.isArray()) {
        BigDecimal price = new BigDecimal(node.path(0).asText());
        BigDecimal volume = new BigDecimal(node.path(1).asText());
        return new HashKeyDepthItem(price, volume);
      }
      return null;
    }
  }
}
