package org.knowm.xchange.mexc;

import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> @Date : 2023/12/7 </p>
 * <p> @Project : XChange</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MEXCStreamingPool {

  private Collection<MEXCStreamingService> services;
  private int maxTopic;
  private String url;

  /**
   * 初始化 MEXCStreamingPool
   *
   * @param initPoolSize 初始连接池大小
   * @param maxTopic     单个服务实例的最大订阅主题数
   * @param url          WebSocket URL
   */
  public MEXCStreamingPool(int initPoolSize, int maxTopic, String url) {
    this.maxTopic = maxTopic;
    this.url = url;
    this.services = new ArrayList<>(initPoolSize);

    // 初始化连接池：创建多个 MEXCStreamingService 实例
    for (int i = 0; i < initPoolSize; i++) {
      services.add(new MEXCStreamingService(url));
    }
  }

  /**
   * 初始化服务实例，返回一个 Completable 表示完成
   *
   * @return Completable，表示初始化连接完成
   */
  public Completable initializeServices() {
    log.info("正在初始化 MEXCStreamingService 实例。");
    // 创建包含所有连接操作的 Completable 列表
    List<Completable> connectCompletables = services.stream()
        .map(service -> service.connect().doOnComplete(() -> log.info("服务已连接：{}", service)))
        .collect(Collectors.toList());

    // 使用 mergeDelayError 合并所有 Completable，确保其中一个失败不会中断整个流程
    return Completable.defer(() ->
        Completable.mergeDelayError(connectCompletables)
            .subscribeOn(Schedulers.io())
            .doOnComplete(() -> log.info("所有服务均成功连接。"))
            .doOnError(error -> log.error("连接服务时发生错误：{}", error.getMessage(), error))
    );
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
    MEXCStreamingService selectedService = getFirstAvailableService();
    if (selectedService == null) {
      // 如果没有可用的服务实例，动态扩容
      selectedService = addNewService();
    }
    // 使用选定的服务实例订阅频道
    return selectedService.subscribeChannel(channelName, args);
  }

  /**
   * 获取负载最轻的服务实例
   *
   * @return 负载最轻的服务实例，如果没有可用实例则返回 null
   */
  private MEXCStreamingService getFirstAvailableService() {
    MEXCStreamingService firstAvailableService = null;

    // 遍历服务实例，找到第一个有可用容量的服务
    for (MEXCStreamingService service : services) {
      int currentTopics = service.getChannelSize();
      if (currentTopics < maxTopic) {
        firstAvailableService = service;
        break;
      }
    }

    return firstAvailableService;
  }

  /**
   * 动态扩容，添加新的服务实例
   *
   * @return 新添加的服务实例
   */
  private MEXCStreamingService addNewService() {
    MEXCStreamingService newService = new MEXCStreamingService(url); // 使用新的 URL 或传递其他参数
    services.add(newService);
    newService.connect().blockingAwait();
    log.info("动态添加了一个新的服务实例,并自动链接;");
    return newService;
  }
}