package info.bitrich.xchangestream.kucoin;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.kucoin.dto.KucoinAccountBalanceChangesEvent;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KucoinStreamingAccountService implements StreamingAccountService {

  private static final Logger logger = LoggerFactory.getLogger(KucoinStreamingAccountService.class);

  private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

  private final KucoinStreamingService service;

  public KucoinStreamingAccountService(KucoinStreamingService service) {
    this.service = service;
  }

  @Override
  public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {
    return service
        .subscribeChannel("/account/balance")
        .doOnError(ex -> logger.warn("encountered error while subscribing to order changes", ex))
        .map(node -> mapper.treeToValue(node, KucoinAccountBalanceChangesEvent.class))
        .map(KucoinStreamingAdapters::adaptBalance);
//        .filter(order -> currency == null || currency.getCurrencyCode()
//            .equalsIgnoreCase(order.data.currency));
//    return getRawBalanceChanges(currency).map(KucoinStreamingAdapters::adaptBalance);
  }

  public Observable<KucoinAccountBalanceChangesEvent> getRawBalanceChanges(Currency currency) {
    return service
        .subscribeChannel("/account/balance")
        .doOnError(ex -> logger.warn("encountered error while subscribing to order changes", ex))
        .map(node -> mapper.treeToValue(node, KucoinAccountBalanceChangesEvent.class))
        .filter(order -> currency == null || currency.getCurrencyCode()
            .equalsIgnoreCase(order.data.currency));
  }
}
