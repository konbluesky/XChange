package org.knowm.xchange.mexc.dto.market;

import com.google.common.collect.Multimap;
import java.util.Map;
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

  private Multimap<Currency, MEXCNetwork> currencyNetwork;

  /**
   * Constructor
   *
   * @param instruments       Map of {@link Instrument} -> {@link InstrumentMetaData}
   * @param currency          Map of currency -> {@link CurrencyMetaData}
   * @param publicRateLimits
   * @param privateRateLimits
   * @param shareRateLimits
   */
  public MEXCExchangeMetaData(
      Map<Instrument, InstrumentMetaData> instruments,
      Map<Currency, CurrencyMetaData> currency,
      RateLimit[] publicRateLimits,
      RateLimit[] privateRateLimits, Boolean shareRateLimits) {
    super(instruments, currency, publicRateLimits, privateRateLimits, shareRateLimits);
  }

  public MEXCExchangeMetaData(Map<Instrument, InstrumentMetaData> instruments,
      Map<Currency, CurrencyMetaData> currency, RateLimit[] publicRateLimits,
      RateLimit[] privateRateLimits, Boolean shareRateLimits,
      Multimap<Currency, MEXCNetwork> networks) {
    super(instruments, currency, publicRateLimits, privateRateLimits, shareRateLimits);
    this.currencyNetwork = networks;
  }
}
