package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingTradeService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.okex.OkexAdapters;
import org.knowm.xchange.okex.dto.trade.OkexOrderDetails;

import java.util.List;

import static info.bitrich.xchangestream.okex.OkexStreamingService.USERORDERS;

public class OkexStreamingTradeService implements StreamingTradeService {

    private final OkexStreamingPool service;
    private final ExchangeMetaData exchangeMetaData;
    private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

    public OkexStreamingTradeService(OkexStreamingPool service, ExchangeMetaData exchangeMetaData) {
        this.service = service;
        this.exchangeMetaData = exchangeMetaData;
    }
//
//    @Override
//    public Observable<UserTrade> getUserTrades(Instrument instrument, Object... args) {
//        String channelUniqueId = USERORDERS +OkexAdapters.adaptInstrument(instrument);
//
//        return service.subscribeChannel(channelUniqueId)
//                .filter(message-> message.has("data"))
//                .flatMap(jsonNode -> {
//                    List<OkexOrderDetails> okexOrderDetails = mapper.treeToValue(jsonNode.get("data"), mapper.getTypeFactory().constructCollectionType(List.class, OkexOrderDetails.class));
//                    return Observable.fromIterable(OkexAdapters.adaptUserTrades(okexOrderDetails, exchangeMetaData).getUserTrades());
//                }
//        );
//    }

    @Override
    public Observable<Order> getOrderChanges(Instrument instrument, Object... args) {
        String channelUniqueId = USERORDERS +OkexAdapters.adaptInstrument(instrument);

        return service.subscribeChannel(channelUniqueId)
                      .filter(message-> message.has("data"))
                      .flatMap(jsonNode -> {
                                  List<OkexOrderDetails> okexOrderDetails = mapper.treeToValue(jsonNode.get("data"), mapper.getTypeFactory().constructCollectionType(List.class, OkexOrderDetails.class));
                                  return Observable.fromIterable(OkexAdapters.adaptOpenOrders(okexOrderDetails, exchangeMetaData).getOpenOrders());
                              }
                      );
    }
}
