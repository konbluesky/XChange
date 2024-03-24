package info.bitrich.xchangestream.okex;

import info.bitrich.xchangestream.core.*;
import io.reactivex.Completable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.okex.OkexExchange;

public class OkexStreamingExchange extends OkexExchange implements StreamingExchange {

    // Production URIs
    public static final String WS_PUBLIC_CHANNEL_URI = "wss://ws.okx.com:8443/ws/v5/public";

    public static final String WS_PRIVATE_CHANNEL_URI = "wss://ws.okx.com:8443/ws/v5/private";

    public static final String AWS_WS_PUBLIC_CHANNEL_URI = "wss://wsaws.okx.com:8443/ws/v5/public";

    public static final String AWS_WS_PRIVATE_CHANNEL_URI = "wss://wsaws.okx.com:8443/ws/v5/private";

    // Recharge URIs
    public static final String WS_BUSINESS_CHANNEL_URI = "wss://ws.okx.com:8443/ws/v5/business";

    public static final String AWS_WS_BUSINESS_CHANNEL_URI = "wss://wsaws.okx.com:8443/ws/v5/business";

    // Demo(Sandbox) URIs
    public static final String SANDBOX_WS_PUBLIC_CHANNEL_URI = "wss://wspap.okx.com:8443/ws/v5/public?brokerId=9999";

    public static final String SANDBOX_WS_PRIVATE_CHANNEL_URI = "wss://wspap.okx.com:8443/ws/v5/private?brokerId=9999";

    private OkexStreamingService streamingService;
    private OkexStreamingPool streamingServicePool;

    private OkexStreamingMarketDataService streamingMarketDataService;

    private OkexStreamingTradeService streamingTradeService;

    //Custom PositionService implementation
    private OkexStreamingPositionService streamingPositionService;

    private OkexStreamingAccountService streamingAccountService;

    private OkexStreamingBusinessService streamingBusinessService;

    public OkexStreamingExchange() {
    }

    @Override
    public Completable connect(ProductSubscription... args) {
        this.streamingServicePool = new OkexStreamingPool(2, 19, getApiUrl(),
            getDefaultExchangeSpecification());

        this.streamingMarketDataService = new OkexStreamingMarketDataService(streamingServicePool);
        this.streamingTradeService = new OkexStreamingTradeService(streamingServicePool, exchangeMetaData);
        this.streamingPositionService = new OkexStreamingPositionService(streamingServicePool,exchangeMetaData);
        this.streamingAccountService = new OkexStreamingAccountService(streamingServicePool);

        // Use WS_BUSINESS_CHANNEL_URI to initialize the socket connection
        OkexStreamingService streamingService = new OkexStreamingService(WS_BUSINESS_CHANNEL_URI, this.exchangeSpecification);
        this.streamingBusinessService = new OkexStreamingBusinessService(streamingService);

        return streamingServicePool.initializeServices().andThen(streamingService.connect());
    }

    private String getApiUrl() {
        String apiUrl;
        ExchangeSpecification exchangeSpec = getExchangeSpecification();
        if (exchangeSpec.getOverrideWebsocketApiUri() != null) {
            return exchangeSpec.getOverrideWebsocketApiUri();
        }

        boolean userAws = Boolean.TRUE.equals(exchangeSpecification.getExchangeSpecificParametersItem(PARAM_USE_AWS));
        if (useSandbox()) {
            apiUrl = (this.exchangeSpecification.getApiKey() == null) ? SANDBOX_WS_PUBLIC_CHANNEL_URI : SANDBOX_WS_PRIVATE_CHANNEL_URI;
        } else {
            apiUrl = (this.exchangeSpecification.getApiKey() == null) ?
                    userAws ? AWS_WS_PUBLIC_CHANNEL_URI : WS_PUBLIC_CHANNEL_URI :
                    userAws ? AWS_WS_PRIVATE_CHANNEL_URI : WS_PRIVATE_CHANNEL_URI;
        }
        return apiUrl;
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
    public StreamingMarketDataService getStreamingMarketDataService() {
        return streamingMarketDataService;
    }

    @Override
    public StreamingTradeService getStreamingTradeService() {
        return streamingTradeService;
    }

    public OkexStreamingPositionService getStreamingPositionService() {
        return streamingPositionService;
    }

    @Override
    public StreamingAccountService getStreamingAccountService() {
        return streamingAccountService;
    }

    public OkexStreamingBusinessService getStreamingBusinessService() {
        return streamingBusinessService;
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {
        throw new NotYetImplementedForExchangeException("useCompressedMessage");
    }
}
