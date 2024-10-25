package info.bitrich.xchangestream.kucoin.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.math.BigDecimal;

@JsonDeserialize(using = KucoinOrderBookItem.OrderDeserializer.class)
public class KucoinOrderBookItem {

  private final BigDecimal price;
  private final BigDecimal volume;

  public KucoinOrderBookItem(BigDecimal price, BigDecimal volume) {
    this.price = price;
    this.volume = volume;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  static class OrderDeserializer extends JsonDeserializer<KucoinOrderBookItem> {

    @Override
    public KucoinOrderBookItem deserialize(JsonParser jsonParser, DeserializationContext ctxt)
        throws IOException {

      ObjectCodec oc = jsonParser.getCodec();
      JsonNode node = oc.readTree(jsonParser);
      if (node.isArray()) {
        BigDecimal price = new BigDecimal(node.path(0).asText());
        BigDecimal volume = new BigDecimal(node.path(1).asText());
        return new KucoinOrderBookItem(price, volume);
      }

      return null;
    }
  }
}
