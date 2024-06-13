package org.knowm.xchange.gateio;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * <p> @Date : 2024/5/28 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class GateioNormalTimestampFactory implements SynchronizedValueFactory<Long> {

  private final AtomicLong nonce = new AtomicLong(0);

  private final Supplier<Long> timeFn;

  public GateioNormalTimestampFactory(final TimeUnit timeUnit) {
    switch (timeUnit) {
      case SECONDS:
        timeFn = () -> System.currentTimeMillis() / 1000;
        break;
      case MILLISECONDS:
        timeFn = System::currentTimeMillis;
        break;
      case MICROSECONDS:
        timeFn = () -> System.nanoTime() / 1000;
        break;
      case NANOSECONDS:
        timeFn = System::nanoTime;
        break;
      default:
        throw new IllegalArgumentException(String.format("TimeUnit %s not supported", timeUnit));
    }
  }

  @Override
  public Long createValue() {
    return timeFn.get();
  }
}
