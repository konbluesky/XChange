package org.knowm.xchange.mexc.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Builder
@Getter
@ToString
public class MEXCWithdrawApply {

  /**
   * 请求参数
   *
   * 参数名	数据类型	是否必须	说明
   * coin	string	是	币种
   * withdrawOrderId	string	否	自定义提币ID
   * network	string	否	提币网络
   * address	string	是	提币地址
   * memo	string	否	如地址中需求memo，则此处必传
   * amount	string	是	数量
   * remark	string	否	备注
   * timestamp	string	是	时间戳
   * signature	string	是	签名
   */

  /**
   * 币种
   */
  private String coin;
  /**
   * 自定义提币ID
   */
  private String withdrawOrderId;
  /**
   * 提币网络
   */
  private String network;
  /**
   * 提币地址
   */
  private String address;
  /**
   * 如地址中需求memo，则此处必传
   */
  private String memo;
  /**
   * 数量
   */
  private String amount;
  /**
   * 备注
   */
  private String remark;

}
