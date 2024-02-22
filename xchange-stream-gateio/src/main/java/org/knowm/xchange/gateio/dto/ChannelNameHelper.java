package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.knowm.xchange.gateio.GateioConst;

/**
 * <p> @Date : 2024/2/22 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
public class ChannelNameHelper {

  public static String getChannelName(JsonNode node) {
    //spot.tickers symbol = .result.currency_pair
    //spot.trades  symbol = .result.currency_pair
    //spot.book_ticker  symbol = .result.s
    //spot.order_book  symbol = .result.s
    //spot.orders  symbol = .result.currency_pair
    //spot.usertrades  symbol = .result.currency_pair
    //spot.balances  symbol = .result.user
    // 在另一个类或方法中
    String channel = node.get("channel").asText();
    String resultIdentifier;

    switch (channel) {
      case GateioConst.SPOT_TICKERS:
        resultIdentifier =
            GateioConst.SPOT_TICKERS + "_" + node.path("result").path("currency_pair").asText();
        break;
      case GateioConst.SPOT_TRADES:
        resultIdentifier =
            GateioConst.SPOT_TRADES + "_" + node.path("result").path("currency_pair").asText();
        break;
      case GateioConst.SPOT_BOOK_TICKER:
        resultIdentifier =
            GateioConst.SPOT_BOOK_TICKER + "_" + node.path("result").path("s").asText();
        break;
      case GateioConst.SPOT_ORDER_BOOK:
        resultIdentifier =
            GateioConst.SPOT_ORDER_BOOK + "_" + node.path("result").path("s").asText();
        break;
      case GateioConst.SPOT_ORDERS:
        if (node.path("result").isArray()) {
          resultIdentifier =
              GateioConst.SPOT_ORDERS + "_" + node.path("result").get(0).path("currency_pair").asText();
        }else {
          resultIdentifier =
              GateioConst.SPOT_ORDERS + "_" + node.path("result").path("currency_pair").asText();
        }
        break;
      case GateioConst.SPOT_USERTRADES:
        resultIdentifier =
            GateioConst.SPOT_USERTRADES + "_" + node.path("result").path("currency_pair").asText();
        break;
      case GateioConst.SPOT_BALANCES:
//        resultIdentifier =
//            GateioConst.SPOT_BALANCES + "_" + node.path("result").path("user").asText();
        resultIdentifier = GateioConst.SPOT_BALANCES;
        break;
      default:
        // 处理未知的 channel 值
        resultIdentifier = GateioConst.UNKNOWN_CHANNEL_PREFIX + channel;
    }

    return resultIdentifier.toLowerCase();
  }

  public static String getChannelName(String channel, String currencyPair) {
    return (channel + "_" + currencyPair).toLowerCase();
  }

}
