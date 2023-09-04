package org.knowm.xchange.mexc.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.mexc.MEXCExchange;

import java.io.IOException;

public class BaseWiremockTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    public Exchange createExchange() throws IOException {
        Exchange exchange =
                ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(MEXCExchange.class);
        ExchangeSpecification specification = exchange.getDefaultExchangeSpecification();
        specification.setHost("localhost");
        specification.setSslUri("http://localhost:" + wireMockRule.port());
        specification.setPort(wireMockRule.port());
        specification.setApiKey("test_api_key");
        specification.setSecretKey("test_secret_key");
        specification.setShouldLoadRemoteMetaData(false);
        exchange.applySpecification(specification);
        return exchange;
    }


    public Exchange createRawExchange() throws  IOException{
        Exchange exchange =
            ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(MEXCExchange.class);
        ExchangeSpecification specification = exchange.getDefaultExchangeSpecification();
        specification.setHost("mexc.com");
        specification.setSslUri("https://api.mexc.com");
//    specification.setPort(443);
        specification.setApiKey("");
        specification.setSecretKey("");
        specification.setShouldLoadRemoteMetaData(false);
        exchange.applySpecification(specification);
        return exchange;
    }
}
