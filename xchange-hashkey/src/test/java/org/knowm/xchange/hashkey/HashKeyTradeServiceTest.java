package org.knowm.xchange.hashkey;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.hashkey.dto.HashKeyOrderDetailResponse;
import org.knowm.xchange.hashkey.service.HashKeyTradeServiceRaw;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamInstrument;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeyTradeServiceTest extends HashKeyExchangeTest {

  @Test
  public void testCreateOrder() throws IOException {
    Exchange rawExchange = createRawExchange();
    LimitOrder limitOrder = new LimitOrder.Builder(OrderType.BID, ETH_HKD)
        .limitPrice(new BigDecimal("14690.58"))
        .originalAmount(new BigDecimal("0.01"))
        .build();
    String orderId = rawExchange.getTradeService().placeLimitOrder(limitOrder);

    log.info("{}", orderId);

  }

  @Test
  public void testQueryOrderById() throws IOException {
    Exchange rawExchange = createRawExchange();
    Collection<Order> order = rawExchange.getTradeService()
        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, "1549315863118644992"));
    log.info("order :{}", order.iterator().next());
  }

  @Test
  public void testCancelOrderById() throws IOException {
    Exchange rawExchange = createRawExchange();
    boolean cancelOrderStatus = rawExchange.getTradeService()
        .cancelOrder("1549315863118644992");
    log.info("cancelOrderStatus is cancel :{}", cancelOrderStatus);
  }


  /**
   * Create Limit Order body:
   * {"accountId":"1508027074815750912","symbol":"ETHHKD","symbolName":"ETHHKD","clientOrderId":"1699432689624","orderId":"1549348134320113408","transactTime":"1699432690360","price":"0","origQty":"0","executedQty":"0.0054","status":"FILLED","timeInForce":"IOC","type":"MARKET","side":"BUY","reqAmount":"80","concentration":""}
   * Query Limit order Body:
   * {"accountId":"1508027074815750912","exchangeId":"301","symbol":"ETHHKD","symbolName":"ETHHKD","clientOrderId":"1699432689624","orderId":"1549348134320113408","price":"0","origQty":"0","executedQty":"0.0054","cummulativeQuoteQty":"79.401924","cumulativeQuoteQty":"79.401924","avgPrice":"14704.06","status":"FILLED","timeInForce":"IOC","type":"MARKET","side":"BUY","stopPrice":"0.0","icebergQty":"0.0","time":"1699432690360","updateTime":"1699432690386","isWorking":true,"reqAmount":"80"}
   * executedQty: 0.0054 eth数量 cummulativeQuoteQty,cumulativeQuoteQty : hkd数量
   *
   * @throws IOException
   */
  @Test
  public void testBuyLimitOrderSetAction() throws IOException {
    Exchange rawExchange = createRawExchange();
    LimitOrder limitOrder = new LimitOrder.Builder(OrderType.BID, ETH_HKD)
        .limitPrice(new BigDecimal("14023.25"))
        .originalAmount(new BigDecimal("0.01"))
        .build();
    String orderId = rawExchange.getTradeService().placeLimitOrder(limitOrder);

    log.info("{}", orderId);

    Collection<Order> order = rawExchange.getTradeService()
        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, orderId));
    log.info("query order details :{}", order.iterator().next());

    boolean cancelOrderStatus = rawExchange.getTradeService()
        .cancelOrder(orderId);
    log.info("cancelOrderStatus is cancel :{}", cancelOrderStatus);

    order = rawExchange.getTradeService()
        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, orderId));
    log.info("after order details :{}", order.iterator().next());
  }

  /**
   * { "symbol":"ETHHKD", "symbolName":"ETHHKD", "status":"TRADING", "baseAsset":"ETH",
   * "baseAssetName":"ETH", "baseAssetPrecision":"0.0001", "quoteAsset":"HKD",
   * "quoteAssetName":"HKD", "quotePrecision":"0.000001", "retailAllowed":true, "piAllowed":true,
   * "corporateAllowed":true, "omnibusAllowed":true, "icebergAllowed":false, "isAggregate":false,
   * "allowMargin":false, "filters":[ { "minPrice":"0.01", "maxPrice":"100000.00000000",
   * "tickSize":"0.01", "filterType":"PRICE_FILTER" }, { "minQty":"0.006", "maxQty":"61",
   * "stepSize":"0.0001", "filterType":"LOT_SIZE" }, { "minNotional":"80",
   * "filterType":"MIN_NOTIONAL" }, { "minAmount":"80", "maxAmount":"800000", "minBuyPrice":"0",
   * "filterType":"TRADE_AMOUNT" }, { "maxSellPrice":"0", "buyPriceUpRate":"0.2",
   * "sellPriceDownRate":"0.2", "filterType":"LIMIT_TRADING" }, { "buyPriceUpRate":"0.2",
   * "sellPriceDownRate":"0.2", "filterType":"MARKET_TRADING" }, {
   * "noAllowMarketStartTime":"1695362400000", "noAllowMarketEndTime":"1695362700000",
   * "limitOrderStartTime":"0", "limitOrderEndTime":"0", "limitMinPrice":"0", "limitMaxPrice":"0",
   * "filterType":"OPEN_QUOTE" } ]
   * <pre>
   *    filterType ->TRADE_AMOUNT   最小amount不能小于 80, 否则 {"code":"-1140","msg":"Order amount lower than the minimum"}
   *  </pre>
   *
   * @throws IOException
   */
  @Test
  public void testBuyMarketOrderSetAction() throws IOException {
    Exchange rawExchange = createRawExchange();
    //市价单时amount传报价货币的数量, ETH_HKD  即为 80 HKD 买多少市价ETH币
    MarketOrder marketrder = new MarketOrder.Builder(OrderType.BID, ETH_HKD)
        .originalAmount(new BigDecimal("100"))
        .build();
    String orderId = rawExchange.getTradeService().placeMarketOrder(marketrder);

    log.info("{}", orderId);

    Collection<Order> order = rawExchange.getTradeService()
        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, orderId));
    log.info("query order details :{}", order.iterator().next());

  }


  @Test
  public void testSellMarketOrderSetAction() throws IOException {
    Exchange rawExchange = createRawExchange();
    //市价单时amount传报价货币的数量, ETH_HKD  即为 80 HKD 买多少市价ETH币
    MarketOrder marketrder = new MarketOrder.Builder(OrderType.ASK, ETH_HKD)
        .originalAmount(new BigDecimal("0.0068"))
        .build();
    //16:51:37.910 [default] [main] INFO  si.mazi.rescu.ResponseReader - Constructed an exception with no message. Response body was: {"code":"-1203","msg":"Order sell quantity too large"}
    String orderId = rawExchange.getTradeService().placeMarketOrder(marketrder);

    log.info("{}", orderId);
//
//    Collection<Order> order = rawExchange.getTradeService()
//        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, orderId));
//    log.info("query order details :{}", order.iterator().next());
//
//    boolean cancelOrderStatus = rawExchange.getTradeService()
//        .cancelOrder(orderId);
//    log.info("cancelOrderStatus is cancel :{}", cancelOrderStatus);
//
//
//    order = rawExchange.getTradeService()
//        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, orderId));
//    log.info("after order details :{}", order.iterator().next());
  }


  /**
   * request body: [{"accountId":"1508027074815750912","exchangeId":"301","symbol":"ETHHKD","symbolName":"ETHHKD","clientOrderId":"1699436282299","orderId":"1549378290409621248","price":"14306","origQty":"0.01","executedQty":"0","cummulativeQuoteQty":"0","cumulativeQuoteQty":"0","avgPrice":"0","status":"NEW","timeInForce":"GTC","type":"LIMIT","side":"BUY","stopPrice":"0.0","icebergQty":"0.0","time":"1699436285246","updateTime":"1699436285253","isWorking":true,"reqAmount":"0"}]
   * @throws IOException
   */
  @Test
  public void testOpenOrders() throws IOException {
    Exchange rawExchange = createRawExchange();
    List<HashKeyOrderDetailResponse> result = ((HashKeyTradeServiceRaw) rawExchange.getTradeService()).getOpenOrders(
        null, null, null);
    log.info("{}", result.toString());
//
//    Collection<Order> order = rawExchange.getTradeService()
//        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, orderId));
//    log.info("query order details :{}", order.iterator().next());
  }


  /**
   * @throws IOException
   */
  @Test
  public void testTradeHistorys() throws IOException {
    Exchange rawExchange = createRawExchange();
    List<HashKeyOrderDetailResponse> result = ((HashKeyTradeServiceRaw) rawExchange.getTradeService()).getTradeOrders(
        null, null, null,null,null);
    log.info("{}", result.toString());
    for (HashKeyOrderDetailResponse hashKeyOrderDetailResponse : result) {
      log.info("detail:{}", hashKeyOrderDetailResponse.toString());
    }
//
//    Collection<Order> order = rawExchange.getTradeService()
//        .getOrder(new DefaultQueryOrderParamInstrument(ETH_HKD, orderId));
//    log.info("query order details :{}", order.iterator().next());
  }
}
