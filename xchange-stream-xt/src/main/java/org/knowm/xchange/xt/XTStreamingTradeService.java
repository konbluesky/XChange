package org.knowm.xchange.xt;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingTradeService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.xt.dto.XTOrderDetail;
import org.knowm.xchange.xt.dto.XTSendMessage;

/**
 * <p> @Date : 2023/7/15 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTStreamingTradeService implements StreamingTradeService {

    private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();
    private XTStreamingService privateStreamingService;

    public XTStreamingTradeService(XTStreamingService privateStreamingService) {
        this.privateStreamingService = privateStreamingService;
    }

    @Override
    public Observable<Order> getOrderChanges(CurrencyPair currencyPair, Object... args) {
        String topic = "order";
        XTSendMessage sendMessage = XTSendMessage.createSubMessage();
        sendMessage.putParam(topic);

        return privateStreamingService.subscribeChannel(sendMessage.getChannelName(),sendMessage)
                                      .filter(message -> message.has("data")).flatMap(jsonNode -> {
                    XTOrderDetail xtOrderDetail = mapper.treeToValue(jsonNode.get("data"), XTOrderDetail.class);
                    Order order = XTStreamingAdapters.adaptOrder(xtOrderDetail);
                    return Observable.just(order);
                });
    }

}
