package com.blog.monitorservice.notification;

import com.blog.monitorservice.alert.model.AlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 通知管理器，用于协调多个通知服务
 */
@Component
public class NotificationManager {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);
    
    private final Map<String, NotificationService> notificationServices = new ConcurrentHashMap<>();
    
    @Autowired
    public NotificationManager(List<NotificationService> services) {
        for (NotificationService service : services) {
            notificationServices.put(service.getChannelType(), service);
            logger.info("注册通知服务: {}", service.getChannelType());
        }
    }
    
    /**
     * 发送告警通知到所有可用的通知服务
     * @param alertMessage 告警消息
     * @return 发送结果
     */
    public Map<String, Boolean> sendNotification(AlertMessage alertMessage) {
        Map<String, Boolean> results = new ConcurrentHashMap<>();
        
        notificationServices.forEach((channelType, service) -> {
            if (service.isAvailable()) {
                try {
                    boolean success = service.sendNotification(alertMessage);
                    results.put(channelType, success);
                } catch (Exception e) {
                    logger.error("发送通知失败 ({}): {}", channelType, e.getMessage());
                    results.put(channelType, false);
                }
            } else {
                logger.debug("通知服务不可用: {}", channelType);
                results.put(channelType, false);
            }
        });
        
        return results;
    }
    
    /**
     * 获取所有通知服务
     */
    public Map<String, NotificationService> getNotificationServices() {
        return notificationServices;
    }
    
    /**
     * 获取指定类型的通知服务
     */
    public NotificationService getNotificationService(String channelType) {
        return notificationServices.get(channelType);
    }
    
    /**
     * 获取可用的通知服务列表
     */
    public List<String> getAvailableChannels() {
        return notificationServices.entrySet().stream()
                .filter(entry -> entry.getValue().isAvailable())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 检查是否有可用的通知服务
     */
    public boolean hasAvailableChannel() {
        return notificationServices.values().stream().anyMatch(NotificationService::isAvailable);
    }
}