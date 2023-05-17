package org.knowm.xchange.okex.dto.marketdata;

import com.google.common.collect.Multimap;
import lombok.Getter;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.meta.RateLimit;
import org.knowm.xchange.instrument.Instrument;

import java.util.Map;

/**
 * <p> @Date : 2023/5/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class OkexExchangeMetaData extends ExchangeMetaData {

    private Multimap<Currency, OkexInstrument> okexInstruments;

    private Multimap<Currency, OkexCurrency> okexCurrencys;

    /**
     * Constructor
     *
     * @param instruments       Map of {@link Instrument} -> {@link InstrumentMetaData}
     * @param currency          Map of currency -> {@link CurrencyMetaData}
     * @param publicRateLimits
     * @param privateRateLimits
     * @param shareRateLimits
     */
    public OkexExchangeMetaData(Map<Instrument, InstrumentMetaData> instruments, Map<Currency, CurrencyMetaData> currency, RateLimit[] publicRateLimits,
                                RateLimit[] privateRateLimits, Boolean shareRateLimits) {
        super(instruments, currency, publicRateLimits, privateRateLimits, shareRateLimits);
    }

    public OkexExchangeMetaData(Map<Instrument, InstrumentMetaData> instruments, Map<Currency, CurrencyMetaData> currency, RateLimit[] publicRateLimits,
                                RateLimit[] privateRateLimits, Boolean shareRateLimits, Multimap<Currency, OkexInstrument> okexInstruments,
                                Multimap<Currency, OkexCurrency> okexCurrencys) {
        super(instruments, currency, publicRateLimits, privateRateLimits, shareRateLimits);
        this.okexInstruments = okexInstruments;
        this.okexCurrencys = okexCurrencys;
    }
}
