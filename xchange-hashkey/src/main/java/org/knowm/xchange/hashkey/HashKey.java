package org.knowm.xchange.hashkey;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import org.knowm.xchange.hashkey.dto.HashKeyExchangeInfo;
import org.knowm.xchange.hashkey.dto.HashKeyOrderBookDepth;
import org.knowm.xchange.hashkey.dto.HashKeySymbolPriceTicker;
import org.knowm.xchange.hashkey.dto.ws.WebSocketToken;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * <p> @Date : 2023/11/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Path("/api/v1")
@Produces({MediaType.APPLICATION_JSON})
public interface HashKey {

  String API_KEY = "X-HK-APIKEY";
  String SING_KEY = "signature";
  String REQ_TIME = "timestamp";

  @GET
  @Path("/time")
  JsonNode time() throws HashKeyException;

  @GET
  @Path("/exchangeInfo")
  HashKeyExchangeInfo getExchangeInfo() throws IOException, HashKeyException;


  @GET
  @Path("/depth")
  HashKeyOrderBookDepth getOrderBookDepth(
      @QueryParam("symbol") String symbol,
      @QueryParam("limit") Integer limit
  ) throws IOException, HashKeyException;


  @GET
  @Path("/ticker/price")
  HashKeySymbolPriceTicker getSymbolPriceTicker(
      @QueryParam("symbol") String symbol
  ) throws IOException, HashKeyException;

  @POST
  @Path("/userDataStream")
  WebSocketToken getWsToken(
      @HeaderParam(API_KEY) String apiKey,
      @QueryParam(REQ_TIME) SynchronizedValueFactory<Long> timestamp,
      @QueryParam(SING_KEY) ParamsDigest signature,
      @QueryParam("recvWindow") Long recvWindow
  ) throws IOException, HashKeyException;

}
