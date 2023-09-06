package org.knowm.xchange.okex.service.params;

import lombok.Builder;
import lombok.Getter;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.trade.params.HistoryParamsFundingType;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrency;

import java.util.Date;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;

/**
 * <p> @Date : 2023/5/16 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Builder
@Getter
public class OkexFundingHistoryParams implements HistoryParamsFundingType, TradeHistoryParamCurrency ,
    TradeHistoryParamsTimeSpan {
    private FundingRecord.Type type;
    private Currency currency;
    private Date after;
    private Date before;

    @Override
    public FundingRecord.Type getType() {
        return this.type;
    }

    @Override
    public void setType(FundingRecord.Type type) {
        this.type = type;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public Date getStartTime() {
        return after;
    }

    @Override
    public void setStartTime(Date startTime) {
      this.after=startTime;
    }

    @Override
    public Date getEndTime() {
        return before;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.before=endTime;
    }
}
