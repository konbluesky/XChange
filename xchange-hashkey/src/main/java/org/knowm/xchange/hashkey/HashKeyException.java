package org.knowm.xchange.hashkey;

/**
 * <p> @Date : 2023/11/8 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class HashKeyException extends RuntimeException {

  public HashKeyException() {
  }

  public HashKeyException(String message) {
    super(message);
  }

  public HashKeyException(String message, Throwable cause) {
    super(message, cause);
  }

  public HashKeyException(Throwable cause) {
    super(cause);
  }

  public HashKeyException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
