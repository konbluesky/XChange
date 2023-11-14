package org.knowm.xchange.hashkey.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class HashKeyOrderBookDepth {

  private String timestamp; // 时间戳
  private List<HashKeyOrderInfo> buyingDirection; // 买方订单
  private List<HashKeyOrderInfo> sellingDirection; // 卖方订单


}
