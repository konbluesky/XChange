package org.knowm.xchange.mexc.dto.market;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import java.util.Collection;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.mexc.dto.account.MEXCNetwork;

/**
 * <p> @Date : 2023/12/12 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCCurrencyNetworkMapping {

  /** 使用 Multimap 表示一个币种支持的多个网络 */
  private final Multimap<String, String> currencyToNetwork = HashMultimap.create();
  /** 使用 Multimap 表示一个网络下支持的多个币种 */
  private final Multimap<String, String> networkToCurrency = HashMultimap.create();

  /**
   * 构造函数，根据给定的表格初始化映射关系。
   *
   * @param currencyNetwork 表格，表示币种和网络之间的关系
   */
  public MEXCCurrencyNetworkMapping(Table<Currency, String, MEXCNetwork> currencyNetwork) {
    // 遍历表格，初始化映射关系
    for (Table.Cell<Currency, String, MEXCNetwork> cell : currencyNetwork.cellSet()) {
      String currency = cell.getRowKey().getCurrencyCode();
      String network = cell.getColumnKey();
      currencyToNetwork.put(currency, network);
      networkToCurrency.put(network, currency);
    }
  }

  /**
   * 添加币种到网络的映射关系。
   *
   * @param currency 币种
   * @param network  网络
   */
  public void addCurrencyToNetwork(String currency, String network) {
    currencyToNetwork.put(currency, network);
    networkToCurrency.put(network, currency);
  }

  /**
   * 获取一个币种支持的所有网络。
   *
   * @param currency 币种
   * @return 该币种支持的所有网络
   */
  public Collection<String> getNetworksForCurrency(String currency) {
    return currencyToNetwork.get(currency);
  }

  /**
   * 获取一个网络下支持的所有币种。
   *
   * @param network 网络
   * @return 该网络下支持的所有币种
   */
  public Collection<String> getCurrenciesForNetwork(String network) {
    return networkToCurrency.get(network);
  }

  /**
   * 移除币种到网络的映射关系。
   *
   * @param currency 币种
   * @param network  网络
   */
  public void removeCurrencyToNetwork(String currency, String network) {
    currencyToNetwork.remove(currency, network);
    networkToCurrency.remove(network, currency);
  }

  /**
   * 清空所有映射关系。
   */
  public void clearMappings() {
    currencyToNetwork.clear();
    networkToCurrency.clear();
  }
}
