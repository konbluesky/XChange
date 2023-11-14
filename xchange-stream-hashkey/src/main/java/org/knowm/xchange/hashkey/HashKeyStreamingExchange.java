package org.knowm.xchange.hashkey;

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
public class HashKeyStreamingExchange extends HashKeyExchange implements StreamingExchange {

  public static final String API_BASE_PUBLIC_URI = "wss://stream-pro.hashkey.com/quote/ws/v1";
  public static final String API_BASE_PRIVATE_URI = "wss://stream-pro.hashkey.com/api/v1/ws/%s";

  private HashKeyStreamingService streamingService;
  private HashKeyStreamingService privateStreamingService;
  private StreamingMarketDataService streamingMarketDataService;
  private StreamingTradeService streamingTradeService;
  private HashKeyStreamingAccountService streamingAccountService;

  public HashKeyStreamingExchange() {
  }

  @Override
  public Completable connect(ProductSubscription... args) {
    try {
      String listenKey = this.getWsTokenService().getWsToken().getListenKey();
      if (listenKey == null) {
        throw new RuntimeException("HashKey ListenKey is null");
      }
      String privateURL = String.format(API_BASE_PRIVATE_URI, listenKey);
      this.streamingService = new HashKeyStreamingService(API_BASE_PUBLIC_URI);
      this.privateStreamingService = new HashKeyStreamingService(privateURL);
      this.streamingMarketDataService = new HashKeyStreamingMarketDataService(streamingService);
      this.streamingTradeService = new HashKeyStreamingTradeService(streamingService);
      this.streamingAccountService = new HashKeyStreamingAccountService(privateStreamingService);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

//    String listenKey = (String) exchangeSpecification.getExchangeSpecificParametersItem(LISTEN_KEY);
//    return this.streamingService.connect().andThen(this.privateStreamingService.connect());
    return this.streamingService.connect().andThen(privateStreamingService.connect());
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
