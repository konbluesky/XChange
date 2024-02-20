package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class MEXCNetwork {

  /**
   * "coin": "EOS", "depositDesc": null, "depositEnable": true, "minConfirm": 0, "name": "EOS",
   * "network": "EOS", "withdrawEnable": false, "withdrawFee": "0.000100000000000000",
   * "withdrawIntegerMultiple": null, "withdrawMax": "10000.000000000000000000", "withdrawMin":
   * "0.001000000000000000", "sameAddress": false, "contract": "TN3W4H6rK2ce4vX9YnFQHwKENnHjoxbm9",
   * "withdrawTips": "Both a MEMO and an Address are required.", "depositTips": "Both a MEMO and an
   * Address are required."
   */
  public static final String NETWORK_ARB="Arbitrum One(ARB)";
  public static final String NETWORK_BSC1="BEP20(BSC)";
  public static final String NETWORK_BSC2="BNB Smart Chain(BEP20)";
  public static final String NETWORK_ERC20="Ethereum(ERC20)";
  public static final String NETWORK_TRC20="Tron(TRC20)";
  public static final String NETWORK_SOLANA="Solana(SOL)";
  public static final String NETWORK_AVAX_C="Avalanche C Chain(AVAX CCHAIN)";
  public static final String NETWORK_POLYGON_MATIC="Polygon(MATIC)";



  private String coin;
  private String depositDesc;
  private boolean depositEnable;
  private int minConfirm;
  private String name;
  private String network;
  private boolean withdrawEnable;
  private String withdrawFee;
  private String withdrawIntegerMultiple;
  private String withdrawMax;
  private String withdrawMin;
  private boolean sameAddress;
  private String contract;
  private String withdrawTips;
  private String depositTips;

}
