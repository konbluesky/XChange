package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * 网络配置信息;
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXCConfig {

  private String coin;
  private String name;
  private List<MEXCNetwork> networkList;

  @JsonCreator
  public MEXCConfig(
      @JsonProperty("coin") String coin,
      @JsonProperty("name") String name,
      @JsonProperty("networkList") List<MEXCNetwork> networkList) {
    this.coin = coin;
    this.name = name;
    this.networkList = networkList;
  }

  public MEXCNetwork getNetwork(String chain){
    return networkList.stream().filter(network -> network.getNetwork().equalsIgnoreCase(chain.toLowerCase())).findFirst().get();
  }

}
