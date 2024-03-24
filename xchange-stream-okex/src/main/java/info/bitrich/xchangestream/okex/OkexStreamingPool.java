package info.bitrich.xchangestream.okex;

import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.ExchangeSpecification;

@Slf4j
public class OkexStreamingPool {

  private Collection<OkexStreamingService> services;
  private int maxTopic;
  private String url;
  private ExchangeSpecification xSpec;

  /**
   * 初始化 MEXCStreamingPool
   *
   * @param initPoolSize 初始连接池大小
   * @param maxTopic     单个服务实例的最大订阅主题数
   * @param url          WebSocket URL
   */
  public OkexStreamingPool(int initPoolSize, int maxTopic, String url,
      ExchangeSpecification xSpec) {
    this.maxTopic = maxTopic;
    this.url = url;
    this.services = new ArrayList<>(initPoolSize);
    this.xSpec = xSpec;
    // 初始化连接池：创建多个 MEXCStreamingService 实例
    for (int i = 0; i < initPoolSize; i++) {
      services.add(new OkexStreamingService(url, xSpec));
    }
  }


  /**
   * 初始化服务实例，返回一个 Completable 表示完成
   *
   * @return Completable，表示初始化连接完成
   */
  public Completable initializeServices() {
    log.info("正在初始化 OkexStreamingService 实例。");
    // 创建包含所有连接操作的 Completable 列表
    List<Completable> connectCompletables = services.stream().map(service -> service.connect()
            .doOnComplete(() -> log.info("Service is connected:{}", service)))
        .collect(Collectors.toList());

    // 使用 mergeDelayError 合并所有 Completable，确保其中一个失败不会中断整个流程
    return Completable.defer(
        () -> Completable.mergeDelayError(connectCompletables).subscribeOn(Schedulers.io())
            .doOnComplete(() -> log.info("所有服务均成功连接。"))
            .doOnError(error -> log.error("连接服务时发生错误：{}", error.getMessage(), error)));
  }


  /**
   * 订阅频道，根据当前负载动态选择服务实例
   *
   * @param channelName 频道名称
   * @param args        订阅参数
   * @return Observable，表示订阅的结果
   */
  public Observable<JsonNode> subscribeChannel(String channelName, Object... args) {
    // 找到当前负载最轻的服务实例
    OkexStreamingService selectedService = getFirstAvailableService();
    if (selectedService == null) {
      // 如果没有可用的服务实例，动态扩容
      selectedService = createNewService(true);
    }
    // 使用选定的服务实例订阅频道
    return selectedService.subscribeChannel(channelName, args);
  }

  /**
   * 获取负载最轻的服务实例
   *
   * @return 负载最轻的服务实例，如果没有可用实例则返回 null
   */
  private OkexStreamingService getFirstAvailableService() {
    OkexStreamingService firstAvailableService = null;

    // 遍历服务实例，找到第一个有可用容量的服务
    for (OkexStreamingService service : services) {
      int currentTopics = service.getChannelSize();
      if (currentTopics < maxTopic) {
        firstAvailableService = service;
        break;
      }
    }

    return firstAvailableService;
  }

  private OkexStreamingService createNewService(boolean onceConnect) {
    OkexStreamingService newService = new OkexStreamingService(this.url, this.xSpec);

    services.add(newService);
    if (onceConnect) {
      newService.connect().blockingAwait();
    }
    log.info("添加了一个新的服务实例，并自动链接;");
    return newService;
  }
}
