package org.knowm.xchange.mexc;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.mexc.service.MEXCWsTokenService;

@Slf4j
public class MEXCStreamingPool {

  private static final int MAX_SERVICES_PER_TOKEN = 5;

  private Collection<MEXCStreamingService> services;
  private int maxTopic;
  private String url;
  private MEXCWsTokenService tokenService;
  private int privateCount;
  private String currentToken;

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
   * 初始化 MEXCStreamingPool
   * <pre>
   *
   * </pre>
   *
   * @param initPoolSize 初始连接池大小
   * @param maxTopic     单个服务实例的最大订阅主题数
   * @param url          WebSocket URL
   * @param tokenService 用于获取token的服务
   */
  public MEXCStreamingPool(int initPoolSize, int maxTopic, String url,
      MEXCWsTokenService tokenService) {
    this.maxTopic = maxTopic;
    this.url = url;
    this.tokenService = tokenService;
    this.services = new ArrayList<>(initPoolSize);
    this.privateCount = 0;

    // 初始化连接池：创建多个 MEXCStreamingService 实例
    for (int i = 0; i < initPoolSize; i++) {
      createNewService(false);
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
        .map(service -> service.connect()
            .doOnComplete(() -> log.info("Service is connected:{}", service)))
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

  private String getCurrentToken() {
    if (Strings.isNullOrEmpty(currentToken)) {
      try {
        currentToken = tokenService.getWsToken().getListenKey();
        log.debug("首次创建获取token: {}", currentToken);
      } catch (IOException e) {
        throw new RuntimeException("无法获取token", e);
      }
    }
    return currentToken;
  }

  private MEXCStreamingService createNewService(boolean onceConnect) {
    MEXCStreamingService newService;

    if (tokenService != null) {
      // 如果有tokenService，表示是私有服务，需要获取token
      String token = getCurrentToken();
      if (privateCount > MAX_SERVICES_PER_TOKEN) {
        privateCount = 1;
        currentToken = token;
      } else {
        privateCount++;
      }
      String privateUrl = String.format(url, token);
      newService = new MEXCStreamingService(privateUrl, tokenService);
    } else {
      // 如果没有tokenService，表示不是私有服务
      newService = new MEXCStreamingService(url);
    }

    services.add(newService);
    if (onceConnect) {
      newService.connect().blockingAwait();
    }
    log.info("添加了一个新的服务实例，并自动链接;");
    return newService;
  }
}
