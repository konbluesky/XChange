package org.knowm.xchange.mexc.service;

import static org.knowm.xchange.utils.DigestUtils.bytesToHex;

import com.google.common.net.PercentEscaper;
import jakarta.ws.rs.QueryParam;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import org.knowm.xchange.mexc.MEXC;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.Params;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

public class MEXCDigest extends BaseParamsDigest {

  public MEXCDigest(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static ParamsDigest createInstance(String secretKey) {
    return new MEXCDigest(secretKey);
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    String input = getDigestInputParams(restInvocation);
    Mac mac = getMac();
    mac.update(input.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(mac.doFinal());
  }

  private String getDigestInputParams(RestInvocation restInvocation) {
    Params p = Params.of();
    restInvocation.getParamsMap().get(QueryParam.class).asHttpHeaders().entrySet().stream()
        .filter(e -> !MEXC.SING_KEY.equals(e.getKey()))
        .forEach(e -> p.add(e.getKey(), e.getValue()));

//    guava 中自带的UrlEscapers.urlFragmentEscaper对()并不转义,所以需要通过干预PercentEscaper来实现
    String URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS = "-._~" // Unreserved characters.
        + "!$'*,;&=" // The subdelim characters (excluding '+').
        + "@:"; // The gendelim characters permitted in paths.
    PercentEscaper hasBracketEscaper = new PercentEscaper(URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS,
        false);

    return hasBracketEscaper.escape(p.toString());
//    throw new NotYetImplementedForExchangeException("Only GET, DELETE and POST are supported in digest");
  }

}