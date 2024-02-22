package org.knowm.xchange.gateio;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.core.StreamingTradeService;
import io.reactivex.Completable;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Author: Max Gao (gaamox@tutanota.com) Created: 05-05-2021 */
@Slf4j
public class GateioStreamingExchange extends GateioExchange implements StreamingExchange {
  private final String API_BASE_PUBLIC_URI = "wss://api.gateio.ws/ws/v4/";

  private GateioNewStreamingService streamingService;
  private StreamingMarketDataService streamingMarketDataService;
  private StreamingAccountService streamingAccountService;
  private StreamingTradeService streamingTradeService;

  public GateioStreamingExchange() {}

  @Override
  public Completable connect(ProductSubscription... args) {
    String apiKey = getExchangeSpecification().getApiKey();
    String secretKey = getExchangeSpecification().getSecretKey();
    this.streamingService = new GateioNewStreamingService(API_BASE_PUBLIC_URI, apiKey, secretKey);
    this.streamingAccountService = new GateioStreamingAccountService(streamingService);
    this.streamingTradeService = new GateioStreamingTradeService(streamingService);
    this.streamingMarketDataService = new GateioStreamingMarketDataService(streamingService);
    return streamingService.connect();
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
    streamingService.useCompressedMessages(compressedMessages);
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
