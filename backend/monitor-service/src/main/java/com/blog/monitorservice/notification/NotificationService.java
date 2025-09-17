package com.blog.monitorservice.notification;

import com.blog.monitorservice.alert.model.AlertMessage;

/**
 * 通知服务接口，用于发送告警通知
 */
public interface NotificationService {
    
    /**
     * 发送告警通知
     * @param alertMessage 告警消息
     * @return 是否发送成功
     */
    boolean sendNotification(AlertMessage alertMessage);
    
    /**
     * 获取通知渠道类型
     * @return 通知渠道类型
     */
    String getChannelType();
    
    /**
     * 检查通知服务是否可用
     * @return 是否可用
     */
    boolean isAvailable();
}