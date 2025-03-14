package org.knowm.xchange.mexc;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingTradeService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.dto.MEXCOrderDetail;
import org.knowm.xchange.mexc.dto.MEXCSendMessage;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCStreamingTradeService implements StreamingTradeService {


  private final MEXCStreamingPool pool;
  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  public MEXCStreamingTradeService(MEXCStreamingPool pool) {
    this.pool = pool;
  }

  @Override
  public Observable<Order> getOrderChanges(Instrument instrument, Object... args) {
    String topic = "spot@private.orders.v3.api";
    MEXCSendMessage sendMessage = MEXCSendMessage.createSubMessage();
    sendMessage.putParam(topic);

    return pool.subscribeChannel(sendMessage.getChannelName(), sendMessage)
        .filter(message -> message.has("d"))
        .flatMap(jsonNode -> {
          MEXCOrderDetail orderDetail = mapper.treeToValue(jsonNode.get("d"),
              MEXCOrderDetail.class);
          String symbol = jsonNode.get("s").asText();
          Order order = MEXCStreamingAdapters.adaptOrder(orderDetail, symbol);
          return Observable.just(order);
        });
  }

  /**
   * 客户端调用兼容
   * @param currencyPair Currency pair of the order changes.
   * @param args
   * @return
   */
  @Override
  public Observable<Order> getOrderChanges(CurrencyPair currencyPair, Object... args) {
    return getOrderChanges(new Instrument() {
      @Override
      public Currency getBase() {
        return null;
      }

      @Override
      public Currency getCounter() {
        return null;
      }
    }, args);
  }
}
