package org.knowm.xchange.gateio.service;

import com.google.common.hash.Hashing;
import jakarta.ws.rs.HeaderParam;
import javax.crypto.Mac;
import org.knowm.xchange.service.BaseParamsDigest;
import org.knowm.xchange.utils.DigestUtils;
import si.mazi.rescu.Params;
import si.mazi.rescu.RestInvocation;

/**
 * This may be used as the value of a @HeaderParam, @QueryParam or @PathParam to create a digest of
 * the post body (composed of @FormParam's). Don't use as the value of a @FormParam, it will
 * probably cause an infinite loop.
 *
 * <p>This may be used for REST APIs where some parameters' values must be digests of other
 * parameters. An example is the MtGox API v1, where the Rest-Sign header parameter must be a digest
 * of the request body (which is composed of @FormParams).
 */
public class GateioHmacPostBodyDigest extends BaseParamsDigest {

  private String apiKey;
  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded
   *                                  key is invalid).
   */
  private GateioHmacPostBodyDigest(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_512);
  }

  private  GateioHmacPostBodyDigest(String apiKey, String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_512);
    this.apiKey = apiKey;
  }

  public static GateioHmacPostBodyDigest createInstance(String secretKeyBase64) {
    return secretKeyBase64 == null ? null : new GateioHmacPostBodyDigest(secretKeyBase64);
  }

  public static GateioHmacPostBodyDigest createInstance(String apiKey,String secretKeyBase64) {
    return secretKeyBase64 == null || apiKey == null ? null
        : new GateioHmacPostBodyDigest(apiKey, secretKeyBase64);
  }


  @Override
  public String digestParams(RestInvocation restInvocation) {

    // little hack here. the post body to create the signature mus not contain the url-encoded
    // parameters, they must be in plain form
    // passing ie the white space inside the withdraw method (required for XLM and XRP ... to pass
    // the tag) results in a plus sing '+', which is the correct encoding, but in this case the
    // signature is not created correctly.
    // the expected signature must be created using plain parameters. here we simply replace the +
    // by a white space, should be fine for now
    // see https://support.gate.io/hc/en-us/articles/360000808354-How-to-Withdraw-XRP

    /**
     * String ts = String.valueOf(System.currentTimeMillis() / 1000);
     * String bodyString = this.bodyToString(request.body());
     * String queryString = (request.url().query() == null) ? "" : request.url().query();
     * String signatureString = String.format("%s\n%s\n%s\n%s\n%s", request.method(), request.url().encodedPath(), queryString,
     *                                        DigestUtils.sha512Hex(bodyString), ts);
     */

    Params headParams = restInvocation.getParamsMap().get(HeaderParam.class);
    Long ts = (Long) headParams.getParamValue("Timestamp");
    String bodyString = restInvocation.getRequestBody();
    String queryString = restInvocation.getQueryString();

    Mac mac = getMac();
    String signatureString = String.format("%s\n%s\n%s\n%s\n%s",
        restInvocation.getHttpMethod(),
        "/" + restInvocation.getPath(),
        queryString,
        Hashing.sha512().hashBytes(bodyString.getBytes()).toString(), ts);

    return DigestUtils.bytesToHex(mac.doFinal((signatureString.getBytes())));
  }

  /**
   * 获取socket的签名
   * @param channel
   * @param event
   * @param timestamp
   * @return
   */
  public String socketSign(String channel, String event, long timestamp) {
    String signTmpl = String.format("channel=%s&event=%s&time=%d", channel, event, timestamp);
    Mac mac = getMac();
    String sign = DigestUtils.bytesToHex(mac.doFinal((signTmpl.getBytes())));
    return "{'method':'api_key','KEY':"+apiKey+",'SIGN':"+sign+"}";
  }

}
