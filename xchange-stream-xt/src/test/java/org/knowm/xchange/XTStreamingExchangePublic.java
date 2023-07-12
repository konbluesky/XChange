package org.knowm.xchange;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTStreamingExchange;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
@Ignore
@Slf4j
public class XTStreamingExchangePublic {
    // Enter your authentication details here to run private endpoint tests
    private static final String API_KEY = "";
    private static final String SECRET_KEY = "";
    StreamingExchange streamingExchange;
    private Instrument instrument = new FuturesContract("ARB/USDT/SWAP");

    @Before
    public void setUp() throws IOException {
        ExchangeSpecification spec = new XTExchange().getDefaultExchangeSpecification();
        spec.setApiKey(API_KEY);
        spec.setSecretKey(SECRET_KEY);

        ExchangeSpecification defaultExchangeSpecification = new XTStreamingExchange().getDefaultExchangeSpecification();
        defaultExchangeSpecification.setApiKey(API_KEY);
        defaultExchangeSpecification.setSecretKey(SECRET_KEY);

        streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
        streamingExchange.connect().blockingAwait();
    }

    @Test
    public void testPrivate() throws InterruptedException {
        XTStreamingExchange xtStreamingExchange = (XTStreamingExchange) streamingExchange;
        instrument = new CurrencyPair("btc", "usdt");
        Disposable dis = xtStreamingExchange.getStreamingMarketDataService()
                                            .getOrderBook(instrument, 20)
                                            .subscribe(orderBook -> {
                                                log.info("sub: {}", orderBook.toString());
                                            });
        TimeUnit.SECONDS.sleep(30);

        dis.dispose();
    }

}
