package org.knowm.xchange.gateio;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.gateio.dto.marketdata.GateioCandlestickHistory;
import org.knowm.xchange.gateio.dto.marketdata.GateioCoin;
import org.knowm.xchange.gateio.dto.marketdata.GateioCurrencyPairs;
import org.knowm.xchange.gateio.dto.marketdata.GateioDepth;
import org.knowm.xchange.gateio.dto.marketdata.GateioPair;
import org.knowm.xchange.gateio.dto.marketdata.GateioTicker;
import org.knowm.xchange.gateio.dto.marketdata.GateioTradeHistory;

@Path("api/v4")
@Produces(MediaType.APPLICATION_JSON)
public interface Gateio {

  String PUBLIC_RULE = "public_rule";
  public static final String PATH_SPOT_TICKERS = "/spot/tickers";
  Map<String, List<Integer>> publicPathRateLimits =
      new HashMap<String, List<Integer>>() {
        {
          put(PUBLIC_RULE, Arrays.asList(200, 10));
        }
      };


  @GET
  @Path("/spot/currency_pairs")
  List<GateioPair> getMarketInfo() throws IOException;

  @GET
  @Path("/spot/currencies")
  List<GateioCoin> getCoinInfo() throws IOException;

  @GET
  @Path("/spot/tickers")
  List<GateioTicker> getTickers() throws IOException;


  @GET
  @Path("pairs")
  GateioCurrencyPairs getPairs() throws IOException;

  @GET
  @Path("orderBooks")
  Map<String, GateioDepth> getDepths() throws IOException;

  @GET
  @Path("ticker/{ident}_{currency}")
  GateioTicker getTicker(
      @PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency)
      throws IOException;

  @GET
  @Path("orderBook/{ident}_{currency}")
  GateioDepth getFullDepth(
      @PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency)
      throws IOException;

  @GET
  @Path("tradeHistory/{ident}_{currency}")
  GateioTradeHistory getTradeHistory(
      @PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency)
      throws IOException;

  @GET
  @Path("tradeHistory/{ident}_{currency}/{tradeId}")
  GateioTradeHistory getTradeHistorySince(
      @PathParam("ident") String tradeableIdentifier,
      @PathParam("currency") String currency,
      @PathParam("tradeId") String tradeId)
      throws IOException;

  @GET
  @Path("candlestick2/{currency_pair}")
  GateioCandlestickHistory getKlinesGate(
      @PathParam("currency_pair") String tradePair,
      @QueryParam("range_hour") Integer hours,
      @QueryParam("group_sec") Long interval)
      throws IOException;
}
