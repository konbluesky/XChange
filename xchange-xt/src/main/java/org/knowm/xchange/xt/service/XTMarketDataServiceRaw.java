package org.knowm.xchange.xt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyInfo;
import org.knowm.xchange.xt.dto.marketdata.XTCurrencyWalletInfo;
import org.knowm.xchange.xt.dto.marketdata.XTSymbol;
import org.knowm.xchange.xt.dto.marketdata.XTTicker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTMarketDataServiceRaw extends XTBaseService {
    /**
     * Constructor
     *
     * @param exchange
     */
    public XTMarketDataServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public List<XTSymbol> getSymbols(String symbol, String currency) {
        JsonNode currencies = xt.symbol(symbol == null ? "" : symbol, currency == null ? "" : currency, null).getData();
        try {
            return mapper.treeToValue(currencies.get("symbols"), mapper.getTypeFactory()
                                                                       .constructCollectionType(List.class, XTSymbol.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<XTCurrencyWalletInfo> getWalletSupportCurrencys() {
        return xt.walletSupportCurrency().getData();
    }

    public Map<String, XTCurrencyWalletInfo> getWalletSupportCurrencysMap() {

        List<XTCurrencyWalletInfo> data = xt.walletSupportCurrency().getData();
        //将data转换成map结构，使用currency 作为key，XTCurrencyWalletInfo 作为value
        Map<String, XTCurrencyWalletInfo> collected = data.stream()
                                                          .collect(Collectors.toMap(XTCurrencyWalletInfo::getCurrency, x -> x));
        return collected;
    }


    public List<XTCurrencyInfo> getCurrencyInfos() {
        JsonNode currencies = xt.currencies().getData();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(currencies.get("currencies"), mapper.getTypeFactory()
                                                                          .constructCollectionType(List.class, XTCurrencyInfo.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public List<XTTicker> getTickers(String symbol, String symbols) {
        return xt.getFullTickerPrice(symbol == null ? null : symbol, symbols == null ? null : symbols, null).getData();
    }

}
