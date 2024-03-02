package org.knowm.xchange.gateio.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.account.Wallet.WalletFeature;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.gateio.GateioExchangeWiremock;
import org.knowm.xchange.gateio.dto.GateioEnums.OrderSide;
import org.knowm.xchange.gateio.dto.GateioEnums.TimeInForce;
import org.knowm.xchange.gateio.dto.trade.GateioOrder;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderByCurrencyPairAndIdParams;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderByInstrumentAndIdParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;

/**
 * <p> @Date : 2024/2/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class GateioTradeServiceTest extends GateioExchangeWiremock {

  GateioTradeService gateioTradeService = (GateioTradeService) exchange.getTradeService();
  GateioTradeServiceRaw gateioTradeServiceRaw = (GateioTradeServiceRaw) exchange.getTradeService();
  AccountService accountService = exchange.getAccountService();

  @Test
  public void getTrade() throws IOException {
    OpenOrders openOrders = gateioTradeService.getOpenOrders();
    System.out.println(openOrders);
  }

  @Test
  public void testRawPlaceBuyLimitOrder() throws IOException {
    GateioOrder gateioOrder = gateioTradeServiceRaw.placeLimitOrder(new CurrencyPair("GT", "USDT"),
        OrderSide.BUY,
        new BigDecimal("4.756"), new BigDecimal("2"), TimeInForce.GTC);
    log.info("Result:{}", gateioOrder);
  }

  @Test
  public void testRawPlaceSellLimitOrder() throws IOException {
    GateioOrder gateioOrder = gateioTradeServiceRaw.placeLimitOrder(new CurrencyPair("GT", "USDT"),
        OrderSide.SELL,
        new BigDecimal("4.77"), new BigDecimal("1"),TimeInForce.GTC);
    log.info("Result:{}", gateioOrder);
  }

  @Test
  public void testPlaceBuyMarketOrder() throws IOException {
    GateioOrder gateioOrder = gateioTradeServiceRaw.placeMarketOrder(new CurrencyPair("GT", "USDT"),
        OrderSide.BUY,
        new BigDecimal("1"),TimeInForce.FOK);
    log.info("Result:{}", gateioOrder);
  }

//TODO
  @Test
  public void testPlaceSellMarketOrder() throws IOException {
    Wallet wallet = accountService.getAccountInfo().getWallet(WalletFeature.TRADING);
    Balance balance = wallet.getBalance(Currency.GT);
    log.info("{} : {}", balance.getCurrency(), balance);
    GateioOrder gateioOrder = gateioTradeServiceRaw.placeMarketOrder(new CurrencyPair("GT", "USDT"),
        OrderSide.SELL,
        new BigDecimal("3.988"),TimeInForce.FOK);
    log.info("Result:{}", gateioOrder);
  }

  @Test
  public void testCancelOrder() throws IOException {
//    gateioTradeServiceRaw.cancelOrder()
//{"id":"513510332407","text":"apiv4","amend_text":"-","create_time":"1708498716","update_time":"1708498716","create_time_ms":1708498716250,"update_time_ms":1708498716250,"status":"open","currency_pair":"GT_USDT","type":"limit","account":"spot","side":"buy","amount":"2","price":"4.766","time_in_force":"gtc","iceberg":"0","left":"2","fill_price":"0","filled_total":"0","fee":"0","fee_currency":"GT","point_fee":"0","gt_fee":"0","gt_maker_fee":"0","gt_taker_fee":"0","gt_discount":false,"rebated_fee":"0","rebated_fee_currency":"USDT","finish_as":"open"}
    String orderId="513510332407";
    CurrencyPair currency_pair = new CurrencyPair("GT", "USDT");
    DefaultCancelOrderByCurrencyPairAndIdParams params = new DefaultCancelOrderByCurrencyPairAndIdParams(
        currency_pair, orderId);
    boolean b = gateioTradeService.cancelOrder(params);
    log.info("Result:{}", b);
  }

  @Test
  public void testGetOrderDetailsById() throws IOException{
//    {"id":"513454777367","text":"apiv4","amend_text":"-","create_time":"1708492255","update_time":"1708492255","create_time_ms":1708492255005,"update_time_ms":1708492255005,"status":"open","currency_pair":"GT_USDT","type":"limit","account":"spot","side":"buy","amount":"1","price":"4.7","time_in_force":"gtc","iceberg":"0","left":"1","fill_price":"0","filled_total":"0","fee":"0","fee_currency":"GT","point_fee":"0","gt_fee":"0","gt_maker_fee":"0","gt_taker_fee":"0","gt_discount":false,"rebated_fee":"0","rebated_fee_currency":"USDT","finish_as":"open"}
    DefaultQueryOrderParamCurrencyPair orderParamCurrencyPair = new DefaultQueryOrderParamCurrencyPair();
    orderParamCurrencyPair.setOrderId("520673393756");
    orderParamCurrencyPair.setCurrencyPair(new CurrencyPair("LOOP", "USDT"));
    Collection<Order> order = gateioTradeService.getOrder(orderParamCurrencyPair);
    log.info("order:{}", order);
  }

  @Test
  public void testGetOpenOrders() throws IOException{
    OpenOrders openOrders = gateioTradeService.getOpenOrders();
    log.info("size:{}", openOrders.getOpenOrders().size());
    log.info("openOrders:{}",openOrders);
  }




}