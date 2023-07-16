package org.knowm.xchange.xt;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.xt.dto.XTBalance;
import org.knowm.xchange.xt.dto.XTSendMessage;

/**
 * <p> @Date : 2023/7/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTStreamingAccountService implements StreamingAccountService {

    private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();
    private XTStreamingService privateStreamingService;

    public XTStreamingAccountService(XTStreamingService privateStreamingService) {
        this.privateStreamingService = privateStreamingService;
    }


    @Override
    public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {
        String topic = "balance";
        XTSendMessage sendMessage = XTSendMessage.createSubMessage();
        sendMessage.putParam(topic);

        return privateStreamingService.subscribeChannel(sendMessage.getChannelName(), sendMessage)
                                      .filter(message -> message.has("data")).flatMap(jsonNode -> {
                    XTBalance balance = mapper.treeToValue(jsonNode.get("data"), XTBalance.class);
                    Balance b = XTStreamingAdapters.adaptBalance(balance);
                    return Observable.just(b);
                });
    }
}
