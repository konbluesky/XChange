package org.knowm.xchange.mexc;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.core.StreamingTradeService;
import io.reactivex.Completable;
import java.io.IOException;
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
  private MEXCStreamingService privateStreamingService;
  private StreamingMarketDataService streamingMarketDataService;
  private StreamingTradeService streamingTradeService;
  private MEXCStreamingAccountService streamingAccountService;

  public MEXCStreamingExchange() {
  }

  @Override
  public Completable connect(ProductSubscription... args) {
    try {
      if (getDefaultExchangeSpecification().getExchangeSpecificParametersItem(IS_PRIVATE)==null) {
        this.streamingService = new MEXCStreamingService(API_BASE_PUBLIC_URI);
        this.streamingMarketDataService = new MEXCStreamingMarketDataService(streamingService);
        return this.streamingService.connect();
      }else {
        String listenKey = this.getWsTokenService().getWsToken().getListenKey();
        if (listenKey == null) {
          throw new RuntimeException("Mexc ListenKey is null");
        }
        String privateURL = String.format(API_BASE_PRIVATE_URI, listenKey);
        this.privateStreamingService = new MEXCStreamingService(privateURL);
        this.streamingTradeService = new MEXCStreamingTradeService(privateStreamingService);
        this.streamingAccountService = new MEXCStreamingAccountService(privateStreamingService);
        return this.privateStreamingService.connect();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
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
