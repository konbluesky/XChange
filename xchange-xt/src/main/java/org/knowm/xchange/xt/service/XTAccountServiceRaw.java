package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.xt.dto.account.BalanceResponse;
import org.knowm.xchange.xt.dto.account.WithdrawRequest;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyInfo;

import java.io.IOException;
import java.util.List;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTAccountServiceRaw extends XTBaseService {
    /**
     * Constructor
     *
     * @param exchange
     */
    public XTAccountServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public BalanceResponse balance(String currency) throws IOException {
        return xtAuthenticated.balance(
                BaseParamsDigest.HMAC_SHA_256,
                apiKey,
                RECV_WINDOW,
                String.valueOf(System.currentTimeMillis()),
                signatureCreator,
                currency).getData();
    }

    public List<BalanceResponse> balances() throws IOException {
        JsonNode jsonNode = xtAuthenticated.balances(
                BaseParamsDigest.HMAC_SHA_256,
                apiKey,
                RECV_WINDOW,
                String.valueOf(System.currentTimeMillis()),
                signatureCreator).getData();

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(jsonNode.get("assets"), mapper.getTypeFactory()
                                                                          .constructCollectionType(List.class, BalanceResponse.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public String time() throws IOException {
        return xt.time().getData().get("serverTime").toString();
    }

    public String withdraw(WithdrawRequest request) throws IOException {
        JsonNode jsonNode = xtAuthenticated.withdraw(
                BaseParamsDigest.HMAC_SHA_256,
                apiKey,
                RECV_WINDOW,
                String.valueOf(System.currentTimeMillis()),
                signatureCreator,
                request
        ).getData();
        return jsonNode.get("id").textValue();
    }

}
