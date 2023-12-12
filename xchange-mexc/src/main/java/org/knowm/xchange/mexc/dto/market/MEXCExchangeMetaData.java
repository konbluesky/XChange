package org.knowm.xchange.mexc.dto.market;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.meta.RateLimit;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;

/**
 * <p> @Date : 2023/12/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class MEXCExchangeMetaData extends ExchangeMetaData {

  private Table<Currency, String, MEXCNetwork> currencyNetwork = HashBasedTable.create();
  private MEXCCurrencyNetworkMapping currencyNetworkMapping;

  /**
   * Constructor
   *
   * @param instruments       Map of {@link Instrument} -> {@link InstrumentMetaData}
   * @param currencies        Map of currencies -> {@link CurrencyMetaData}
   * @param publicRateLimits
   * @param privateRateLimits
   * @param shareRateLimits
   */
  public MEXCExchangeMetaData(
      Map<Instrument, InstrumentMetaData> instruments,
      Map<Currency, CurrencyMetaData> currencies,
      RateLimit[] publicRateLimits,
      RateLimit[] privateRateLimits, Boolean shareRateLimits) {
    super(instruments, currencies, publicRateLimits, privateRateLimits, shareRateLimits);
  }

  public MEXCExchangeMetaData(Map<Instrument, InstrumentMetaData> instruments,
      Map<Currency, CurrencyMetaData> currency, RateLimit[] publicRateLimits,
      RateLimit[] privateRateLimits, Boolean shareRateLimits,
      Table<Currency, String, MEXCNetwork> networks) {
    super(instruments, currency, publicRateLimits, privateRateLimits, shareRateLimits);
    this.currencyNetwork = networks;
    this.currencyNetworkMapping = new MEXCCurrencyNetworkMapping(networks);
  }

  public Collection<Currency> getCurrenciesByNetwork(String network) {
    Set<Currency> collect = currencyNetworkMapping.getCurrenciesForNetwork(
        network).stream().map(a -> Currency.getInstance(a)).collect(Collectors.toSet());
    // 返回在currenciesForNetwork中存在的币种
    SetView<Currency> intersection = Sets.intersection(getCurrencies().keySet(), collect);
    return intersection.immutableCopy();
  }

}