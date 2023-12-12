package org.knowm.xchange.mexc;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.account.MEXCExchangeInfo;
import org.knowm.xchange.mexc.dto.account.MEXCPricePair;
import org.knowm.xchange.mexc.dto.market.MEXC24Ticker;
import org.knowm.xchange.mexc.dto.ws.WebSocketToken;
import org.knowm.xchange.mexc.service.MEXCException;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * <p> @Date : 2023/8/30 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Path("/api/v3")
@Produces(MediaType.APPLICATION_JSON)
public interface MEXC {

  String API_KEY = "X-MEXC-APIKEY";
  String SING_KEY = "signature";
  String REQ_TIME = "timestamp";

  @GET
  @Path("/time")
  MEXCResult<JsonNode> time() throws MEXCException;

  @POST
  @Path("/userDataStream")
  WebSocketToken getWsToken(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature
  ) throws IOException, MEXCException;

  @GET
  @Path("/userDataStream")
  JsonNode getWsTokens(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature
  ) throws IOException, MEXCException;

  @DELETE
  @Path("/userDataStream")
  WebSocketToken deleteWsToken(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("listenKey") String listenKey
  ) throws IOException, MEXCException;

  @PUT
  @Path("/userDataStream")
  WebSocketToken putWsToken(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("listenKey") String listenKey
  ) throws IOException, MEXCException;

  @GET
  @Path("/exchangeInfo")
  MEXCExchangeInfo getExchangeInfo() throws IOException, MEXCException;


  @GET
  @Path("/defaultSymbols")
  MEXCResult<List<String>> getSupportApiSymbols() throws IOException, MEXCException;

  @GET
  @Path("/ticker/price")
  MEXCPricePair getTickerPrice(
      @QueryParam("symbol") String symbol
  ) throws IOException, MEXCException;

  @GET
  @Path("/ticker/price")
  List<MEXCPricePair> getTickersPrice() throws IOException, MEXCException;

  @GET
  @Path("/ticker/24hr")
  List<MEXC24Ticker> getTicker24hr() throws IOException, MEXCException;
}
