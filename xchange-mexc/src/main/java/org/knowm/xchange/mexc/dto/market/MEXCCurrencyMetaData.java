package org.knowm.xchange.mexc.dto.market;

import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.ToString;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;

/**
 * <p> @Date : 2023/12/5 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Data
public class MEXCCurrencyMetaData extends CurrencyMetaData {

  public static final String DEFAULT_KEY = MEXCNetwork.NETWORK_BSC2;

  private Collection<MEXCNetwork> networks;
  private Map<String, MEXCNetwork> networksMap;

  public MEXCCurrencyMetaData(Integer scale, BigDecimal withdrawalFee) {
    super(scale, withdrawalFee);
  }

  public MEXCCurrencyMetaData(Integer scale, BigDecimal withdrawalFee,
      BigDecimal minWithdrawalAmount) {
    super(scale, withdrawalFee, minWithdrawalAmount);
  }


  public MEXCCurrencyMetaData(Integer scale, BigDecimal withdrawalFee,
      BigDecimal minWithdrawalAmount,
      WalletHealth walletHealth) {
    super(scale, withdrawalFee, minWithdrawalAmount, walletHealth);
  }

  public void setNetworks(Collection<MEXCNetwork> networks) {
    this.networks = networks;
    this.networksMap = networks.stream().collect(
        java.util.stream.Collectors.toMap(MEXCNetwork::getNetwork, network -> network));
  }

  public MEXCNetwork getDefaultNetwork() {
    Preconditions.checkNotNull(getNetwork(DEFAULT_KEY), "BSC node not found!");
    return getNetwork(DEFAULT_KEY);
  }

  public MEXCNetwork getNetwork(String name) {
    return networksMap.get(name);
  }
}
