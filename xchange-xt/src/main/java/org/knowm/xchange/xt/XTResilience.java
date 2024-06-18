package org.knowm.xchange.xt;

import static jakarta.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.client.ResilienceUtils;

/**
 * <p> @Date : 2024/6/13 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class XTResilience {

  /**
   * <pre>
   * 部分接口会有限流控制(对应接口下会有限流说明)，限流主要分为网关限流和WAF限流。
   * 若接口请求触发了网关限流则会返回429，表示警告访问频次超限，即将被封IP或者apiKey。
   * 网关限流分为针对IP和apiKey限流。
   * IP限流示例说明：100/s/ip，表示每个IP每秒该接口请求次数限制。
   * apiKey限流示例说明：50/s/apiKey，表示每个apiKey每秒该接口请求次数限制。
   * </pre>
   *
   * @link https://doc.xt.com/#documentation_cnlimitRules
   */
  public static final String IP_RATE_TYPE = "IP_RATE_TYPE";
  public static final String API_RATE_TYPE = "API_RATE_TYPE";

  public static ResilienceRegistries createRegistries() {
    final ResilienceRegistries registries = new ResilienceRegistries();
    registries
        .rateLimiters()
        .rateLimiter(
            IP_RATE_TYPE,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(100)
                .drainPermissionsOnResult(
                    e -> ResilienceUtils.matchesHttpCode(e, TOO_MANY_REQUESTS))
                .build());
    registries.rateLimiters().rateLimiter(API_RATE_TYPE,
        RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .limitForPeriod(50)
            .drainPermissionsOnResult(
                e -> ResilienceUtils.matchesHttpCode(e, TOO_MANY_REQUESTS))
            .build());
    return registries;
  }
}
