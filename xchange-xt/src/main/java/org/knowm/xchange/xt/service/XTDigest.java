package org.knowm.xchange.xt.service;

import jakarta.ws.rs.QueryParam;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.utils.DigestUtils;
import si.mazi.rescu.Params;
import si.mazi.rescu.RestInvocation;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class XTDigest extends BaseParamsDigest {


  private static final String VALIDATE_TIMESTAMP = "validate-timestamp";
  private static final String VALIDATE_RECVWINDOW = "validate-recvwindow";
  private String apikey;

  private XTDigest(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static XTDigest createInstance(String apikey, String secretKeyBase64) {
    XTDigest xtDigest = new XTDigest(secretKeyBase64 == null ? "" : secretKeyBase64);
    xtDigest.apikey = apikey;
    return xtDigest;
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    String timestamp = restInvocation.getHttpHeadersFromParams().get(VALIDATE_TIMESTAMP);
    String recvwindow = restInvocation.getHttpHeadersFromParams().get(VALIDATE_RECVWINDOW);
    String x = String.format(
        "validate-algorithms=%s&validate-appkey=%s&validate-recvwindow=%s&validate-timestamp=%s",
        HMAC_SHA_256, apikey, recvwindow, timestamp);
    String y = String.format("#%s#%s", restInvocation.getHttpMethod(), restInvocation.getPath());
//    String query = restInvocation.getQueryString();
//    String query = restInvocation.getQueryString().replaceAll("\\+"," ");
    // restInvocation.getParamsMap().get(QueryParam.class).asHttpHeaders().keySet().stream().sorted().collect(Collectors.toList())
    // 此处对key要排序,否则参数未按字典排序签名后发给服务端,会返回AUTH103签名错误
    Params newParams = Params.of();
    Params oldParams = restInvocation.getParamsMap().get(QueryParam.class);
    restInvocation.getParamsMap().get(QueryParam.class).asHttpHeaders()
        .keySet().stream().sorted().forEach(e -> newParams.add(e, oldParams.getParamValue(e)));
    String query = newParams.toString();
    if (query != null && query.length() > 0) {
      y += "#" + query;
    }
    String jsonBody = restInvocation.getRequestBody();
    if (jsonBody != null && jsonBody.length() > 0) {
      y += "#" + jsonBody;
    }
    String origin = x + y;
    Mac mac = getMac();
    return DigestUtils.bytesToHex(mac.doFinal(origin.getBytes()));
  }
}
