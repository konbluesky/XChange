package org.knowm.xchange.binance.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.knowm.xchange.binance.dto.account.TransferAllPurposeResponse.TransferRecord;

/**
 * <p> @Date : 2023/8/20 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
public final class TransferAllPurposeResponse extends SapiResponse<List<TransferRecord>> {

  private final TransferRecord[] rows;
  private final BigDecimal total;

  public TransferAllPurposeResponse(@JsonProperty("rows") TransferRecord[] rows,
      @JsonProperty("total") BigDecimal total) {
    this.rows = rows;
    this.total = total;
  }

  @Override
  public List<TransferRecord> getData() {
    return Arrays.asList(rows);
  }

  @Data
  @ToString
  public static final class TransferRecord {

    private String asset;
    private BigDecimal amount;
    private TransferType type;
    private TransferStatus status;
    private long tranId;
    private long timestamp;
  }

  public  enum TransferStatus{
    PENDING,
    CONFIRMED,
    FAILED
  }
}
