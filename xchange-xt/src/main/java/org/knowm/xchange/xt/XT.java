package org.knowm.xchange.xt;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import org.knowm.xchange.xt.dto.XTException;
import org.knowm.xchange.xt.dto.XTResponse;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;


/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */

@Path("/v4/public")
@Produces(APPLICATION_JSON)
public interface XT {

  @GET
  @Path("/time")
  XTResponse<JsonNode> time() throws XTException;

  @GET
  @Path("/symbol")
  XTResponse<JsonNode> symbol(
      @QueryParam("symbol") String symbol,
      @QueryParam("symbols") String symbols,
      @QueryParam("version") String version
  ) throws XTException;

  @GET
  @Path("/currencies")
  XTResponse<JsonNode> currencies() throws XTException;

  @GET
  @Path("/wallet/support/currency")
  XTResponse<List<XTCurrencyWalletInfo>> walletSupportCurrency() throws XTException;

  @GET
  @Path("/ticker/price")
  XTResponse<List<XTTicker>> getTickerPrice(
      @QueryParam("symbol") String symbol,
      @QueryParam("symbols") String symbols,
      @QueryParam("tags") String tags
  ) throws XTException;

  @GET
  @Path("/ticker/24h")
  XTResponse<List<XTTicker>> getFullTickerPrice(
      @QueryParam("symbol") String symbol,
      @QueryParam("symbols") String symbols,
      @QueryParam("tags") String tags
  ) throws XTException;

}
