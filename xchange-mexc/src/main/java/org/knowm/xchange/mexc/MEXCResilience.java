package org.knowm.xchange.mexc;

import static jakarta.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import java.time.Duration;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.client.ResilienceUtils;

/**
 * 限流官方说明 : <a href="https://mexcdevelop.github.io/apidocs/spot_v3_cn/#c1fd2fc5ac">文档</a>
 * <p> @Date : 2023/12/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class MEXCResilience {

  /**
   * 限频说明 每个接口会标明是按照IP或者按照UID统计, 以及相应请求一次的权重值。不同接口拥有不同的权重，越消耗资源的接口权重就会越大。 按IP和按UID(account)两种模式分别统计,
   * 两者互相独立。按IP权重限频的接口，所有接口共用每分钟20000限制，按照UID统计的单接口权重总额是每分钟240000。
   */
  public  static final String REST_IP_RATE_LIMITER = "mexc_rest_ip_rate_limiter";
  public static final String REST_UID_RATE_LIMITER = "mexc_rest_uid_rate_limiter";

  public static ResilienceRegistries createRegistries() {
    final ResilienceRegistries registries = new ResilienceRegistries();

    registries.rateLimiters()
        .rateLimiter(REST_IP_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(20000)
                .drainPermissionsOnResult(
                    e -> ResilienceUtils.matchesHttpCode(e, TOO_MANY_REQUESTS))
                .build());

    registries.rateLimiters()
        .rateLimiter(REST_UID_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(24000)
                .drainPermissionsOnResult(
                    e -> ResilienceUtils.matchesHttpCode(e, TOO_MANY_REQUESTS))
                .build());
    return registries;
  }


}