package org.knowm.xchange.xt.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * <p> @Date : 2024/6/19 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@ToString
@Data
@Builder
public class XTFundingHistoryRequest {

  //  Parameters
//  参数	数据类型	是否必须	默认值	描述	取值范围
//  currency	string	true		币种名称，可从“获取XT可充提的币种”接口中获取
//  chain	string	true		转账网络名称，可从“获取XT可充提的币种”接口中获取
//  status	string	false		充值记录的状态	SUBMIT、REVIEW、AUDITED、PENDING、SUCCESS、FAIL、CANCEL
//  fromId	long	false		上次开始分页的Id，即记录的主键id
//  direction	string	false	NEXT	分页方向	NEXT：下一页，PREV：上一页
//  limit	int	false	10	每页记录数，最大不超过200	1<=limit<=200
//  startTime	long	false		查询范围开始边界，毫秒级时间戳
//  endTime	long	false		查询范围结束边界，毫秒级时间戳
  @JsonProperty("currency")
  private String currency;
  @JsonProperty("chain")
  private String chain;
  @JsonProperty("status")
  private String status;
  @JsonProperty("fromId")
  private Long fromId;
  @JsonProperty("direction")
  private String direction;
  @JsonProperty("limit")
  private Integer limit;
  @JsonProperty("startTime")
  private Long startTime;
  @JsonProperty("endTime")
  private Long endTime;


}
