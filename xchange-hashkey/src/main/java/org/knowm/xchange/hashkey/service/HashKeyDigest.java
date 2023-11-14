package org.knowm.xchange.hashkey.service;

import static org.knowm.xchange.utils.DigestUtils.bytesToHex;

import jakarta.ws.rs.QueryParam;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import org.knowm.xchange.hashkey.HashKey;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.Params;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

public class HashKeyDigest extends BaseParamsDigest {

  public HashKeyDigest(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static ParamsDigest createInstance(String secretKey) {
    return new HashKeyDigest(secretKey);
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
        .filter(e -> !HashKey.SING_KEY.equals(e.getKey()))
        .forEach(e -> p.add(e.getKey(), e.getValue()));

    return p.asQueryString();
//    throw new NotYetImplementedForExchangeException("Only GET, DELETE and POST are supported in digest");
  }

}