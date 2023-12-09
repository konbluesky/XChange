package org.knowm.xchange.mexc.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.mexc.MEXCExchange;
import org.knowm.xchange.mexc.MEXCResilience;
import org.knowm.xchange.mexc.dto.OrderEnum;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderByInstrumentAndIdParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamInstrument;

@Slf4j
public class MEXCTradeServiceTest extends BaseWiremockTest {

  @Test
  public void testGetMEXCOrder() throws IOException {
    MEXCExchange mexcExchange = (MEXCExchange) createExchange();
    MEXCTradeService mexcAccountService = new MEXCTradeService(mexcExchange, MEXCResilience.createRegistries());

    String orderDetails = "{\n" +
        "    \"code\": 200,\n" +
        "    \"data\": [\n" +
        "        {\n" +
        "            \"id\": \"504feca6ba6349e39c82262caf0be3f4\",\n" +
        "            \"symbol\": \"MX_ETH\",\n" +
        "            \"price\": \"0.000901\",\n" +
        "            \"quantity\": \"300000\",\n" +
        "            \"state\": \"NEW\",\n" +
        "            \"type\": \"BID\",\n" +
        "            \"deal_quantity\": \"0\",\n" +
        "            \"deal_amount\": \"0\",\n" +
        "            \"create_time\": 1573117266000\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"72872b6ae721480ca4fe0f265d29dfee\",\n" +
        "            \"symbol\": \"MX_ETH\",\n" +
        "            \"price\": \"0.000907\",\n" +
        "            \"quantity\": \"300000\",\n" +
        "            \"state\": \"FILLED\",\n" +
        "            \"type\": \"ASK\",\n" +
        "            \"deal_quantity\": \"0.5\",\n" +
        "            \"deal_amount\": \"0.25\",\n" +
        "            \"create_time\": 1573117267000\n" +
        "        }\n" +
        "    ]\n" +
        "}";

    stubFor(
        get(urlPathEqualTo("/open/api/v2/order/query"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(orderDetails)
            )
    );

//    Collection<Order> orders = mexcAccountService.getOrder("504feca6ba6349e39c82262caf0be3f4",
//        "72872b6ae721480ca4fe0f265d29dfee");
//    assertThat(orders.size()).isEqualTo(2);
//
//    Order order = (Order) orders.toArray()[0];
//    assertThat(order.getType()).isEqualTo(Order.OrderType.BID);
//    assertThat(order.getInstrument()).isEqualTo(new CurrencyPair("MX", "ETH"));
//    assertThat(order.getAveragePrice()).isEqualTo(new BigDecimal("0"));
//    assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.NEW);
//    assertThat(order.getOriginalAmount()).isEqualTo(new BigDecimal("300000"));
//    assertThat(order.getCumulativeAmount()).isEqualTo(new BigDecimal("0"));
//
//    order = (Order) orders.toArray()[1];
//    assertThat(order.getType()).isEqualTo(Order.OrderType.ASK);
//    assertThat(order.getInstrument()).isEqualTo(new CurrencyPair("MX", "ETH"));
//    assertThat(order.getAveragePrice()).isEqualTo(new BigDecimal("0.50"));
//    assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.FILLED);
//    assertThat(order.getOriginalAmount()).isEqualTo(new BigDecimal("300000"));
//    assertThat(order.getCumulativeAmount()).isEqualTo(new BigDecimal("0.5"));

  }


  @Test
  public void testPlaceMEXCOrder() throws IOException {
    MEXCExchange mexcExchange = (MEXCExchange) createExchange();
    MEXCTradeService mexcTradeService = new MEXCTradeService(mexcExchange, MEXCResilience.createRegistries());

    String orderPlacementResponse = "{\n" +
        "    \"code\": 200,\n" +
        "    \"data\": \"c8663a12a2fc457fbfdd55307b463495\"\n" +
        "}";

    stubFor(
        post(urlPathEqualTo("/open/api/v2/order/place"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(orderPlacementResponse)
            )
    );

    String orderId = mexcTradeService.placeLimitOrder(
        new LimitOrder(
            Order.OrderType.ASK,
            new BigDecimal("300"),
            new CurrencyPair("COIN", "USDT"),
            null, null, new BigDecimal("0.25")
        )
    );

    assertThat(orderId).isEqualTo("c8663a12a2fc457fbfdd55307b463495");

  }

  @Test
  public void testPlaceLimitOrder() throws IOException {
    Exchange mexcExchange = createRawExchange();
    TradeService tradeService = mexcExchange.getTradeService();
    //BENQIUSDT
    CurrencyPair instrument = new CurrencyPair("DEXE", "USDT");
    LimitOrder limitOrder = new LimitOrder.Builder(OrderType.BID, instrument)
        .limitPrice(new BigDecimal("2.21"))
        .originalAmount(new BigDecimal("3")).build();

    String s = tradeService.placeLimitOrder(limitOrder);
    log.info("place Order : {} ", s);

    Collection<Order> orders = tradeService.getOrder(
        new DefaultQueryOrderParamInstrument(instrument, s));
    log.info("Query order size:{}", orders.size());
    log.info("order details:{}", orders.stream().findFirst());

    boolean b = tradeService.cancelOrder(
        new DefaultCancelOrderByInstrumentAndIdParams(instrument, s));
    log.info("cancel result:{}", b);

  }

  @Test
  public void testPlaceLimitOrderForLimitMaker() throws IOException {
    Exchange mexcExchange = createRawExchange();
    TradeService tradeService = mexcExchange.getTradeService();
    //BENQIUSDT
    CurrencyPair instrument = new CurrencyPair("OOE", "USDT");
    LimitOrder limitOrder = new LimitOrder.Builder(OrderType.ASK, instrument)
        .limitPrice(new BigDecimal("0.01988"))
        .originalAmount(new BigDecimal("1231.353")).build();
    // limitOrder.addOrderFlag(OrderEnum.OrderType.LIMIT_MAKER);

    String s = tradeService.placeLimitOrder(limitOrder);
    log.info("place Order : {} ", s);
    //
    // Collection<Order> orders = tradeService.getOrder(
    //     new DefaultQueryOrderParamInstrument(instrument, s));
    // log.info("Query order size:{}", orders.size());
    // log.info("order details:{}", orders.stream().findFirst());
    //
    // boolean b = tradeService.cancelOrder(
    //     new DefaultCancelOrderByInstrumentAndIdParams(instrument, s));
    // log.info("cancel result:{}", b);

  }


  @Test
  public void testGetOrderBy()throws IOException{

    Exchange mexcExchange = createRawExchange();
    TradeService tradeService = mexcExchange.getTradeService();
    //BENQIUSDT
    CurrencyPair instrument = new CurrencyPair("OOE", "USDT");

    String s="C01__361365552591089665";
    Collection<Order> orders = tradeService.getOrder(
        new DefaultQueryOrderParamInstrument(instrument, s));
    log.info("Query order size:{}", orders.size());
    log.info("order details:{}", orders.stream().findFirst());

  }


  @Test
  public void testMarketOrder() throws IOException {
    Exchange mexcExchange = createRawExchange();
    TradeService tradeService = mexcExchange.getTradeService();
    //BENQIUSDT
    CurrencyPair instrument = new CurrencyPair("DEXE", "USDT");
    MarketOrder  marketOrder   = new MarketOrder.Builder(OrderType.BID, instrument)
        .originalAmount(new BigDecimal("3")).build();

    String s = tradeService.placeMarketOrder(marketOrder);
    log.info("place Order : {} ", s);

    Collection<Order> orders = tradeService.getOrder(
        new DefaultQueryOrderParamInstrument(instrument, s));
    log.info("Query order size:{}", orders.size());
    log.info("order details:{}", orders.stream().findFirst());

    boolean b = tradeService.cancelOrder(
        new DefaultCancelOrderByInstrumentAndIdParams(instrument, s));
    log.info("cancel result:{}", b);

  }


}
