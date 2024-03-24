package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.okex.OkexAdapters;
import org.knowm.xchange.okex.dto.account.OkexWalletBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p> @Date : 2023/5/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class OkexStreamingAccountService implements StreamingAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(OkexStreamingAccountService.class);

    private final OkexStreamingPool service;

    private final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

    public OkexStreamingAccountService(OkexStreamingPool service) {
        this.service = service;
    }

    public Observable<Balance> getAllBalancesChanges() {
        return service.subscribeChannel(OkexStreamingService.ACCOUNT)
                      .filter(message -> message.has("data"))
                      .flatMap(jsonNode -> {
                          LOG.debug("getBalanceChanges: {}", jsonNode);
                          List<OkexWalletBalance> walletBalances =
                                  mapper.treeToValue(
                                          jsonNode.get("data"),
                                          mapper.getTypeFactory().constructCollectionType(List.class, OkexWalletBalance.class));
                          return Observable.fromIterable(OkexAdapters.adaptOkexBalances(walletBalances).balances());
                      });
    }
    @Override
    public Observable<Balance> getBalanceChanges(Currency currency, Object... args) {
        return service.subscribeChannel(OkexStreamingService.ACCOUNT,currency.getCurrencyCode())
                      .filter(message -> message.has("data"))
                      .flatMap(jsonNode -> {
                          LOG.debug("getBalanceChanges: {}", jsonNode);
                          List<OkexWalletBalance> walletBalances =
                                  mapper.treeToValue(
                                          jsonNode.get("data"),
                                          mapper.getTypeFactory().constructCollectionType(List.class, OkexWalletBalance.class));
                          return Observable.fromIterable(OkexAdapters.adaptOkexBalances(walletBalances).balances());
                      });
    }
}