package org.knowm.xchange.mexc;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.core.StreamingTradeService;
import io.reactivex.Completable;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;

/**
 * <p> @Date : 2023/7/11 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCStreamingExchange extends MEXCExchange implements StreamingExchange {

  public static final String API_BASE_PUBLIC_URI = "wss://wbs.mexc.com/ws";
  public static final String API_BASE_PRIVATE_URI = "wss://wbs.mexc.com/ws?listenKey=%s";
  public static final String IS_PRIVATE = "isPrivate";

  private MEXCStreamingService streamingService;
  private StreamingMarketDataService streamingMarketDataService;
  private StreamingTradeService streamingTradeService;
  private MEXCStreamingAccountService streamingAccountService;

  public MEXCStreamingExchange() {
  }

  @Override
  public Completable connect(ProductSubscription... args) {
    if (getDefaultExchangeSpecification().getExchangeSpecificParametersItem(IS_PRIVATE) == null) {
      this.streamingService = new MEXCStreamingService(API_BASE_PUBLIC_URI);
      MEXCStreamingPool mexcStreamingPool = new MEXCStreamingPool(3, 28, API_BASE_PUBLIC_URI);
      this.streamingMarketDataService = new MEXCStreamingMarketDataService(mexcStreamingPool);
      return mexcStreamingPool.initializeServices();
    } else {
      MEXCStreamingPool mexcStreamingPool = new MEXCStreamingPool(1, 28, API_BASE_PRIVATE_URI,
          this.getWsTokenService());
      this.streamingTradeService = new MEXCStreamingTradeService(mexcStreamingPool);
      this.streamingAccountService = new MEXCStreamingAccountService(mexcStreamingPool);
      return mexcStreamingPool.initializeServices();
    }
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
