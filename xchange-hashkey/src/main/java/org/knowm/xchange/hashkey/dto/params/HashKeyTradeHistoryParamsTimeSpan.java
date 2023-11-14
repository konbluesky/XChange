package org.knowm.xchange.hashkey.dto.params;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.trade.params.TradeHistoryParamInstrument;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@Builder
public class HashKeyTradeHistoryParamsTimeSpan implements TradeHistoryParamInstrument,
    TradeHistoryParamsTimeSpan {

  private Integer limit;
  private String orderId;
  private Date start;
  private Date end;
  private Instrument instrument;

  @Override
  public Instrument getInstrument() {
    return instrument;
  }

  @Override
  public void setInstrument(Instrument instrument) {
    this.instrument = instrument;
  }

  @Override
  public Date getStartTime() {
    return start;
  }

  @Override
  public void setStartTime(Date startTime) {
    this.start = startTime;
  }

  @Override
  public Date getEndTime() {
    return end;
  }

  @Override
  public void setEndTime(Date endTime) {
    this.end = endTime;
  }
}
