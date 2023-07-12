package org.knowm.xchange.xt.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p> @Date : 2023/7/10 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Getter
@NoArgsConstructor
@ToString
public class XTSymbol {
    @JsonProperty("id")
    private String id;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("state")
    private String state;
    @JsonProperty("tradingEnabled")
    private String tradingEnabled;
    @JsonProperty("openapiEnabled")
    private String openapiEnabled;
    @JsonProperty("nextStateTime")
    private String nextStateTime;
    @JsonProperty("nextState")
    private String nextState;
    @JsonProperty("depthMergePrecision")
    private String depthMergePrecision;


    /**
     * {
     *         "id": 614,                                  //ID
     *         "symbol": "btc_usdt",                       //交易对
     *         "state": "ONLINE",                          //交易对状态[ONLINE=上线的;OFFLINE=下线的,DELISTED=退市]
     *         "tradingEnabled": true,                     //启用交易
     *         "openapiEnabled": true,                     //启用OPENAPI
     *         "nextStateTime": null,                      //下一个状态时间
     *         "nextState": null,                          //下一个状态
     *         "depthMergePrecision": 5,                   //深度合并精度
     *         "baseCurrency": "btc",                      //标的资产
     *         "baseCurrencyPrecision": 5,                 //标的资产精度
     *         "baseCurrencyId": 2,                        //标的资产ID
     *         "quoteCurrency": "usdt",                    //报价资产
     *         "quoteCurrencyPrecision": 6,                //报价资产精度
     *         "quoteCurrencyId": 11,                      //报价资产ID
     *         "pricePrecision": 4,                        //交易价格精度
     *         "quantityPrecision": 6,                     //交易数量精度
     *         "orderTypes": [                             //订单类型[LIMIT=限价单;MARKET=市价单]
     *           "LIMIT",
     *           "MARKET"
     *         ],
     *         "timeInForces": [                           //有效方式[GTC=成交为止,一直有效; IOC=无法立即成交(吃单)的部分就撤销; FOK=无法全部立即成交就撤销; GTX=无法成为挂单方就撤销]
     *           "GTC",
     *           "FOK",
     *           "IOC",
     *           "GTX"
     *         ],
     *         "displayWeight": 1,                         //展示权重，越大越靠前
     *         "displayLevel": "FULL",                     //展示级别,[FULL=完全展示,SEARCH=搜索展示,DIRECT=直达展示,NONE=不展示]
     *         "plates": [],                               //所属板块  eg:22,23,24
     *         "filters": [                                //过滤器
     *           {
     *             "filter": "PROTECTION_LIMIT",
     *             "buyMaxDeviation": "0.8"
     *             "sellMaxDeviation": "0.8"
     *           },
     *           {
     *             "filter": "PROTECTION_MARKET",
     *             "maxDeviation": "0.1"
     *           },
     *           {
     *             "filter": "PROTECTION_ONLINE",
     *             "durationSeconds": "300",
     *             "maxPriceMultiple": "5"
     *           },
     *           {
     *             "filter": "PRICE",
     *             "min": null,
     *             "max": null,
     *             "tickSize": null
     *           },
     *           {
     *             "filter": "QUANTITY",
     *             "min": null,
     *             "max": null,
     *             "tickSize": null
     *           },
     *           {
     *             "filter": "QUOTE_QTY",
     *             "min": null
     *           },
     *        ]
     *       }
     */
}
