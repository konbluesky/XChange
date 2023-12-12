package org.knowm.xchange.mexc.dto.market;

import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
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
public class MEXCCurrencyMetaData extends CurrencyMetaData {

  public static final String DEFAULT_KEY = MEXCNetwork.NETWORK_BSC2;

  @Getter
  private Collection<MEXCNetwork> networks;
  @Getter
  private Map<String, MEXCNetwork> networksMap;

  private MEXCNetwork cursorNetwork;

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

  /**
   * 使用指定的网络。
   *
   * @param network 要使用的网络。
   * @return 当前的 MEXCCurrencyMetaData 实例。
   */
  public synchronized MEXCCurrencyMetaData use(String network) {
    Preconditions.checkNotNull(network, "网络不能为空");
    this.cursorNetwork = getNetwork(network);
    return this;
  }

  /**
   * 设置网络并构建 networksMap。
   *
   * @param networks 网络的集合。
   */
  public void setNetworks(Collection<MEXCNetwork> networks) {
    this.networks = networks;
    this.networksMap = networks.stream().collect(
        java.util.stream.Collectors.toMap(MEXCNetwork::getNetwork, network -> network));
  }

  /**
   * 获取默认网络。
   *
   * @return 默认的网络。
   * @throws IllegalStateException 如果找不到默认的网络。
   */
  public MEXCNetwork getDefaultNetwork() {
    MEXCNetwork defaultNetwork = getNetwork(DEFAULT_KEY);
    if (defaultNetwork == null) {
      throw new IllegalStateException("找不到默认网络！");
    }
    return defaultNetwork;
  }

  /**
   * 根据名称获取网络。
   *
   * @param name 网络的名称。
   * @return 网络。
   */
  public MEXCNetwork getNetwork(String name) {
    return networksMap.get(name);
  }

  @Override
  public BigDecimal getWithdrawalFee() {
    Preconditions.checkNotNull(cursorNetwork, "请先设置 cursorNetwork！");
    return new BigDecimal(cursorNetwork.getWithdrawFee());
  }

  @Override
  public BigDecimal getMinWithdrawalAmount() {
    Preconditions.checkNotNull(cursorNetwork, "请先设置 cursorNetwork！");
    return new BigDecimal(cursorNetwork.getWithdrawMin());
  }

  @Override
  public WalletHealth getWalletHealth() {
    Preconditions.checkNotNull(cursorNetwork, "请先设置 cursorNetwork！");
    return cursorNetwork.isWithdrawEnable() && cursorNetwork.isDepositEnable() ? WalletHealth.ONLINE
        : WalletHealth.OFFLINE;
  }
}
