package org.knowm.xchange.xt.dto.marketdata;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@ToString
public class XTCurrencyWalletInfo {
    private String currency;
    private List<XTCurrencyChainInfo> supportChains;


    public XTCurrencyChainInfo getWalletInfo(String chainName) {
        for (XTCurrencyChainInfo chainInfo : supportChains) {
            if (chainInfo.getChain().equalsIgnoreCase(chainName)) {
                return chainInfo;
            }
        }
        return null;

    }
}
