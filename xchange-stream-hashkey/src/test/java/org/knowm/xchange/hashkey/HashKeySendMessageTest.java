package org.knowm.xchange.hashkey;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.knowm.xchange.hashkey.dto.HashKeySendMessage;

/**
 * <p> @Date : 2023/9/4 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class HashKeySendMessageTest extends TestCase {

  @Test
  public void testString() {
    HashKeySendMessage sendMessage = HashKeySendMessage.createSubDepthMessage("BTCUSDT");

    sendMessage.putStringParam("binary",Boolean.FALSE.toString());
    log.info("sub message: {}", sendMessage);

//    sendMessage.putParam("spot@public.limit.depth.v3.api","BTCUDST",5);
//    sendMessage.putParam("spot@public.limit.depth.v3.api","ETHUDST",20);


    log.info("ping message: {}", HashKeySendMessage.createPingMessage());
  }

  @Test
  public void test1(){
//    {"symbol":"BTCUSDT","symbolName":"BTCUSDT","topic":"depth","params":{"realtimeInterval":"24h","binary":"false"},"data":[{"e":301,"s":"BTCUSDT","t":1699953000462,"v":"9199530_18","b":[["36718.44","0.0004"],["36716.01","0.2"],["36715.71","0.05"],["36703.78","0.3406"],["36702.44","0.10922"],["36697.47","0.10416"],["36696.66","0.00092"],["36692.89","0.08128"],["36692.76","1.278"],["36688.35","0.15211"],["36684.68","0.15569"],["36683.82","0.014"],["36683.62","0.01"],["36680.89","0.06763"],["36679.9","0.0108"],["36679.4","0.015"],["36678.3","0.00092"],["36676.92","0.14479"],["36674.41","0.2506"],["36672.9","0.1335"],["36670.71","0.259"],["36668.03","0.10128"],["36663.21","0.07464"],["36659.95","0.00093"],["36653.43","0.08642"],["36652.37","0.259"],["36646.42","0.07221"],["36645.95","0.01"],["36638.97","0.07423"],["36633.47","0.07102"],["36626.4","0.14233"],["36618.07","0.09208"],["36611.54","0.17021"],["36604.28","0.1041"],["36598.78","0.18665"],["36590.59","0.01"],["36590.42","0.13135"],["36586.05","0.13026"],["36580.56","0.09095"],["36575.07","0.14571"],["36569.58","0.06989"],["36564.09","0.19484"],["36558","0.1641"],["36557.94","0.18859"],["36551.48","0.07122"],["36549.78","0.6751"],["36545.01","0.06889"],["36539.52","0.13103"],["36533.43","0.13132"],["36527.09","0.07364"],["36520.75","0.08717"],["36514.4","0.07126"],["36508.06","0.07884"],["36501.71","0.08665"],["36495.8","0.07645"],["36489.8","0.1265"],["36483.73","0.16936"],["36476.54","0.10274"],["36469.25","0.09852"],["36465.71","0.4964"],["36381.83","0.4964"],["36289.98","0.011"],["36282.88","0.01"],["36054.39","0.3885"],["36000","0.8"],["35800","0.5"],["35000","1"],["34888","0.6183"],["34123","0.1"],["22840","1"],["22222","0.82015"]],"a":[["36721.76","0.0004"],["36725.87","0.12"],["36725.88","0.05925"],["36730.74","0.12682"],["36733.37","0.00092"],["36733.73","0.3402"],["36735.61","0.1203"],["36739.28","0.09763"],["36742.95","0.14815"],["36744.75","1.2773"],["36746.62","0.11678"],["36751.17","0.10615"],["36751.73","0.00092"],["36755.81","0.12864"],["36760.41","0.08357"],["36763.13","0.2504"],["36765.46","0.11877"],["36765.64","0.011"],["36766.18","0.011"],["36770.08","0.00093"],["36772.02","0.0116"],["36772.41","0.012"],["36772.43","0.14091"],["36776.81","0.2588"],["36777.08","0.14268"],["36778.2","0.025"],["36778.68","0.011"],["36782.16","0.08431"],["36786.4","0.13168"],["36791.92","0.14752"],["36795.2","0.2588"],["36796.12","0.18056"],["36800.59","0.13275"],["36805.08","0.10169"],["36809.64","0.11868"],["36815.17","0.16517"],["36820.7","0.12655"],["36825.69","0.08961"],["36830.69","0.07015"],["36835.66","0.12908"],["36842.55","0.09903"],["36851.42","0.08474"],["36856.05","0.18422"],["36861.58","0.08518"],["36867.11","0.13926"],["36872.65","0.10482"],["36878.19","0.19701"],["36879.51","0.6799"],["36884.66","0.0894"],["36891.16","0.13228"],["36897.67","0.18591"],["36904.48","0.14018"],["36911.33","0.14817"],["36918.28","0.17455"],["36925.38","0.099"],["36932.48","0.09554"],["36940.09","0.18158"],["36964.34","0.4999"],["37049.36","0.4999"],["37052.31","0.012"],["37295.62","0.08188"],["37306.21","0.0068"],["37382.81","0.3912"],["38515","0.0099"],["38516","2.50778"]],"o":0}],"f":true,"sendTime":1699953001725,"shared":false}

  }

}