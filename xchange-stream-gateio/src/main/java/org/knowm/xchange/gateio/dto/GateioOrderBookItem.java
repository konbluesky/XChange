package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.math.BigDecimal;

@JsonDeserialize(using = GateioOrderBookItem.OrderDeserializer.class)
public class GateioOrderBookItem {

  private final BigDecimal price;
  private final BigDecimal volume;

  public GateioOrderBookItem(BigDecimal price, BigDecimal volume) {
    this.price = price;
    this.volume = volume;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  static class OrderDeserializer extends JsonDeserializer<GateioOrderBookItem> {

    @Override
    public GateioOrderBookItem deserialize(JsonParser jsonParser, DeserializationContext ctxt)
        throws IOException {

      ObjectCodec oc = jsonParser.getCodec();
      JsonNode node = oc.readTree(jsonParser);
      if (node.isArray()) {
        BigDecimal price = new BigDecimal(node.path(0).asText());
        BigDecimal volume = new BigDecimal(node.path(1).asText());
        return new GateioOrderBookItem(price, volume);
      }

      return null;
    }
  }
}
