package org.knowm.xchange.okex.service.params;

import lombok.Getter;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;

import java.math.BigDecimal;

/**
 * <p> @Date : 2023/5/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class OkexWithdrawFundsParams extends DefaultWithdrawFundsParams {

    private final String chain;

    private final String clientId;

    public OkexWithdrawFundsParams(String address, Currency currency, BigDecimal amount, String chain, String clientId) {
        super(address, currency, amount);
        this.chain = chain;
        this.clientId = clientId;
    }

    public OkexWithdrawFundsParams(AddressWithTag address, Currency currency, BigDecimal amount, String chain, String clientId) {
        super(address, currency, amount);
        this.chain = chain;
        this.clientId = clientId;
    }

    public OkexWithdrawFundsParams(String address, Currency currency, BigDecimal amount, BigDecimal commission, String chain, String clientId) {
        super(address, currency, amount, commission);
        this.chain = chain;
        this.clientId = clientId;
    }

    public OkexWithdrawFundsParams(AddressWithTag address, Currency currency, BigDecimal amount, BigDecimal commission, String chain, String clientId) {
        super(address, currency, amount, commission);
        this.chain = chain;
        this.clientId = clientId;
    }

    public OkexWithdrawFundsParams(String address, String addressTag, Currency currency, BigDecimal amount, BigDecimal commission, String chain, String clientId) {
        super(address, addressTag, currency, amount, commission);
        this.chain = chain;
        this.clientId = clientId;
    }
}
