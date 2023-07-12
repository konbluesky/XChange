package org.knowm.xchange.xt;

import com.fasterxml.jackson.databind.JsonNode;
import org.knowm.xchange.xt.dto.XTException;
import org.knowm.xchange.xt.dto.XTResponse;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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

}
