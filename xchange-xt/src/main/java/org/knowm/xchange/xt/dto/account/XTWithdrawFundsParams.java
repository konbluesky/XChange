package org.knowm.xchange.xt.dto.account;

import lombok.Getter;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;

import java.math.BigDecimal;

/**
 * <p> @Date : 2023/7/14 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
public class XTWithdrawFundsParams extends DefaultWithdrawFundsParams {

    private final String chain;

    public XTWithdrawFundsParams(String address, Currency currency, BigDecimal amount, String chain) {
        super(address, currency, amount);
        this.chain = chain;
    }

    public XTWithdrawFundsParams(AddressWithTag address, Currency currency, BigDecimal amount, String chain) {
        super(address, currency, amount);
        this.chain = chain;
    }

    public XTWithdrawFundsParams(String address, Currency currency, BigDecimal amount, BigDecimal commission, String chain) {
        super(address, currency, amount, commission);
        this.chain = chain;
    }

    public XTWithdrawFundsParams(AddressWithTag address, Currency currency, BigDecimal amount, BigDecimal commission, String chain) {
        super(address, currency, amount, commission);
        this.chain = chain;
    }

    public XTWithdrawFundsParams(String address, String addressTag, Currency currency, BigDecimal amount, BigDecimal commission, String chain) {
        super(address, addressTag, currency, amount, commission);
        this.chain = chain;
    }
}
