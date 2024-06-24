package org.knowm.xchange;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.AuthUtils;
import org.knowm.xchange.xt.XTExchange;
import org.knowm.xchange.xt.XTStreamingExchange;
import org.knowm.xchange.xt.dto.ws.WebSocketToken;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
@Ignore
@Slf4j
public class XTStreamingExchangePrivate extends XTStreamingBase{
    // Enter your authentication details here to run private endpoint tests
    private final Instrument instrument = new FuturesContract("ARB/USDT/SWAP");

    @Test
    public void testBalanceChange() throws InterruptedException {
        XTStreamingExchange xtStreamingExchange = (XTStreamingExchange) streamingExchange;
        Disposable dis = xtStreamingExchange.getStreamingAccountService()
                                            .getBalanceChanges(null)
                                            .subscribe(balance -> {
                                                log.info("sub: {}", balance.toString());
                                            });
        TimeUnit.MINUTES.sleep(10);
        dis.dispose();
    }

    @Test
    public void testSelfOrder() throws InterruptedException{
        XTStreamingExchange xtStreamingExchange = (XTStreamingExchange) streamingExchange;
        Disposable dis = xtStreamingExchange.getStreamingTradeService()
            .getOrderChanges(null)
            .subscribe(orderBook -> {
                log.info("orderBook: {}", orderBook.toString());
            });
        TimeUnit.MINUTES.sleep(10);
        dis.dispose();
    }


}
