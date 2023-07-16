package org.knowm.xchange.xt;

import info.bitrich.xchangestream.core.*;
import io.reactivex.Completable;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTStreamingExchange extends XTExchange implements StreamingExchange {

    public static final String API_BASE_PUBLIC_URI = "wss://stream.xt.com/public";
    public static final String API_BASE_PRIVATE_URI = "wss://stream.xt.com/private";
    public static final String LISTEN_KEY = "listenKey";

    private XTStreamingService streamingService;
    private XTStreamingService privateStreamingService;
    private XTStreamingMarketDataService streamingMarketDataService;
    private XTStreamingAccountService streamingAccountService;
    private XTStreamingTradeService streamingTradeService;

    public XTStreamingExchange() {
    }

    @Override
    public Completable connect(ProductSubscription... args) {
        this.streamingService = new XTStreamingService(API_BASE_PUBLIC_URI);
        this.streamingMarketDataService = new XTStreamingMarketDataService(streamingService);

        String listenKey = (String) exchangeSpecification.getExchangeSpecificParametersItem(LISTEN_KEY);
        this.privateStreamingService = new XTStreamingService(API_BASE_PRIVATE_URI, listenKey);
        this.streamingAccountService = new XTStreamingAccountService(this.privateStreamingService);
        this.streamingTradeService = new XTStreamingTradeService(privateStreamingService);

        return this.streamingService.connect().andThen(this.privateStreamingService.connect());
    }

    @Override
    public Completable disconnect() {
        streamingService.pingPongDisconnectIfConnected();
        return streamingService.disconnect();
    }

    @Override
    public boolean isAlive() {
        return streamingService != null && streamingService.isSocketOpen();
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {
        throw new NotYetImplementedForExchangeException("useCompressedMessage");
    }

    @Override
    public StreamingTradeService getStreamingTradeService() {
        return this.streamingTradeService;
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        return streamingMarketDataService;
    }


    @Override
    public StreamingAccountService getStreamingAccountService() {
        return streamingAccountService;
    }
}
