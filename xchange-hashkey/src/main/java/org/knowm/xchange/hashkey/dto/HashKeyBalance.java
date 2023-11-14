package org.knowm.xchange.hashkey.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Getter
public class HashKeyBalance {

  private String asset; // 资产
  private String assetId; // 资产ID
  private String assetName; // 资产名称
  private String total; // 总可用资金
  private String free; // 可用资金
  private String locked; // 冻结资金

}